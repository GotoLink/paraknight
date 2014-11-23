package core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import lawnmower.EntityLawnMower;
import lawnmower.client.HUDLawnMower;
import lawnmower.client.RenderLawnMower;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import steambikes.EntityBlackWidow;
import steambikes.EntityMaroonMarauder;
import steambikes.client.RenderSteamBike;
import tvmod.EntityTV;
import tvmod.RenderTV;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerHandlers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMaroonMarauder.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlackWidow.class, new RenderSteamBike());
		RenderingRegistry.registerEntityRenderingHandler(EntityLawnMower.class, new RenderLawnMower());
        RenderingRegistry.registerEntityRenderingHandler(EntityTV.class, new RenderTV());
		MinecraftForge.EVENT_BUS.register(new HUDLawnMower(FMLClientHandler.instance().getClient()));
	}

    @Override
    public MovingObjectPosition getMouseOver(){
        return FMLClientHandler.instance().getClient().objectMouseOver;
    }
}
