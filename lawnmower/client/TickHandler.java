package mods.paraknight.lawnmower.client;

import java.util.EnumSet;

import mods.paraknight.lawnmower.EntityLawnMower;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler{

	private Minecraft mc = FMLClientHandler.instance().getClient();
    private HUDLawnMower lawnMowerHUD = new HUDLawnMower(mc);
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (((Entity)tickData[0]).ridingEntity != null && ((Entity)tickData[0]).ridingEntity instanceof EntityLawnMower && mc.currentScreen == null && !mc.isGamePaused)
        {
            this.lawnMowerHUD.renderSkillHUD((EntityLawnMower)((Entity)tickData[0]).ridingEntity);
        }
		/*if(mc.currentScreen == null && !mc.isGamePaused && mc.thePlayer!=null && mc.thePlayer.ridingEntity!=null && mc.thePlayer.ridingEntity instanceof EntityLawnMower)
		{
			this.lawnMowerHUD.renderSkillHUD((EntityLawnMower) mc.thePlayer.ridingEntity);
		}*/
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER/*TickType.RENDER*/);
	}

	@Override
	public String getLabel() {
		return "LawnMower player tick";
	}

}
