package tvmod;

import core.ModPack;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public final class ItemTVRemote extends Item {
	
	public ItemTVRemote() {
		super();
		setTextureName("tvmod:tvremote");
		setUnlocalizedName("tvRemote");
		setMaxStackSize(1);
        GameRegistry.registerItem(this, "tvRemote");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        if(world.isRemote) {
            MovingObjectPosition movingObjectPosition = ModPack.proxy.getMouseOver();
            if(movingObjectPosition!=null && movingObjectPosition.entityHit instanceof EntityTV) {
                EntityTV tv = ((EntityTV) movingObjectPosition.entityHit);
                if (!tv.isVideoPlaying)
                    new Thread(tv, "TVMod Processing").start();
                else if (player.isSneaking()) {
                    tv.shouldSkip = true;
                    if (tv.isVideoPaused)
                        tv.lastFrame = tv.noSignal;
                } else
                    tv.isVideoPaused = !tv.isVideoPaused;
            }
        }
        world.playSoundAtEntity(player, "random.click", 0.1F, 0.1F);
        return itemstack;
	}
}
