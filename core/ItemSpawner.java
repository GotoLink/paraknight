package core;

import java.util.List;

import lawnmower.EntityLawnMower;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import steambikes.EntityBlackWidow;
import steambikes.EntityMaroonMarauder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpawner extends Item {
	public static String[] name = new String[] { "maroonmarauder", "blackwidow", "lawnmower", "lawnmowerkey" };
	protected Icon[] icon;

	public ItemSpawner(int id) {
		super(id);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabTransport);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		int j = MathHelper.clamp_int(par1, 0, name.length);
		return this.icon[j];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int j = 0; j < name.length; ++j) {
			par3List.add(new ItemStack(par1, 1, j));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, name.length);
		return super.getUnlocalizedName() + name[i];
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		float f = 1.0F;
		float f1 = par3EntityPlayer.prevRotationPitch + (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f;
		float f2 = par3EntityPlayer.prevRotationYaw + (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f;
		double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * f;
		double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * f + 1.62D - par3EntityPlayer.yOffset;
		double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * f;
		Vec3 vec3 = par2World.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		MovingObjectPosition movingobjectposition = par2World.clip(vec3, vec31, true);
		if (movingobjectposition == null) {
			return par1ItemStack;
		} else {
			Vec3 vec32 = par3EntityPlayer.getLook(f);
			boolean flag = false;
			float f9 = 1.0F;
			List list = par2World.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer,
					par3EntityPlayer.boundingBox.addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand(f9, f9, f9));
			int i;
			for (i = 0; i < list.size(); ++i) {
				Entity entity = (Entity) list.get(i);
				if (entity.canBeCollidedWith()) {
					float f10 = entity.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f10, f10, f10);
					if (axisalignedbb.isVecInside(vec3)) {
						flag = true;
					}
				}
			}
			if (flag) {
				return par1ItemStack;
			} else {
				if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
					i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;
					if (par2World.getBlockId(i, j, k) == Block.snow.blockID) {
						--j;
					}
					Entity entity = makeEntity(par1ItemStack, par2World, i + 0.5F, j + 1F, k + 0.5F);
					if (entity != null) {
						entity.rotationYaw = ((MathHelper.floor_double(par3EntityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) - 1) * 90;
						//entityBike.setLocationAndAngles(i+0.5, j+1+entityBike.yOffset, k+0.5, par3EntityPlayer.rotationYaw, par3EntityPlayer.rotationPitch);
						if (!par2World.getCollidingBoundingBoxes(entity, entity.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
							return par1ItemStack;
						}
						if (!par2World.isRemote) {
							par2World.spawnEntityInWorld(entity);
						}
						if (!par3EntityPlayer.capabilities.isCreativeMode) {
							--par1ItemStack.stackSize;
						}
					}
				}
				return par1ItemStack;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.icon = new Icon[name.length];
		for (int i = 0; i < name.length; i++)
			this.icon[i] = par1IconRegister.registerIcon("paraknight:" + this.name[i]);
	}

	protected Entity makeEntity(ItemStack par1ItemStack, World par2World, double d, double e, double f) {
		switch (par1ItemStack.getItemDamage()) {
		case 0:
			return new EntityMaroonMarauder(par2World, d, e, f);
		case 1:
			return new EntityBlackWidow(par2World, d, e, f);
		case 2:
			return new EntityLawnMower(par2World, d, e, f);
		default:
			return null;
		}
	}
}
