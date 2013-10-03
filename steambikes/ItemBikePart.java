package assets.paraknight.steambikes;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class ItemBikePart extends Item{

	public int type;
	public static String[] name=new String[]{"bikewheel","bikechassis","steamengine"};
	private Icon[] icon;
	public ItemBikePart(int id) {
		super(id);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabTransport);
	}
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        int j = MathHelper.clamp_int(par1, 0, name.length);
        return this.icon[j];
    }
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
    {
		this.icon=new Icon[name.length];
		for(int i=0;i<name.length;i++)
			this.icon[i] = par1IconRegister.registerIcon("paraknight:"+this.name[i]);
    }
	@Override
	@SideOnly(Side.CLIENT)
	 public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	 {
       for (int j = 0; j < name.length; ++j)
       {
           par3List.add(new ItemStack(par1, 1, j));
       }
	 }
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, name.length);
        return super.getUnlocalizedName() + name[i];
    }
}
