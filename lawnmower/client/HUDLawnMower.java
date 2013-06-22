package mods.paraknight.lawnmower.client;

import mods.paraknight.lawnmower.EntityLawnMower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class HUDLawnMower extends Gui
{
    private Minecraft game;
    private int windowWidth;
    private int wave = 0;
    
    public HUDLawnMower(Minecraft var1)
    {
        this.game = var1;  
    }

    public void renderSkillHUD(EntityLawnMower var1)
    {
        this.windowWidth = (new ScaledResolution(this.game.gameSettings, this.game.displayWidth, this.game.displayHeight)).getScaledWidth();
    	this.renderBG();
        this.renderHealthBar(var1);
        this.renderFuelBar(var1);
        this.renderSpeedometer(var1);

        if (this.wave > 360)
        {
            this.wave = 0;
        }

        this.wave += 20;
    }

    private void renderBG()
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.game.renderEngine.getTexture("/mods/paraknight/textures/gui/lawnmowerhud.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        /*this.game.ingameGUI.*/drawTexturedModalRect((this.windowWidth - 155) / 2, 0, 0, 0, 155, 44);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderHealthBar(EntityLawnMower var1)
    {
        /*this.game.ingameGUI.*/drawTexturedModalRect((this.windowWidth - 102) / 2 + 19, 5, 0, 44, 102, 8);

        if ((80-var1.getDamageTaken()) * 2 <= 15)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)(Math.sin(Math.toRadians((double)this.wave)) / 2.0D + 0.5D));
        }

        /*this.game.ingameGUI.*/drawTexturedModalRect((this.windowWidth - 102) / 2 + 19 + 1, 6, 0, 52, (80-var1.getDamageTaken()) * 2, 6);
        this.game.fontRenderer.drawStringWithShadow("Health:", this.windowWidth / 2 - 68, 5, 16777215);
    }

    private void renderFuelBar(EntityLawnMower var1)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.game.renderEngine.getTexture("/mods/paraknight/textures/gui/lawnmowerhud.png"));
        /*this.game.ingameGUI.*/drawTexturedModalRect((this.windowWidth - 102) / 2 + 19, 15, 0, 44, 102, 8);

        if (var1.getFuelTime() <= 15)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)(Math.sin(Math.toRadians((double)this.wave)) / 2.0D + 0.5D));
        }

        /*this.game.ingameGUI.*/drawTexturedModalRect((this.windowWidth - 102) / 2 + 19 + 1, 16, 0, 58, var1.getFuelTime(), 6);
        this.game.fontRenderer.drawStringWithShadow("Fuel:", this.windowWidth / 2 - 68, 15, 16777215);
    }

    private void renderSpeedometer(EntityLawnMower var1)
    {
        this.game.fontRenderer.drawStringWithShadow("Speed: " + (float)Math.round((float)(var1.speed * 10.0D)) / 10.0F + " bps", this.windowWidth / 2 - 68 + 6, 29, 16777215);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
