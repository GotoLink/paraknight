package core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
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

    @Override
    public void tryCheckForUpdate(){
        try {
            Class.forName("mods.mud.ModUpdateDetector").getDeclaredMethod("registerMod", ModContainer.class, String.class, String.class).invoke(null,
                    FMLCommonHandler.instance().findContainerFor(ModPack.instance),
                    "https://raw.github.com/GotoLink/paraknight/master/update.xml",
                    "https://raw.github.com/GotoLink/paraknight/master/changelog.md"
            );
        } catch (Throwable e) {
        }
    }
}
