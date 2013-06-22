package mods.paraknight.lawnmower;

import java.util.List;

import mods.paraknight.core.ItemSpawner;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@Deprecated
public class ItemLawnMower extends Item
{
	public ItemLawnMower(int var1)
    {
        super(var1);
    }

    @Deprecated
    private void givePlayerStuff(EntityPlayer var1)
    {
        var1.inventory.addItemStackToInventory(new ItemStack(Item.coal, 256));
        var1.inventory.addItemStackToInventory(new ItemStack(Item.ingotIron, 64));
        var1.inventory.addItemStackToInventory(new ItemStack(Item.boat, 4));
    }
	@Deprecated
    private void spawnFloatingPlatform(World var1, EntityPlayer var2)
    {
        for (int var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = MathHelper.floor_double(var2.posX - 25.0D); var4 < MathHelper.floor_double(var2.posX + 25.0D); ++var4)
            {
                for (int var5 = MathHelper.floor_double(var2.posZ - 25.0D); var5 < MathHelper.floor_double(var2.posZ + 25.0D); ++var5)
                {
                    if (var3 > 0 && var4 > MathHelper.floor_double(var2.posX - 5.0D) && var4 < MathHelper.floor_double(var2.posX + 5.0D) && var5 > MathHelper.floor_double(var2.posZ - 5.0D) && var5 < MathHelper.floor_double(var2.posZ + 5.0D))
                    {
                        var1.setBlock(var4, MathHelper.floor_double(var2.posY + 20.0D + (double)var3), var5, Block.waterStill.blockID);
                    }
                    else
                    {
                        var1.setBlock(var4, MathHelper.floor_double(var2.posY + 20.0D + (double)var3), var5, Block.grass.blockID);
                    }
                }
            }
        }

        var2.setPosition(var2.posX, var2.posY + 22.0D, var2.posZ);
    }
	@Deprecated
    private void growLoadsOfGrass(World var1, EntityPlayer var2)
    {
        for (int var3 = MathHelper.floor_double(var2.posX - 16.0D); var3 < MathHelper.floor_double(var2.posX + 16.0D); ++var3)
        {
            for (int var4 = MathHelper.floor_double(var2.posZ - 16.0D); var4 < MathHelper.floor_double(var2.posZ + 16.0D); ++var4)
            {
                int var5;

                for (var5 = 127; var1.isAirBlock(var3, var5 - 1, var4); --var5)
                {
                    ;
                }

                if (var1.getBlockId(var3, var5 - 1, var4) == Block.grass.blockID)
                {
                    var1.setBlockMetadataWithNotify(var3, var5, var4, Block.tallGrass.blockID, 1);
                }
            }
        }
    }
}
