package core;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public final class SlotSelective extends Slot {
	public SlotSelective(IInventory var1, int var2, int var3, int var4) {
		super(var1, var2, var3, var4);
	}

	@Override
	public boolean isItemValid(ItemStack var1) {
		return this.inventory.isItemValidForSlot(slotNumber, var1);
	}
}
