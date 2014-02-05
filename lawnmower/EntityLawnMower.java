package lawnmower;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import core.CommonProxy;
import core.EntityChestBoat;
import core.ModPack;

public class EntityLawnMower extends EntityChestBoat {
	public float bladesAngle = ((float) Math.PI / 4F);

	public EntityLawnMower(World var1) {
		super(var1);
		this.setSize(0.98F, 0.98F);
	}

	public EntityLawnMower(World world, double d, double e, double f) {
		this(world);
		this.setPosition(d, e + yOffset, f);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = d;
		this.prevPosY = e;
		this.prevPosZ = f;
	}

	@Override
	public int getFuelDuration() {
		return 400;
	}

	@Override
	public String func_145825_b() {
		return "Lawn Mower";
	}

	@Override
	public int getItemDamage() {
		return 2;
	}

	@Override
	public void handleParticleEffects() {
		double var1;
		double var3;
		if (this.speed > 3.625D) {
			var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D);
			var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D);
			for (int var5 = 0; var5 < 1.0D + this.speed * 60.0D; ++var5) {
				double var6 = this.rand.nextFloat() * 2.0F - 1.0F;
				double var8 = (this.rand.nextInt(2) * 2 - 1) * 0.7D;
				if (this.rand.nextInt(100) == 0) {
					double var10;
					double var12;
					if (this.rand.nextBoolean()) {
						var10 = this.posX - var1 * var6 * 0.8D + var3 * var8;
						var12 = this.posZ - var3 * var6 * 0.8D - var1 * var8;
						//ModPack.proxy.addSlimeEffect(new EntitySlimeFX(this.worldObj, var10, this.posY - 0.125D, var12, Item.seeds));
						worldObj.spawnParticle("slime", var10, this.posY - 0.125D, var12, 0, 0, 0);
					} else {
						var10 = this.posX + var1 + var3 * var6 * 0.7D;
						var12 = this.posZ + var3 - var1 * var6 * 0.7D;
						//ModPack.proxy.addSlimeEffect(new EntitySlimeFX(this.worldObj, var10, this.posY - 0.125D, var12, Item.seeds));
						worldObj.spawnParticle("slime", var10, this.posY - 0.125D, var12, 0, 0, 0);
					}
				}
			}
		}
		super.handleParticleEffects();
	}

	@Override
	public void handleSoundEffects() {
		if (this.riddenByEntity != null && this.getFuelTime() > 0) {
			this.worldObj.playSoundAtEntity(this, ModPack.FOLDER + "lawnmower", 0.1F + (float) (this.speed / 7.0D), (float) (this.speed / 6.0D));
		}
	}

    @Override
    public double getMaxCollisionSpeed() {
        return 2D;
    }

    @Override
	public boolean interactFirst(EntityPlayer var1) {
		ItemStack var2 = var1.getCurrentEquippedItem();
		if (var2 != null) {
			if (var2.getItem() == Items.coal) {
				if (this.addFuel()) {
					--var2.stackSize;
					if (var2.stackSize <= 0) {
						var1.inventory.mainInventory[var1.inventory.currentItem] = null;
					}
					return true;
				}
				return false;
			}
			if (var2.getItem() == ModPack.wrench) {
				if (this.getDamageTaken() >= 10) {
					this.setDamageTaken(this.getDamageTaken() - 10);
					var2.damageItem(1, var1);
					if (var2.getItemDamage() <= 0)
						var1.destroyCurrentEquippedItem();
					worldObj.playSoundAtEntity(this, "note.hat", 1F, 1F);
					return true;
				}
				return false;
			}
			if (var2.getItem() == ModPack.ride && var2.getItemDamage() == 3) {
				if (this.riddenByEntity != null && this.riddenByEntity != var1 && this.riddenByEntity instanceof EntityPlayer) {
					return true;
				} else {
					this.worldObj.playSoundAtEntity(this, ModPack.FOLDER + "ignition", 1.0F, 1.0F);
					if (!this.worldObj.isRemote)
						var1.mountEntity(this);
					return true;
				}
			}
		} else {
			var1.openGui(ModPack.instance, CommonProxy.GUI, this.worldObj, (int) this.posX, (int) this.posY, (int) this.posZ);
			return false;
		}
		return false;
	}

	@Override
	public void updateRiderPosition() {
		if (this.riddenByEntity != null) {
			double var1 = Math.cos(Math.toRadians(this.rotationYaw)) * 0.4D;
			double var3 = Math.sin(Math.toRadians(this.rotationYaw)) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
		}
	}

	@Override
	public void updateSpeedModel() {
		this.speed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 25.0D;
		if (this.speed < 0.01D && this.speed > -0.01D) {
			this.speed = 0.0D;
		}
		this.bladesAngle += (float) this.speed / 4.0F;
		this.bladesAngle = this.bladesAngle >= 360.0F ? this.bladesAngle - 360.0F : this.bladesAngle;
		this.bladesAngle = this.bladesAngle < 0.0F ? this.bladesAngle + 360.0F : this.bladesAngle;
	}

	@Override
	protected void handleBlockCollisions() {
		super.handleBlockCollisions();
		for (int var1 = 0; var1 < 4; ++var1) {
			int var2 = MathHelper.floor_double(this.posX + (var1 % 2 - 0.5D) * 0.8D);
			int var3 = MathHelper.floor_double(this.posY);
			int var4 = MathHelper.floor_double(this.posZ + (var1 / 2 - 0.5D) * 0.8D);
			Block var5 = this.worldObj.func_147439_a(var2, var3, var4);
			if (var5 instanceof BlockTallGrass /* BlockFlower ? */&& this.riddenByEntity != null) {
				this.worldObj.func_147468_f(var2, var3, var4);
				if (this.rand.nextInt(8) == 0 && !this.addItemStackToCargo(ForgeHooks.getGrassSeed(worldObj))) {
					if (!this.worldObj.isRemote)
						this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, var2 + 0.5D, var3 + 0.5D, var4 + 0.5D, new ItemStack(var5, 1, this.worldObj.getBlockMetadata(
								var2, var3, var4))));
				}
			}
		}
	}
}
