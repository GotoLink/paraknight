package steambikes;

import net.minecraft.world.World;

public class EntityMaroonMarauder extends EntitySteamBike {
	public EntityMaroonMarauder(World world) {
		super(world);
	}

	public EntityMaroonMarauder(World par2World, double d, double e, double f) {
		super(par2World, d, e, f);
	}

	@Override
	public String getEntityTexture() {
		return "textures/models/maroonmarauderskin.png";
	}

	@Override
	public double getFrictionFactor() {
		return 0.95D;
	}

	@Override
	public int getFuelDuration() {
		return 800;
	}

	@Override
	public int getItemDamage() {
		return 0;
	}
}