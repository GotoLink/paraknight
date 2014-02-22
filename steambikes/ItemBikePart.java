package steambikes;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBikePart extends Item {
	public static String[] name = new String[] { "bikewheel", "bikechassis", "steamengine" };
	private IIcon[] icon;

	public ItemBikePart() {
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabTransport);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		int j = MathHelper.clamp_int(par1, 0, name.length);
		return this.icon[j];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int j = 0; j < name.length; ++j) {
			par3List.add(new ItemStack(par1, 1, j));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, name.length);
		return super.getUnlocalizedName() + name[i];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.icon = new IIcon[name.length];
		for (int i = 0; i < name.length; i++)
			this.icon[i] = par1IconRegister.registerIcon("paraknight:" + this.name[i]);
	}
}
