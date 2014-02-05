package core;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiContainerBase extends GuiContainer {
	private String background;

	public GuiContainerBase(Container par1Container) {
		super(par1Container);
	}

	@Override
	public void func_146281_b() {
		super.func_146281_b();
		this.field_147002_h.getSlot(0).onSlotChanged();
	}

	public GuiContainerBase setBackground(String name) {
		this.background = name;
		return this;
	}

	@Override
	protected void func_146976_a(float f, int i, int j) {
        field_146297_k.renderEngine.bindTexture(new ResourceLocation("paraknight", "textures/gui/" + background));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect((this.field_146294_l - this.field_146999_f) / 2, (this.field_146295_m - this.field_147000_g) / 2, 0, 0, field_146999_f, field_147000_g);
	}

	@Override
	protected void func_146979_b(int par1, int par2) {
        field_146289_q.drawString("Fuel", 10, 10, 0x005939);
        field_146289_q.drawString("Cargo", 100, 10, 0x005939);
        field_146289_q.drawString("Inventory", 8, 72, 0x005939);
	}
}
