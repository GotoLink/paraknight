package tvmod;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderTV extends Render {

	private void renderTV(EntityTV entityTV, int width, int height) {
		float f = (float) (-width) / 2.0F;
		float f1 = (float) (-height) / 2.0F;
		float f2 = -0.5F;
		float f3 = 0.5F;
		for (int i1 = 0; i1 < width / 16; i1++)
			for (int j1 = 0; j1 < height / 16; j1++) {
				float f4 = f + (float) ((i1 + 1) * 16);
				float f5 = f + (float) (i1 * 16);
				float f6 = f1 + (float) ((j1 + 1) * 16);
				float f7 = f1 + (float) (j1 * 16);
				GL11.glColor3f(1, 1, 1);
				float f8 = (float) (width - i1 * 16) / 256F;
				float f9 = (float) (width - (i1 + 1) * 16) / 256F;
				float f10 = (float) (height - j1 * 16) / 256F;
				float f11 = (float) (height - (j1 + 1) * 16) / 256F;
				float f12 = 0.75F;
				float f13 = 0.8125F;
				float f14 = 0.0F;
				float f15 = 0.0625F;
				float f16 = 0.75F;
				float f17 = 0.8125F;
				float f18 = 0.001953125F;
				float f19 = 0.001953125F;
				float f20 = 0.7519531F;
				float f21 = 0.7519531F;
				float f22 = 0.0F;
				float f23 = 0.0625F;
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1F);
				tessellator.addVertexWithUV(f4, f7, f2, f9, f10);
				tessellator.addVertexWithUV(f5, f7, f2, f8, f10);
				tessellator.addVertexWithUV(f5, f6, f2, f8, f11);
				tessellator.addVertexWithUV(f4, f6, f2, f9, f11);
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator.addVertexWithUV(f4, f6, f3, f12, f14);
				tessellator.addVertexWithUV(f5, f6, f3, f13, f14);
				tessellator.addVertexWithUV(f5, f7, f3, f13, f15);
				tessellator.addVertexWithUV(f4, f7, f3, f12, f15);
				tessellator.setNormal(0.0F, -1F, 0.0F);
				tessellator.addVertexWithUV(f4, f6, f2, f16, f18);
				tessellator.addVertexWithUV(f5, f6, f2, f17, f18);
				tessellator.addVertexWithUV(f5, f6, f3, f17, f19);
				tessellator.addVertexWithUV(f4, f6, f3, f16, f19);
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.addVertexWithUV(f4, f7, f3, f16, f18);
				tessellator.addVertexWithUV(f5, f7, f3, f17, f18);
				tessellator.addVertexWithUV(f5, f7, f2, f17, f19);
				tessellator.addVertexWithUV(f4, f7, f2, f16, f19);
				tessellator.setNormal(-1F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(f4, f6, f3, f21, f22);
				tessellator.addVertexWithUV(f4, f7, f3, f21, f23);
				tessellator.addVertexWithUV(f4, f7, f2, f20, f23);
				tessellator.addVertexWithUV(f4, f6, f2, f20, f22);
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(f5, f6, f2, f21, f22);
				tessellator.addVertexWithUV(f5, f7, f2, f21, f23);
				tessellator.addVertexWithUV(f5, f7, f3, f20, f23);
				tessellator.addVertexWithUV(f5, f6, f3, f20, f22);
				tessellator.draw();
			}
	}

	@Override
	public void doRender(Entity entityTV, double d, double d1, double d2,
			float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		bindEntityTexture(entityTV);
		float f2 = mod_TVMod.isHDEnabled ? 0.015625F : 0.0625F;
		GL11.glScalef(f2, f2, f2);
		renderTV((EntityTV) entityTV, mod_TVMod.tvProps[2] * 16
				* (mod_TVMod.isHDEnabled ? 4 : 1), mod_TVMod.tvProps[3] * 16
				* (mod_TVMod.isHDEnabled ? 4 : 1));
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entityTV) {
		return renderManager.renderEngine.getDynamicTextureLocation(
				((EntityTV) entityTV).frameIntBuffer.get(0) + "",
				new DynamicTexture(((EntityTV) entityTV).lastFrame));
	}
}
