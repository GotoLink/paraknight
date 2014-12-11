package core;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiContainerBase extends GuiContainer {
	private ResourceLocation background;

	public GuiContainerBase(Container par1Container) {
		super(par1Container);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		this.inventorySlots.getSlot(0).onSlotChanged();
	}

	public GuiContainerBase setBackground(String name) {
		this.background = new ResourceLocation("paraknight", "textures/gui/" + name);
		return this;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Fuel", 10, 10, 0x005939);
        fontRendererObj.drawString("Cargo", 100, 10, 0x005939);
        fontRendererObj.drawString("Inventory", 8, 72, 0x005939);
	}
}
