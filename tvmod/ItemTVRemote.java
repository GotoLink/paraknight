package tvmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

public class ItemTVRemote extends Item {
	
	public ItemTVRemote(int i) {
		super(i);
		this.setTextureName("tvremote");
		setUnlocalizedName("tvRemote");
		setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
		if(movingobjectposition!=null && movingobjectposition.entityHit instanceof EntityTV) {
			EntityTV tv = ((EntityTV) movingobjectposition.entityHit);
			if(!tv.isVideoPlaying)
				new Thread(tv, "TVMod Processing").start();
			else if(Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				tv.shouldSkip = true;
				if(tv.isVideoPaused)
					tv.lastFrame = tv.noSignal;
			}
			else
				tv.isVideoPaused = !tv.isVideoPaused;
		}
		world.playSoundAtEntity(player, "random.click", 0.1F, 0.1F);
        return itemstack;
	}
}
