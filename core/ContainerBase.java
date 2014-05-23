package core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {
	protected final IInventory inv;

	public ContainerBase(IInventory ridingEntity, InventoryPlayer playerInv) {
		this.inv = ridingEntity;
		addSlotToContainer(new SlotSelective(ridingEntity, 0, 30, 35));
		for (int row = 0; row < 2; row++)
			for (int col = 0; col < 2; col++)
				addSlotToContainer(new SlotSelective(ridingEntity, 1 + row + (col * 2), 102 + col * 18, 26 + row * 18));
		addPlayerSlots(playerInv);
	}

	public void addPlayerSlots(InventoryPlayer inv) {
		for (int var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(inv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		for (int var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(inv, var3, 8 + var3 * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inv.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (itemstack1.getItem() == Items.coal && i != 0)
				this.mergeItemStack(itemstack1, 0, 1, false);//Put coal into fuel slot
			else if (i < this.inv.getSizeInventory()) //From inventory to player inventory
			{
				this.mergeItemStack(itemstack1, this.inv.getSizeInventory(), this.inventorySlots.size(), true);
			} else
				this.mergeItemStack(itemstack1, 1, this.inv.getSizeInventory(), false);
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(player, itemstack1);
			} else {
				return null;
			}
		}
		return itemstack;
	}
}
