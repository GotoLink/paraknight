package assets.paraknight.lawnmower.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import assets.paraknight.lawnmower.EntityLawnMower;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class RenderLawnMower extends Render
{
    protected ModelLawnMower modelLawnMower = new ModelLawnMower();
    private static final ResourceLocation blades = new ResourceLocation("paraknight","textures/models/lawnmowerblades.png");
    private static final ResourceLocation body =  new ResourceLocation("paraknight","textures/models/lawnmowerbody.png");
    public RenderLawnMower()
    {
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        func_110777_b(var1);
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glRotatef(180.0F - var8, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelLawnMower.renderBody(var1, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        this.func_110776_a(blades);
        this.modelLawnMower.renderBlades((EntityLawnMower)var1);
        GL11.glPopMatrix();
    }

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return body;
	}
}
