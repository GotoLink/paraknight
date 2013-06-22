package mods.paraknight.lawnmower.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.paraknight.lawnmower.EntityLawnMower;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
@SideOnly(Side.CLIENT)
public class RenderLawnMower extends Render
{
    protected ModelLawnMower modelLawnMower = new ModelLawnMower();

    public RenderLawnMower()
    {
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glRotatef(180.0F - var8, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.loadTexture("/mods/paraknight/textures/models/lawnmowerbody.png");
        this.modelLawnMower.renderBody(var1, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        this.loadTexture("/mods/paraknight/textures/models/lawnmowerblades.png");
        this.modelLawnMower.renderBlades((EntityLawnMower)var1);
        GL11.glPopMatrix();
    }
}
