package lawnmower.client;

import lawnmower.EntityLawnMower;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLawnMower extends ModelBase {
	public ModelRenderer blades;
	public ModelRenderer[] lawnMowerSides = new ModelRenderer[5];

	public ModelLawnMower() {
		this.lawnMowerSides[0] = new ModelRenderer(this, 0, 8);
		this.lawnMowerSides[1] = new ModelRenderer(this, 0, 0);
		this.lawnMowerSides[2] = new ModelRenderer(this, 0, 0);
		this.lawnMowerSides[3] = new ModelRenderer(this, 0, 0);
		this.lawnMowerSides[4] = new ModelRenderer(this, 0, 0);
		byte b0 = 24;
		byte b1 = 6;
		byte b2 = 20;
		byte b3 = 4;
		this.lawnMowerSides[0].addBox(-b0 / 2, -b2 / 2 + 2, -3.0F, b0, b2 - 4, 4, 0.0F);
		this.lawnMowerSides[0].setRotationPoint(0.0F, b3, 0.0F);
		this.lawnMowerSides[1].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		this.lawnMowerSides[1].setRotationPoint(-b0 / 2 + 1, b3, 0.0F);
		this.lawnMowerSides[2].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		this.lawnMowerSides[2].setRotationPoint(b0 / 2 - 1, b3, 0.0F);
		this.lawnMowerSides[3].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		this.lawnMowerSides[3].setRotationPoint(0.0F, b3, -b2 / 2 + 1);
		this.lawnMowerSides[4].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		this.lawnMowerSides[4].setRotationPoint(0.0F, b3, b2 / 2 - 1);
		this.lawnMowerSides[0].rotateAngleX = ((float) Math.PI / 2F);
		this.lawnMowerSides[1].rotateAngleY = ((float) Math.PI * 3F / 2F);
		this.lawnMowerSides[2].rotateAngleY = ((float) Math.PI / 2F);
		this.lawnMowerSides[3].rotateAngleY = (float) Math.PI;
		this.blades = new ModelRenderer(this, 0, 0);
		this.blades.addBox(-3.0F, -3.0F, -10.5F, 6, 6, 21, 0.0F);
		this.blades.setRotationPoint(-12.0F, 4.0F, 0.0F);
	}

	public void renderBlades(EntityLawnMower var1) {
		this.blades.rotateAngleZ = var1.bladesAngle;
		this.blades.render(0.0625F);
	}

	public void renderBody(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		for (int i = 0; i < 5; ++i) {
			this.lawnMowerSides[i].render(par7);
		}
	}
}
