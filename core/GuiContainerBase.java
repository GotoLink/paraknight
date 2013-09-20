package assets.paraknight.core;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiContainerBase extends GuiContainer
{
	private String background;
	public GuiContainerBase(Container par1Container) 
	{
		super(par1Container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) 
	{
		mc.renderEngine.bindTexture(new ResourceLocation("paraknight","textures/gui/"+background));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) 
	{
		fontRenderer.drawString("Fuel", 10, 10, 0x005939);
		fontRenderer.drawString("Cargo", 100, 10, 0x005939);
		fontRenderer.drawString("Inventory", 8, 72, 0x005939);
	}
	
	@Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        this.inventorySlots.getSlot(0).onSlotChanged();
    }
	
	public GuiContainerBase setBackground(String name)
	{
		this.background = name;
		return this;
	}

}
