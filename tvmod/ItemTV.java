package tvmod;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTV extends Item {
	
	public static ArrayList<String> videoPathes = new ArrayList<String>();

	public ItemTV(int i) {
		super(i);
		setUnlocalizedName("tv");
		setMaxStackSize(1);
		loadVideoPathes();
	}
	
	private void loadVideoPathes() {
		File files[] = null, dir = new File(Minecraft.getMinecraft().mcDataDir+"/resources/mod/TV/");
		if (dir.exists() || dir.mkdirs())
			files = dir.listFiles();
		if(files!=null)
			for (int i = 0; i < files.length; i++)
				videoPathes.add(files[i].toString());
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int blockSide, float par8, float par9, float par10) {
		if (blockSide == 0 || blockSide == 1)
			return false;
		byte orientation = 0;
		if (blockSide == 4)
			orientation = 1;
		if (blockSide == 3)
			orientation = 2;
		if (blockSide == 5)
			orientation = 3;
		EntityTV entityTV = new EntityTV(world, x, y, z, orientation);
		if (entityTV.canStay()) {
			if (!world.isRemote)
				world.spawnEntityInWorld(entityTV);
			itemstack.stackSize--;
		}
		return true;
	}
}
