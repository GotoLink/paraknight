package tvmod;

import com.xuggle.xuggler.*;
import core.ModPack;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class EntityTV extends Entity implements Runnable{
    public static ArrayList<String> videoPathes;
	private int tickCounter;
	public int direction, xPos, yPos, zPos;
	private String currentVideoPath;
	public BufferedImage lastFrame, noSignal;
	public IntBuffer frameIntBuffer;
	private SourceDataLine soundLine;
	public boolean isVideoPaused = false, shouldSkip = false, isVideoOver = false, isVideoPlaying = false;

	public EntityTV(World world) {
		super(world);
		tickCounter = 0;
		direction = 0;
		yOffset = 0.0F;
		setSize(0.5F, 0.5F);
	}

	public EntityTV(World world, int i, int j, int k, int l) {
		this(world);
		xPos = i;
		yPos = j;
		zPos = k;
		setPosAndAABB(l);
	}

    @Override
	protected void entityInit() {
	}

    @SideOnly(Side.CLIENT)
	private void initScreenBuffer() {
        frameIntBuffer = GLAllocation.createDirectIntBuffer(1);
		InputStream inputstream = this.getClass().getResourceAsStream("/assets/tvmod/textures/nosignal.png");
		try {
			BufferedImage noSignalImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = noSignalImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics2D.drawImage(ImageIO.read(inputstream), 0, 0, ModPack.width*16*(ModPack.HD?4:1), ModPack.height*16*(ModPack.HD?4:1), null);
			lastFrame = noSignal = noSignalImage;
			inputstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		frameIntBuffer.clear();
        GL11.glGenTextures(frameIntBuffer);
		//GLAllocation.generateTextureNames(frameIntBuffer);
	}

	public void setPosAndAABB(int orientation) {
		direction = orientation;
		prevRotationYaw = rotationYaw = orientation * 90;
		float xSize = ModPack.width;
		float ySize = ModPack.height;
		float zSize = ModPack.width;
		if (orientation == 0 || orientation == 2)
			zSize = ModPack.HD?0.0625F:0.015625F;
		else
			xSize = ModPack.HD?0.0625F:0.015625F;
		float xPosBlock = (float) xPos + 0.5F;
		float yPosBlock = (float) yPos + 0.5F;
		float zPosBlock = (float) zPos + 0.5F;
		float offsetOutOfBlock = 0.5625F;
		switch (orientation) {
		case 0:
			zPosBlock -= offsetOutOfBlock;
			xPosBlock -= (xSize%2==0)?0.5F:0;
			break;
		case 1:
			xPosBlock -= offsetOutOfBlock;
			zPosBlock += (zSize%2==0)?0.5F:0;
			break;
		case 2:
			zPosBlock += offsetOutOfBlock;
			xPosBlock += (xSize%2==0)?0.5F:0;
			break;
		case 3:
			xPosBlock += offsetOutOfBlock;
			zPosBlock -= (zSize%2==0)?0.5F:0;
			break;
		}
		yPosBlock += (ySize%2==0)?0.5F:0;
		setPosition(xPosBlock, yPosBlock, zPosBlock);
		float offset = -0.00625F;
		boundingBox.setBounds(xPosBlock - xSize/2 - offset, yPosBlock - ySize/2 - offset, zPosBlock - zSize/2 - offset, xPosBlock + xSize/2 + offset, yPosBlock + ySize/2 + offset, zPosBlock + zSize/2 + offset);
	}
	
	@Override
	public void onUpdate() {
        if(worldObj.isRemote) {
            if (videoPathes == null) {
                videoPathes = new ArrayList<String>();
                loadVideoPathes();
            }
            if(currentVideoPath == null)
                currentVideoPath = ModPack.shuffle?getRandomVideoPath():getNextVideoPath();
            if (noSignal == null) {
                initScreenBuffer();
            }
            if (soundLine != null && soundLine.isControlSupported(FloatControl.Type.MASTER_GAIN))
                ((FloatControl) soundLine.getControl(FloatControl.Type.MASTER_GAIN)).setValue(calculateVolume());
            if (isVideoOver) {
                shouldSkip = false;
                isVideoOver = false;
                if (isVideoPaused) {
                    isVideoPaused = false;
                    return;
                }
                currentVideoPath = ModPack.shuffle ? getRandomVideoPath() : getNextVideoPath();
                new Thread(this, "TVMod Processing").start();
            }
        }else if (tickCounter++ >= 100) {
			tickCounter = 0;
			if (!canStay()) {
				setDead();
			}
		}
	}

    @Override
    public void setDead(){
        super.setDead();
        if(!worldObj.isRemote)
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(ModPack.tv)));
    }

    @SideOnly(Side.CLIENT)
    private void loadVideoPathes() {
        File files[] = null, dir = new File(FMLClientHandler.instance().getClient().mcDataDir+"/resources/mod/TV/");
        if (dir.exists() || dir.mkdirs())
            files = dir.listFiles();
        if(files!=null)
            for (File file : files)
                videoPathes.add(file.toString());
    }

	private float calculateVolume() {
		float distToPlayer = getDistanceToEntity(this.worldObj.getClosestPlayerToEntity(this, 50));
		if(distToPlayer>ModPack.soundRange)
			return -80;
		if(distToPlayer==0)
			return 6;
		return ((1-(distToPlayer/ModPack.soundRange))*86)-80;
	}

	public boolean canStay() {
		if (worldObj.getCollidingBoundingBoxes(this, boundingBox).size() > 0)
			return false;
		int xBlockSize = ModPack.width;
		int yBlockSize = ModPack.height;
		int xBlockPos = xPos;
		int yBlockPos = yPos;
		int zBlockPos = zPos;
		if (direction == 0)
			xBlockPos = MathHelper.floor_double(posX - (double) ((float) xBlockSize/2));
		else if (direction == 1)
			zBlockPos = MathHelper.floor_double(posZ - (double) ((float) xBlockSize/2));
		else if (direction == 2)
			xBlockPos = MathHelper.floor_double(posX - (double) ((float) xBlockSize/2));
		else if (direction == 3)
			zBlockPos = MathHelper.floor_double(posZ - (double) ((float) xBlockSize/2));
		yBlockPos = MathHelper.floor_double(posY - (double) ((float) yBlockSize/2));
		for (int i = 0; i < xBlockSize; i++)
			for (int j = 0; j < yBlockSize; j++) {
				Material material;
				if (direction == 0 || direction == 2)
					material = worldObj.getBlock(xBlockPos + i, yBlockPos + j, zPos).getMaterial();
				else
					material = worldObj.getBlock(xPos, yBlockPos + j, zBlockPos + i).getMaterial();
				if (!material.isSolid())
					return false;
			}
		List<?> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);
        for (Object object : list)
            if (object instanceof EntityTV)
                return false;
		return true;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }

    @Override
    public boolean isPushedByWater()
    {
        return false;
    }

    @Override
	public boolean attackEntityFrom(DamageSource src, float i) {
		if (i>0.001F && !isDead && !worldObj.isRemote) {
			setBeenAttacked();
			setDead();
		}
		return true;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("TileX", xPos);
		nbttagcompound.setInteger("TileY", yPos);
		nbttagcompound.setInteger("TileZ", zPos);
		nbttagcompound.setByte("Dir", (byte) direction);
        if(currentVideoPath!=null)
		    nbttagcompound.setString("VidPath", currentVideoPath);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		xPos = nbttagcompound.getInteger("TileX");
		yPos = nbttagcompound.getInteger("TileY");
		zPos = nbttagcompound.getInteger("TileZ");
		direction = nbttagcompound.getByte("Dir");
		currentVideoPath = nbttagcompound.getString("VidPath");
		setPosAndAABB(direction);
	}
	
	@Override
	public void moveEntity(double d, double d1, double d2) {
		if (d * d + d1 * d1 + d2 * d2 > 0.001D) {
			setDead();
		}
	}
	
	@Override
	public void addVelocity(double d, double d1, double d2) {
		if (d * d + d1 * d1 + d2 * d2 > 0.001D) {
			setDead();
		}
	}

    @Override
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount){
        return false;
    }

    @SideOnly(Side.CLIENT)
	private String getRandomVideoPath() {
		if(videoPathes.size()<1)
			return null;
		return videoPathes.get(this.rand.nextInt(videoPathes.size()));
	}

    @SideOnly(Side.CLIENT)
	private String getNextVideoPath() {
		if(videoPathes.size()<1)
			return null;
		boolean pathFound = false;
		for (String path : videoPathes) {
			if(pathFound)
				return path;
			if(path.equals(currentVideoPath))
				pathFound = true;
		}
		return videoPathes.get(0);
	}

	@Override
	public void run() {
		isVideoPlaying  = true;
		IContainer container = IContainer.make();
		if(container.open(currentVideoPath, IContainer.Type.READ, null) < 0)
			if(container.open(currentVideoPath = ModPack.shuffle?getRandomVideoPath():getNextVideoPath(), IContainer.Type.READ, null) < 0)
				throw new IllegalArgumentException("Could not open video files.");

		IStreamCoder videoCoder=null, audioCoder=null;
		int videoStreamID = -1, audioStreamID = -1;
		int numStreams = container.getNumStreams();
		for (int i=0;i<numStreams;i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();

			if (videoStreamID == -1 && coder.getCodecType() == com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_VIDEO) {
				videoStreamID = i;
				videoCoder = coder;
			}
			else if (audioStreamID == -1 && coder.getCodecType() == com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_AUDIO) {
				audioStreamID = i;
				audioCoder = coder;
			}
		}

		if(videoStreamID != -1 && videoCoder!=null && videoCoder.open()>=0 && audioStreamID != -1 && audioCoder!=null && audioCoder.open()>=0) {

			IVideoResampler resampler = null;
			if(videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
				resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
				if (resampler == null)
					throw new RuntimeException("Could not get video resampler.");
			}

			AudioFormat audioFormat = new AudioFormat(audioCoder.getSampleRate(), (int) IAudioSamples.findSampleBitDepth(audioCoder.getSampleFormat()), audioCoder.getChannels(), true, false);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			try {
				soundLine = (SourceDataLine) AudioSystem.getLine(info);
				soundLine.open(audioFormat);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			soundLine.start();
			if (soundLine!=null&&soundLine.isControlSupported(FloatControl.Type.MASTER_GAIN))
				((FloatControl) soundLine.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-80);

			IPacket packet = IPacket.make();
			while(!shouldSkip&&!isDead)
				if(!isVideoPaused) {
					if(container.readNextPacket(packet)>=0) {
						if(packet.getStreamIndex()==videoStreamID) {
							IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
							int offset = 0;
							while(offset < packet.getSize()) {
								int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
								if(bytesDecoded < 0)
									throw new RuntimeException("Could not decode video.");
								offset += bytesDecoded;

								if(picture.isComplete()) {
									IVideoPicture newPict = null;
									if (picture.getPixelType() != IPixelFormat.Type.BGR24) {
										newPict = IVideoPicture.make(IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight());
										if(resampler.resample(newPict, picture)<0)
											throw new IllegalArgumentException("Could not resample video.");
									} else
										newPict = picture;

									sleepUntilReady(newPict);

									BufferedImage scaledImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
									Graphics2D graphics2D = scaledImage.createGraphics();
									graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
									graphics2D.drawImage(Utils.videoPictureToImage(newPict), 0, 0, ModPack.width*16*(ModPack.HD?4:1), ModPack.height*16*(ModPack.HD?4:1), null);
									lastFrame = scaledImage;
								}
							}
						} else if (packet.getStreamIndex()==audioStreamID) {
							IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
							int offset = 0;
							while(offset < packet.getSize()) {
								int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
								if (bytesDecoded < 0)
									throw new RuntimeException("Error in decoding video sound.");
								offset += bytesDecoded;
								if (samples.isComplete()) {
									byte[] rawBytes = samples.getData().getByteArray(0, samples.getSize());
									soundLine.write(rawBytes, 0, samples.getSize());

								}
							}
						}
					} else {
						break;
					}
				}
			if (videoCoder != null) {
				videoCoder.close();
				videoCoder = null;
			}
			if (audioCoder != null) {
				audioCoder.close();
				audioCoder = null;
			}
			if (container != null) {
				container.close();
				container = null;
			}
			if (soundLine != null) {
				soundLine.drain();
				soundLine.close();
				soundLine = null;
			}
		}
		isVideoOver = true;
		isVideoPlaying = false;
	}

	private long firstTimestampInStream = Global.NO_PTS, systemClockStartTime = 0;

	private void sleepUntilReady(IVideoPicture pict) {
		if (firstTimestampInStream == Global.NO_PTS) {
			firstTimestampInStream = pict.getTimeStamp();
			systemClockStartTime = System.currentTimeMillis();
		} else {
			final long millisecondsToSleep = (((pict.getTimeStamp()-firstTimestampInStream)/1000)-(System.currentTimeMillis() - systemClockStartTime + 50 /*tolerance*/));
			if (millisecondsToSleep > 0) {
				try{Thread.sleep(millisecondsToSleep);}catch(InterruptedException ignored){}}
		}
	}
}
