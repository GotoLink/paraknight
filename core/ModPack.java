package assets.paraknight.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import assets.paraknight.lawnmower.EntityLawnMower;
import assets.paraknight.steambikes.EntityBlackWidow;
import assets.paraknight.steambikes.EntityMaroonMarauder;
import assets.paraknight.steambikes.ItemBikePart;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "paraknight", name = "Paraknight Mod Pack", version = "1.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModPack {
	@Instance("paraknight")
	public static ModPack instance;
	
	private static int ids[];
	public static Item ride,wrench,bikePart;

	@SidedProxy(clientSide = "assets.paraknight.core.ClientProxy", serverSide = "assets.paraknight.core.CommonProxy")
	public static CommonProxy proxy;

	private static boolean enableBikes,enableLawnMower;

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event)
	{
		ids = getIDs();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		if(enableBikes || enableLawnMower){
			ride = new ItemSpawner(ids[1]).setUnlocalizedName("paraknight:").func_111206_d("paraknight:");
			wrench = new Item(ids[2]).setMaxStackSize(1).setMaxDamage(100).setUnlocalizedName("paraknight:wrench").setCreativeTab(CreativeTabs.tabTransport).func_111206_d("paraknight:wrench");	
			LanguageRegistry.instance().addName(wrench, "Wrench");
			GameRegistry.addShapelessRecipe(new ItemStack(wrench), new Object[] {
				new ItemStack(Item.ingotIron) , new ItemStack(Item.coal)});
		}
		if(enableBikes){
			bikePart = new ItemBikePart(ids[0]).setUnlocalizedName("steambikes:").func_111206_d("paraknight:");
			LanguageRegistry.instance().addName(new ItemStack(bikePart,1,0), "Bike Wheel");
			GameRegistry.addRecipe(new ItemStack(bikePart,1,0), new Object[] {
				" I ", "IGI", " I ", 'I', Item.ingotIron, 'G', Item.ingotGold });
					
			LanguageRegistry.instance().addName(new ItemStack(bikePart,1,1), "Bike Chassis");
			GameRegistry.addRecipe(new ItemStack(bikePart,1,1), new Object[] {
				"CSR", "III", "WGW",
				'C', Block.chest, 'S', Item.saddle, 'R', Item.redstone, 'I', Item.ingotIron, 'W', new ItemStack(bikePart,1,0),
				'G', Item.ingotGold });
					
			LanguageRegistry.instance().addName(new ItemStack(bikePart,1,2), "Steam Engine");
			GameRegistry.addRecipe(new ItemStack(bikePart,1,2), new Object[] {
				"W", "F", "R", 'W', Item.bucketWater, 'F', Block.furnaceIdle, 'R', Item.redstone });
			EntityRegistry.registerModEntity(EntityMaroonMarauder.class, "MaroonMarauder", 1,this, 40, 1, true);
			LanguageRegistry.instance().addName(new ItemStack(ride,1,0), "Maroon Marauder");
			LanguageRegistry.instance().addName(new ItemStack(ride,1,1), "Black Widow");
			for(int i=0;i<2;i++)
			{
				GameRegistry.addRecipe(new ItemStack(ride,1,i), new Object[] {
					"W", "C", "E", 
					'W', new ItemStack(Block.cloth, 1, 14+i), 'C', new ItemStack(bikePart,1,1), 'E', new ItemStack(bikePart,1,2) });
			}
			EntityRegistry.registerModEntity(EntityBlackWidow.class, "BlackWidow", 2,this, 40, 1, true);
		}
		if(enableLawnMower){
			LanguageRegistry.instance().addName(new ItemStack(ride, 1, 2), "Lawn Mower");
	        GameRegistry.addRecipe(new ItemStack(ride, 1, 2), new Object[] {" B ", "III", 'B', Item.boat, 'I', Item.ingotIron});
	        EntityRegistry.registerModEntity(EntityLawnMower.class, "LawnMower", 3, this, 40, 1, true);
	        LanguageRegistry.instance().addName(new ItemStack(ride, 1, 3), "Lawn Mower Key");
	        GameRegistry.addShapelessRecipe(new ItemStack(ride, 1, 3), new Object[] {Item.ingotIron, Item.seeds});
		}
        
		proxy.registerHandlers();
		NetworkRegistry.instance().registerGuiHandler(this,proxy);
	}
	private static int[] getIDs() {
		int ids[]={31470,31471,31472};
		Properties props = new Properties();		
		File propFile = new File(getMinecraftBaseDir()+"/paraknight.properties");
		if (propFile.exists())
		{// Read properties file
			try {
				props.load(new FileInputStream(propFile));
				ids[0]=Integer.parseInt(props.getProperty("BikePartID", "31470"));
				ids[1]=Integer.parseInt(props.getProperty("RideID", "31471"));
				ids[2]=Integer.parseInt(props.getProperty("WrenchID", "31472"));
				enableBikes=Boolean.parseBoolean(props.getProperty("EnableSteamBikes", "true"));
				enableLawnMower=Boolean.parseBoolean(props.getProperty("EnableLawnMawer", "true"));
			} catch (FileNotFoundException e){FMLLog.getLogger().log(Level.WARNING, "Paraknight Mod Pack couldn't load properties file.");} 
			catch (IOException e) {FMLLog.getLogger().log(Level.WARNING, "Paraknight Mod Pack couldn't load properties file.");}
			catch(NumberFormatException e){FMLLog.getLogger().log(Level.WARNING, "Paraknight Mod Pack couldn't read an item id from properties file.");}
		}
		else
		{// Write properties file.
			props.setProperty("BikePartID", "31470");
			props.setProperty("RideID", "31471");
			props.setProperty("WrenchID", "31472");
			props.setProperty("EnableSteamBikes", "true");
			props.setProperty("EnableLawnMower", "true");
			try 
			{	
				props.store(new FileOutputStream(getMinecraftBaseDir()+"/paraknight.properties"), null);
			} catch (IOException e) {FMLLog.getLogger().log(Level.WARNING, "Paraknight Mod Pack couldn't save properties file.");}
		}
		return ids;
	}

	private static File getMinecraftBaseDir() {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
	      {
	          return FMLClientHandler.instance().getClient().getMinecraft().mcDataDir;
	      }         
	      return FMLCommonHandler.instance().getMinecraftServerInstance().getFile("");
	}
}
