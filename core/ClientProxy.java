package core;

import lawnmower.EntityLawnMower;
import lawnmower.client.HUDLawnMower;
import lawnmower.client.RenderLawnMower;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import steambikes.EntityBlackWidow;
import steambikes.EntityMaroonMarauder;
import steambikes.client.RenderSteamBike;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerHandlers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMaroonMarauder.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlackWidow.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityLawnMower.class, new RenderLawnMower());
		MinecraftForge.EVENT_BUS.register(new HUDLawnMower(Minecraft.getMinecraft()));
	}
}
