package mods.paraknight.core;

import mods.paraknight.lawnmower.EntityLawnMower;
import mods.paraknight.lawnmower.client.EntitySlimeFX;
import mods.paraknight.steambikes.EntitySteamBike;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public static final int GUI=160;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(player.ridingEntity!=null && ID==GUI){
			if( player.ridingEntity instanceof EntitySteamBike)
				return new ContainerBase((IInventory) player.ridingEntity,player.inventory);
			else if( player.ridingEntity instanceof EntityLawnMower)
				return new ContainerBase((IInventory) player.ridingEntity,player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(player.ridingEntity!=null && ID==GUI){
			if( player.ridingEntity instanceof EntitySteamBike)
				return new GuiContainerBase(new ContainerBase((IInventory) player.ridingEntity,player.inventory)).setBackground("steambikegui.png");
			else if( player.ridingEntity instanceof EntityLawnMower)
				return new GuiContainerBase(new ContainerBase((IInventory) player.ridingEntity,player.inventory)).setBackground("lawnmowergui.png");
		}
		return null;
	}

	public void registerHandlers() {}
	@Deprecated
	public void addSlimeEffect(EntitySlimeFX entitySlimeFX) {}

}
