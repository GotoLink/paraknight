package assets.paraknight.lawnmower.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import assets.paraknight.lawnmower.EntityLawnMower;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class HUDLawnMower extends Gui
{
    private Minecraft game;
    private int windowWidth;
    private int wave = 0;
    private final static ResourceLocation hud= new ResourceLocation("paraknight","textures/gui/lawnmowerhud.png");
    public HUDLawnMower(Minecraft var1)
    {
    	super();
        this.game = var1;  
    }
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void renderSkillHUD(RenderGameOverlayEvent event)
    {
        this.windowWidth = event.resolution.getScaledWidth();
        if(game.thePlayer.ridingEntity!=null && game.thePlayer.ridingEntity instanceof EntityLawnMower)
        {
	    	this.renderBG();
	        this.renderHealthBar((EntityLawnMower) game.thePlayer.ridingEntity);
	        this.renderFuelBar((EntityLawnMower) game.thePlayer.ridingEntity);
	        this.renderSpeedometer((EntityLawnMower) game.thePlayer.ridingEntity);
	
	        if (this.wave > 360)
	        {
	            this.wave = 0;
	        }
	
	        this.wave += 20;
        }
    }

    private void renderBG()
    {
        GL11.glEnable(GL11.GL_BLEND);
        this.game.renderEngine.bindTexture(hud);
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

        /*this.game.ingameGUI.*/drawTexturedModalRect((this.windowWidth - 102) / 2 + 19 + 1, 6, 0, 52, (int)(80-var1.getDamageTaken()) * 2, 6);
        this.game.fontRenderer.drawStringWithShadow("Health:", this.windowWidth / 2 - 68, 5, 16777215);
    }

    private void renderFuelBar(EntityLawnMower var1)
    {
        this.game.renderEngine.bindTexture(hud);
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
