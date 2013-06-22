package mods.paraknight.core;

import mods.paraknight.lawnmower.EntityLawnMower;
import mods.paraknight.lawnmower.client.EntitySlimeFX;
import mods.paraknight.lawnmower.client.RenderLawnMower;
import mods.paraknight.lawnmower.client.TickHandler;
import mods.paraknight.steambikes.EntityBlackWidow;
import mods.paraknight.steambikes.EntityMaroonMarauder;
import mods.paraknight.steambikes.client.RenderSteamBike;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy{
	@Override
    public void registerHandlers() 
	{ 
		RenderingRegistry.registerEntityRenderingHandler(EntityMaroonMarauder.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlackWidow.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityLawnMower.class, new RenderLawnMower());

		//TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);TODO: Fix HUD
	}
	@Deprecated
	@Override
	public void addSlimeEffect(EntitySlimeFX entitySlimeFX) {
		//FMLClientHandler.instance().getClient().effectRenderer.addEffect(entitySlimeFX);
	}
}
