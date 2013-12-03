package tvmod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class mod_TVMod {
	
	public static boolean isHDEnabled = true, isShuffleEnabled = false;
	public static final int tvProps[] = getProps();
	public static ItemTV tv;
	public static ItemTVRemote tvRemote;

	public void load() {
		tv = new ItemTV(tvProps[0]);
		tvRemote = new ItemTVRemote(tvProps[1]);
		LanguageRegistry.addName(tvRemote, "TV Remote");
		GameRegistry.addShapelessRecipe(new ItemStack(tvRemote, 1), new Object[] {
			new ItemStack(Item.redstone) , new ItemStack(Item.ingotIron)});
		LanguageRegistry.addName(tv, "TV");
		GameRegistry.addRecipe(new ItemStack(tv, 1), new Object[] {
			"G", "P", "R", Character.valueOf('G'), Block.glass, Character.valueOf('P'), Item.painting, Character.valueOf('R'), Item.redstone });
		EntityRegistry.registerModEntity(EntityTV.class, "TV", 1, this, 50, 3, false);
	}
	
	private static int[] getProps() {
		int props[]={31460,31461,4,2,40};
		FileInputStream propFile = null;
		Properties tvProps = new Properties();
		try {
			propFile = new FileInputStream(Minecraft.getMinecraft().mcDataDir.toString()+"/TV.properties");
			tvProps.load(propFile);
			props[0]=Integer.parseInt(tvProps.getProperty("TVID", "31460"));
			props[1]=Integer.parseInt(tvProps.getProperty("TVRemoteID", "31461"));
			props[2]=Integer.parseInt(tvProps.getProperty("TVWidth", "4"));
			props[3]=Integer.parseInt(tvProps.getProperty("TVHeight", "2"));
			props[4]=Integer.parseInt(tvProps.getProperty("TVSoundRange", "40"));
			isHDEnabled=Boolean.parseBoolean(tvProps.getProperty("HDEnabled", "true"));
			isShuffleEnabled=Boolean.parseBoolean(tvProps.getProperty("ShuffleEnabled", "false"));
		} catch (Exception e){
		} finally {
			if(propFile!=null)
				try {propFile.close();} catch (IOException e){}
		}
		return props;
	}

	@Override
	public void addRenderer(Map map) {
		map.put(EntityTV.class, new RenderTV());
	}
}