package steambikes;

import core.CommonProxy;
import core.EntityChestBoat;
import core.ModPack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public abstract class EntitySteamBike extends EntityChestBoat {
	public float wheelAngle = 0, turnAngle = 0;

	public EntitySteamBike(World world) {
		super(world);
		this.setSize(2.5F, 1F);
		this.stepHeight = 1F;
		this.speedMultiplier = 1 / this.getFrictionFactor();
		this.maxSpeed = 2 / this.getFrictionFactor();
		this.minSpeed = 0.5D / this.getFrictionFactor();
	}

	public EntitySteamBike(World world, double d, double e, double f) {
		this(world);
		this.setPosition(d, e + yOffset, f);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = d;
		this.prevPosY = e;
		this.prevPosZ = f;
	}

	@SideOnly(Side.CLIENT)
	public abstract String getEntityTexture();

	public abstract double getFrictionFactor();

	@Override
	public String getInventoryName() {
		return "Steam Bike";
	}

	@Override
	public abstract int getItemDamage();

	@Override
	public double getMountedYOffset() {
		return this.height + 0.5F;
	}

	@Override
	public void handleSoundEffects() {
		if (riddenByEntity != null && this.getFuelTime() > 0) {
			worldObj.playSoundAtEntity(this, ModPack.FOLDER + "purr", 0.1F + (float) (speed / 14), (float) (speed / 6));
			if (this.getFuelTime() < 10 && rand.nextInt(75) == 0)
				worldObj.playSoundAtEntity(this, ModPack.FOLDER + "steam", 0.1F, rand.nextFloat());
		}
	}

	@Override
	public boolean interactFirst(EntityPlayer player) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			player.openGui(ModPack.instance, CommonProxy.GUI, this.worldObj, (int) this.posX, (int) this.posY, (int) this.posZ);
			return true;
		}
		ItemStack heldItem = player.getCurrentEquippedItem();
		if (heldItem != null) {
			if (heldItem.getItem() == Items.coal) {
				if (addFuel()) {
					if (--heldItem.stackSize <= 0)
						player.destroyCurrentEquippedItem();
					return true;
				}
				return false;
			}
			if (heldItem.getItem() == ModPack.wrench) {
				if (this.getDamageTaken() >= 10) {
					this.setDamageTaken(this.getDamageTaken() - 10);
					heldItem.damageItem(1, player);
					if (heldItem.getItemDamage() <= 0)
						player.destroyCurrentEquippedItem();
					worldObj.playSoundAtEntity(this, "note.hat", 1F, 1F);
					return true;
				}
				return false;
			}
		}
		if (!(riddenByEntity != null && riddenByEntity instanceof EntityPlayer && this.riddenByEntity != player)) {
			if (riddenByEntity == null)
				worldObj.playSoundAtEntity(this, ModPack.FOLDER + "ignition", 1F, 1F);
			if (!this.worldObj.isRemote)
				player.mountEntity(this);
			return true;
		}
		return false;
	}

	@Override
	public void updateRiderPosition() {
		if (riddenByEntity != null)
			riddenByEntity.setPosition(posX, posY + height + getYOffset() + 0.2, posZ);
	}

	@Override
	public void updateSpeedModel() {
		speed = Math.sqrt(motionX * motionX + motionZ * motionZ) * 25;
		if (speed < 0.01 && speed > -0.01)
			speed = 0;
		/* Has to be done here on a game tick as opposed to a render tick. */
		wheelAngle += (float) speed / 16;
		wheelAngle = wheelAngle >= 360 ? wheelAngle - 360 : wheelAngle;
		wheelAngle = wheelAngle < 0 ? wheelAngle + 360 : wheelAngle;
	}

	@Override
	protected void fall(float fallDist) {
		super.fall(fallDist / 2);
		int damage = (int) Math.ceil(fallDist - 3F);
		if (damage > 0)
			attackEntityFrom(DamageSource.fall, damage / 2);
	}
}