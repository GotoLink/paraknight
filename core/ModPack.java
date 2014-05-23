package core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import lawnmower.EntityLawnMower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import steambikes.EntityBlackWidow;
import steambikes.EntityMaroonMarauder;
import steambikes.ItemBikePart;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "paraknight", name = "Paraknight Mod Pack", useMetadata = true)
public class ModPack {
    public final static String FOLDER = "paraknight:";
    @Instance("paraknight")
	public static ModPack instance;
	@SidedProxy(clientSide = "core.ClientProxy", serverSide = "core.CommonProxy")
	public static CommonProxy proxy;
	public static Item ride, wrench, bikePart;

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerHandlers();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
	}

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        boolean enableBikes = config.get("General", "EnableSteamBikes", true).getBoolean(true);
        boolean enableLawnMower = config.get("General", "EnableLawnMawer", true).getBoolean(true);
        if(config.hasChanged())
            config.save();
        if (enableBikes || enableLawnMower) {
            ride = new ItemSpawner().setUnlocalizedName("paraknight:").setTextureName("paraknight:");
            wrench = new Item().setMaxStackSize(1).setMaxDamage(100).setUnlocalizedName("paraknight:wrench").setCreativeTab(CreativeTabs.tabTransport).setTextureName("paraknight:wrench");
            GameRegistry.registerItem(ride, "RideSpawner");
            GameRegistry.registerItem(wrench, "Wrench");
            GameRegistry.addShapelessRecipe(new ItemStack(wrench), new ItemStack(Items.iron_ingot), new ItemStack(Items.coal));
        }
        if (enableBikes) {
            bikePart = new ItemBikePart().setUnlocalizedName("steambikes:").setTextureName("paraknight:");
            GameRegistry.registerItem(bikePart, "BikePart");
            GameRegistry.addRecipe(new ItemStack(bikePart, 1, 0), " I ", "IGI", " I ", 'I', Items.iron_ingot, 'G', Items.gold_ingot);
            GameRegistry.addRecipe(new ItemStack(bikePart, 1, 1), "CSR", "III", "WGW", 'C', Blocks.chest, 'S', Items.saddle, 'R', Items.redstone, 'I', Items.iron_ingot, 'W',
                    new ItemStack(bikePart, 1, 0), 'G', Items.gold_ingot);
            GameRegistry.addRecipe(new ItemStack(bikePart, 1, 2), "W", "F", "R", 'W', Items.water_bucket, 'F', Blocks.furnace, 'R', Items.redstone);
            EntityRegistry.registerModEntity(EntityMaroonMarauder.class, "MaroonMarauder", 1, this, 40, 1, true);
            for (int i = 0; i < 2; i++) {
                GameRegistry.addRecipe(new ItemStack(ride, 1, i), "W", "C", "E", 'W', new ItemStack(Blocks.wool, 1, 14 + i), 'C', new ItemStack(bikePart, 1, 1), 'E',
                        new ItemStack(bikePart, 1, 2));
            }
            EntityRegistry.registerModEntity(EntityBlackWidow.class, "BlackWidow", 2, this, 40, 1, true);
        }
        if (enableLawnMower) {
            GameRegistry.addRecipe(new ItemStack(ride, 1, 2), " B ", "III", 'B', Items.boat, 'I', Items.iron_ingot);
            EntityRegistry.registerModEntity(EntityLawnMower.class, "LawnMower", 3, this, 40, 1, true);
            GameRegistry.addShapelessRecipe(new ItemStack(ride, 1, 3), Items.iron_ingot, Items.wheat_seeds);
        }
        if(event.getSourceFile().getName().endsWith(".jar") && event.getSide().isClient()){
            try {
                Class.forName("mods.mud.ModUpdateDetector").getDeclaredMethod("registerMod", ModContainer.class, String.class, String.class).invoke(null,
                        FMLCommonHandler.instance().findContainerFor(this),
                        "https://raw.github.com/GotoLink/paraknight/master/update.xml",
                        "https://raw.github.com/GotoLink/paraknight/master/changelog.md"
                );
            } catch (Throwable e) {
            }
        }
	}
}
