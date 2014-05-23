package lawnmower.client;

import lawnmower.EntityLawnMower;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLawnMower extends Render {
	private static final ResourceLocation blades = new ResourceLocation("paraknight", "textures/models/lawnmowerblades.png");
	private static final ResourceLocation body = new ResourceLocation("paraknight", "textures/models/lawnmowerbody.png");
	protected final ModelLawnMower modelLawnMower = new ModelLawnMower();

	public RenderLawnMower() {
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		GL11.glPushMatrix();
		bindEntityTexture(var1);
		GL11.glTranslatef((float) var2, (float) var4, (float) var6);
		GL11.glRotatef(180.0F - var8, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelLawnMower.renderBody(var1, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		this.bindTexture(blades);
		this.modelLawnMower.renderBlades((EntityLawnMower) var1);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return body;
	}
}
