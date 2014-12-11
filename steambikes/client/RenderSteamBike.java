package steambikes.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import steambikes.EntitySteamBike;

@SideOnly(Side.CLIENT)
public final class RenderSteamBike extends Render {
	protected final ModelSteamBike modelSteamBike = new ModelSteamBike();

	public RenderSteamBike() {
		shadowSize = 0.5F;
	}

	@Override
	public void doRender(Entity steamBike, double posX, double posY, double posZ, float yaw, float partialTicks) {
		renderBike((EntitySteamBike) steamBike, posX, posY, posZ, yaw, partialTicks);
	}

	public void renderBike(EntitySteamBike steamBike, double posX, double posY, double posZ, float yaw, float partialTicks) {
		double xOffset = -Math.cos(Math.toRadians(yaw));
		double zOffset = -Math.sin(Math.toRadians(yaw));
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (posX + xOffset), (float) posY + steamBike.height, (float) (posZ + zOffset));
		GL11.glRotatef(90 - yaw, 0F, 1F, 0F);
		/*
		 * if(steamBike.riddenByEntity==null)
		 * GL11.glRotatef(steamBike.turnAngle*(-20), 0F, 0F, 1F);
		 */
		GL11.glScalef(-1F, -1F, 1F);
		bindEntityTexture(steamBike);
		//loadTexture(steamBike.getEntityTexture());
		modelSteamBike.render(steamBike);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation("paraknight", ((EntitySteamBike) entity).getEntityTexture());
	}
}