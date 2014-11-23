package tvmod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class ItemTV extends Item {

	public ItemTV() {
		super();
        setTextureName("tvmod:tv");
		setUnlocalizedName("tv");
		setMaxStackSize(1);
        GameRegistry.registerItem(this, "tv");
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
