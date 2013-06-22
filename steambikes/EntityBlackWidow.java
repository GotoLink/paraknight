package mods.paraknight.steambikes;

import net.minecraft.world.World;

public class EntityBlackWidow extends EntitySteamBike{

	public EntityBlackWidow(World world) {
		super(world);
	}

	public EntityBlackWidow(World par2World, double d, double e, double f) {
		super(par2World, d, e, f);
	}

	@Override
	public String getEntityTexture() {
        return "/mods/paraknight/textures/models/blackwidowskin.png";
	}

	@Override
	public double getFrictionFactor() {
		return 0.97D;
	}

	@Override
	public int getFuelDuration() {
		return 400;
	}

	@Override
	public int getItemDamage() {
		return 1;
	}

}