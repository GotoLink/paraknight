package core;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntityChestBoat extends Entity implements IInventory {
	public double speed = 0;
	protected ItemStack[] cargo;
	protected double speedMultiplier, maxSpeed, minSpeed;
	private double boatPitch;
	private int boatPosRotationIncrements;
	private double boatX, boatY, boatZ;
	private double boatYaw;
	private boolean isEmpty;
	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;

	public EntityChestBoat(World par1World) {
		super(par1World);
		this.isEmpty = true;
		cargo = new ItemStack[5];
		this.speedMultiplier = 0.14D;
		this.maxSpeed = 0.35D;
		this.minSpeed = 0.07D;
		this.preventEntitySpawning = true;
		this.setSize(1.5F, 0.6F);
		this.yOffset = this.height / 2.0F;
	}

	public EntityChestBoat(World world, double par2, double par4, double par6) {
		this(world);
		this.setPosition(par2, par4 + this.yOffset, par6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}

	public boolean addFuel() {
		if (cargo[0] == null) {
			cargo[0] = new ItemStack(Item.coal);
			return true;
		} else if (cargo[0].stackSize >= getInventoryStackLimit())
			return false;
		cargo[0].stackSize++;
		onInventoryChanged();
		return true;
	}

	public boolean addItemStackToCargo(ItemStack itemstack) {
		if (!itemstack.isItemDamaged()) {
			int i;
			do {
				i = itemstack.stackSize;
				itemstack.stackSize = storePartialItemStack(itemstack);
			} while (itemstack.stackSize > 0 && itemstack.stackSize < i);
			onInventoryChanged();
			return itemstack.stackSize < i;
		}
		int j = getFirstEmptyStack();
		if (j > 0) {
			cargo[j] = ItemStack.copyItemStack(itemstack);
			cargo[j].animationsToGo = 5;
			itemstack.stackSize = 0;
			onInventoryChanged();
			return true;
		} else
			return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else if (!this.worldObj.isRemote && !this.isDead) {
			worldObj.playSoundAtEntity(this, SoundHandler.FOLDER + "clank", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			if (source.isFireDamage())
				this.setDamageTaken(this.getDamageTaken() + par2 * 15);
			else
				this.setDamageTaken(this.getDamageTaken() + par2 * 8);
			this.setBeenAttacked();
			if (source.getEntity() instanceof EntityPlayer && ((EntityPlayer) source.getEntity()).capabilities.isCreativeMode) {
				this.setDamageTaken(100);
				if (this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}
				this.entityDropItem(new ItemStack(ModPack.instance.ride.itemID, 1, getItemDamage()), 0.0f);
				this.setDead();
			}
			if (this.getDamageTaken() > 80) {
				if (this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}
				this.setDead();
				this.onDeath();
			}
		}
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public void closeChest() {
	}

	public void decrementAnimations() {
		for (int i = 0; i < cargo.length; i++)
			if (cargo[i] != null)
				cargo[i].updateAnimation(this.worldObj, this.riddenByEntity, i, false);// currentItem boolean
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack aitemstack[] = cargo;
		if (aitemstack[i] != null) {
			if (aitemstack[i].stackSize <= j) {
				ItemStack itemstack = aitemstack[i];
				aitemstack[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = aitemstack[i].splitStack(j);
			if (aitemstack[i].stackSize == 0)
				aitemstack[i] = null;
			onInventoryChanged();
			return itemstack1;
		} else
			return null;
	}

	@SideOnly(Side.CLIENT)
	public void func_70270_d(boolean par1) {
		this.isEmpty = par1;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	public float getDamageTaken() {
		return this.dataWatcher.getWatchableObjectFloat(19);
	}

	public int getForwardDirection() {
		return this.dataWatcher.getWatchableObjectInt(18);
	}

	public abstract int getFuelDuration();

	public int getFuelTime() {
		return this.dataWatcher.getWatchableObjectInt(25);
	}

	public int getInventorySlotContainItem(int i) {
		for (int j = 0; j < cargo.length; j++)
			if (cargo[j] != null && cargo[j].itemID == i)
				return j;
		return -1;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public abstract int getItemDamage();

	@Override
	public double getMountedYOffset() {
		return this.height * 0.0D - 0.30000001192092896D;
	}

	@Override
	public int getSizeInventory() {
		return cargo.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return cargo[i];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	public int getTimeInWater() {
		return this.dataWatcher.getWatchableObjectInt(26);
	}

	public int getTimeSinceHit() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	public abstract void handleSoundEffects();

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack item) {
		return (i == 0 && item.itemID == Item.coal.itemID) || i > 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.isDead ? false : player.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public void onInventoryChanged() {
		setFuelTime(cargo[0] != null ? Math.round(cargo[0].stackSize * 100 / 64) : 0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}
		if (this.getFuelTime() > 0) {
			this.setFuelTime(this.getFuelTime() - 1);
		}
		if (this.getFuelTime() == 0) {
			if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && ((EntityPlayer) this.riddenByEntity).inventory.hasItem(263)) {
				this.setFuelTime(getFuelDuration());
				((EntityPlayer) this.riddenByEntity).inventory.consumeInventoryItem(263);
			}
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		double d0 = 0.0D;
		double d3 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double d4;
		double d5;
		if (d3 > 0.2625D) {
			d4 = Math.cos(this.rotationYaw * Math.PI / 180.0D);
			d5 = Math.sin(this.rotationYaw * Math.PI / 180.0D);
		}
		double d10;
		double d11;
		if (this.worldObj.isRemote && this.isEmpty) {
			if (this.boatPosRotationIncrements > 0) {
				d4 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
				d5 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
				d11 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
				d10 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
				this.rotationYaw = (float) (this.rotationYaw + d10 / this.boatPosRotationIncrements);
				this.rotationPitch = (float) (this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(d4, d5, d11);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				d4 = this.posX + this.motionX;
				d5 = this.posY + this.motionY;
				d11 = this.posZ + this.motionZ;
				this.setPosition(d4, d5, d11);
				if (this.getFuelTime() == 0) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}
				this.motionX *= 0.99D;
				this.motionY *= 0.95D;
				this.motionZ *= 0.99D;
			}
		} else {
			if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
				d4 = ((EntityLivingBase) this.riddenByEntity).moveForward;
				d0 = ((EntityLivingBase) this.riddenByEntity).moveStrafing;
				d5 = -Math.sin(this.riddenByEntity.rotationYaw * (float) Math.PI / 180.0F);
				double d21 = Math.cos(this.riddenByEntity.rotationYaw * (float) Math.PI / 180.0F);
				this.motionX += (d0 * d21 + d5 * d4) * this.speedMultiplier;
				this.motionZ += (d4 * d21 - d0 * d5) * this.speedMultiplier;
			}
			d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			if (d4 > maxSpeed) {
				d5 = maxSpeed / d4;
				this.motionX *= d5;
				this.motionZ *= d5;
				d4 = maxSpeed;
			}
			if (d4 > d3 && this.speedMultiplier < maxSpeed) {
				this.speedMultiplier += (maxSpeed - this.speedMultiplier) / (maxSpeed * 100);
				if (this.speedMultiplier > maxSpeed) {
					this.speedMultiplier = maxSpeed;
				}
			} else {
				this.speedMultiplier -= (this.speedMultiplier - minSpeed) / (maxSpeed * 100);
				if (this.speedMultiplier < minSpeed) {
					this.speedMultiplier = minSpeed;
				}
			}
			if (this.getFuelTime() == 0) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if (this.isCollidedHorizontally && d3 > 0.2D) {
				if (!this.worldObj.isRemote && !this.isDead) {
					this.setDead();
				}
			} else {
				this.motionX *= 0.99D;
				this.motionY *= 0.95D;
				this.motionZ *= 0.99D;
			}
			this.rotationPitch = 0.0F;
			d5 = this.rotationYaw;
			d11 = this.prevPosX - this.posX;
			d10 = this.prevPosZ - this.posZ;
			if (d11 * d11 + d10 * d10 > 0.001D) {
				d5 = ((float) (Math.atan2(d10, d11) * 180.0D / Math.PI));
			}
			double d12 = MathHelper.wrapAngleTo180_double(d5 - this.rotationYaw);
			if (d12 > 20.0D) {
				d12 = 20.0D;
			}
			if (d12 < -20.0D) {
				d12 = -20.0D;
			}
			this.rotationYaw = (float) (this.rotationYaw + d12);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			if (!this.worldObj.isRemote) {
				List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
				int l;
				if (list != null && !list.isEmpty()) {
					for (l = 0; l < list.size(); ++l) {
						Entity entity = (Entity) list.get(l);
						if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat) {
							entity.applyEntityCollision(this);
						}
					}
				}
				for (l = 0; l < 4; ++l) {
					int i1 = MathHelper.floor_double(this.posX + (l % 2 - 0.5D) * 0.8D);
					int j1 = MathHelper.floor_double(this.posZ + (l / 2 - 0.5D) * 0.8D);
					for (int k1 = 0; k1 < 2; ++k1) {
						int l1 = MathHelper.floor_double(this.posY) + k1;
						int i2 = this.worldObj.getBlockId(i1, l1, j1);
						if (i2 == Block.snow.blockID) {
							this.worldObj.setBlockToAir(i1, l1, j1);
						}
					}
				}
				if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
					this.riddenByEntity = null;
				}
			}
		}
		updateSpeedModel();
		handleEntityCollisions();
		handleBlockCollisions();
		handleDamage();
		handleSoundEffects();
		handleParticleEffects();
	}

	@Override
	public void openChest() {
	}

	public void readFromNBT(NBTTagList nbttaglist) {
		cargo = new ItemStack[5];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
			if (itemstack.getItem() == null)
				continue;
			if (j >= 0 && j < cargo.length)
				cargo[j] = itemstack;
		}
		onInventoryChanged();
	}

	public void setDamageTaken(float par1) {
		this.dataWatcher.updateObject(19, Float.valueOf(par1));
	}

	public void setForwardDirection(int par1) {
		this.dataWatcher.updateObject(18, Integer.valueOf(par1));
	}

	public void setFuelTime(int par1) {
		this.dataWatcher.updateObject(25, Integer.valueOf(par1));
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		cargo[i] = itemstack;
		onInventoryChanged();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		if (this.isEmpty) {
			this.boatPosRotationIncrements = par9 + 5;
		} else {
			double d3 = par1 - this.posX;
			double d4 = par3 - this.posY;
			double d5 = par5 - this.posZ;
			double d6 = d3 * d3 + d4 * d4 + d5 * d5;
			if (d6 <= 1.0D) {
				return;
			}
			this.boatPosRotationIncrements = 3;
		}
		this.boatX = par1;
		this.boatY = par3;
		this.boatZ = par5;
		this.boatYaw = par7;
		this.boatPitch = par8;
		this.motionX = this.velocityX;
		this.motionY = this.velocityY;
		this.motionZ = this.velocityZ;
	}

	public void setTimeInWater(int par1) {
		this.dataWatcher.updateObject(26, Integer.valueOf(par1));
	}

	public void setTimeSinceHit(int par1) {
		this.dataWatcher.updateObject(17, Integer.valueOf(par1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double par1, double par3, double par5) {
		this.velocityX = this.motionX = par1;
		this.velocityY = this.motionY = par3;
		this.velocityZ = this.motionZ = par5;
	}

	public abstract void updateSpeedModel();

	public NBTTagList writeToNBT(NBTTagList nbttaglist) {
		for (int i = 0; i < cargo.length; i++)
			if (cargo[i] != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				cargo[i].writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		return nbttaglist;
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(1));
		this.dataWatcher.addObject(19, new Float(0.0F));
		this.dataWatcher.addObject(25, new Integer(0));
		this.dataWatcher.addObject(26, new Integer(0));
	}

	protected void handleBlockCollisions() {
		if (isInWater())
			setTimeInWater(getTimeInWater() + 1);
		else
			setTimeInWater(0);
		if (getTimeInWater() > 20) {
			this.setDamageTaken(this.getDamageTaken() + 1);
			setTimeInWater(0);
		}
	}

	protected void handleDamage() {
		if (this.getDamageTaken() > 60 && rand.nextInt(100) < 5)
			this.setDamageTaken(this.getDamageTaken() + 1);
	}

	protected void handleParticleEffects() {
		if (this.getFuelTime() > 0)
			worldObj.spawnParticle("smoke", posX, posY - 0.15, posZ, 0, 0, 0);
		if (this.getDamageTaken() > 50 && !isInWater()) {
			double xOffset = -Math.sin(Math.toRadians(rotationYaw + 50 - rand.nextInt(101))) * 0.8;
			double zOffset = Math.cos(Math.toRadians(rotationYaw + 50 - rand.nextInt(101))) * 0.8;
			worldObj.spawnParticle("smoke", posX + xOffset, posY, posZ + zOffset, 0, 0, 0);
			if (this.getDamageTaken() > 60)
				worldObj.spawnParticle("flame", posX + xOffset, posY, posZ + zOffset, 0, 0, 0);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		readFromNBT(nbttagcompound.getTagList("Cargo"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("Cargo", writeToNBT(new NBTTagList()));
	}

	private int getFirstEmptyStack() {
		for (int i = 1; i < cargo.length; i++)
			if (cargo[i] == null)
				return i;
		return -1;
	}

	private void handleEntityCollisions() {
		List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2D, 0.0D, 0.2D));
		Iterator var2 = var1.iterator();
		while (var2.hasNext()) {
			Entity var3 = (Entity) var2.next();
			if (var3 instanceof EntityItem && ((EntityItem) var3).getEntityItem().itemID == Item.seeds.itemID
					&& this.addItemStackToCargo(new ItemStack(Item.seeds, ((EntityItem) var3).getEntityItem().stackSize))) {
				var3.setDead();
			}
			if (this.riddenByEntity != null && var3 != this.riddenByEntity && var3 instanceof EntityLiving && this.speed > 0.0D && var3.attackEntityFrom(DamageSource.generic, 4)) {
				this.worldObj.playSoundAtEntity(var3, "damage.hurtflesh", 1.0F, 0.5F);
			}
			if (var3 != this.riddenByEntity && var3.canBePushed()) {
				var3.applyEntityCollision(this);
			}
		}
	}

	private void onDeath() {
		worldObj.createExplosion(this, posX, posY, posZ, this.getFuelTime() / 25, true);
		cargo[0] = null;
		for (int i = 1; i < cargo.length; i++)
			if (cargo[i] != null) {
				for (int j = 0; j < cargo[i].stackSize; j++)
					this.entityDropItem(cargo[i], (new Random().nextFloat() - 0.5F) * 2);
				cargo[i] = null;
			}
	}

	private int storeItemStack(ItemStack itemstack) {
		for (int i = 1; i < cargo.length; i++)
			if (cargo[i] != null && cargo[i].itemID == itemstack.itemID && cargo[i].isStackable() && cargo[i].stackSize < cargo[i].getMaxStackSize() && cargo[i].stackSize < getInventoryStackLimit()
					&& (!cargo[i].getHasSubtypes() || cargo[i].getItemDamage() == itemstack.getItemDamage()))
				return i;
		return -1;
	}

	private int storePartialItemStack(ItemStack itemstack) {
		int i = itemstack.itemID;
		int j = itemstack.stackSize;
		int k = storeItemStack(itemstack);
		if (k < 0)
			k = getFirstEmptyStack();
		if (k < 0)
			return j;
		if (cargo[k] == null)
			cargo[k] = new ItemStack(i, 0, itemstack.getItemDamage());
		int l = j;
		if (l > cargo[k].getMaxStackSize() - cargo[k].stackSize)
			l = cargo[k].getMaxStackSize() - cargo[k].stackSize;
		if (l > getInventoryStackLimit() - cargo[k].stackSize)
			l = getInventoryStackLimit() - cargo[k].stackSize;
		if (l == 0)
			return j;
		else {
			j -= l;
			cargo[k].stackSize += l;
			cargo[k].animationsToGo = 5;
			return j;
		}
	}
}
