package assets.paraknight.core;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import assets.paraknight.lawnmower.EntityLawnMower;
import assets.paraknight.lawnmower.client.HUDLawnMower;
import assets.paraknight.lawnmower.client.RenderLawnMower;
import assets.paraknight.steambikes.EntityBlackWidow;
import assets.paraknight.steambikes.EntityMaroonMarauder;
import assets.paraknight.steambikes.client.RenderSteamBike;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	@Override
    public void registerHandlers() 
	{ 
		RenderingRegistry.registerEntityRenderingHandler(EntityMaroonMarauder.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlackWidow.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityLawnMower.class, new RenderLawnMower());


		MinecraftForge.EVENT_BUS.register(new HUDLawnMower(Minecraft.getMinecraft()));
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
	}
}
