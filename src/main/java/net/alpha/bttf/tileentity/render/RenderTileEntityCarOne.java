package net.alpha.bttf.tileentity.render;

import net.alpha.bttf.tileentity.TileEntityCarOne;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderTileEntityCarOne extends TileEntitySpecialRenderer<TileEntityCarOne>
{

    @Override
    public void render(TileEntityCarOne te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {
            GlStateManager.scale(5, 5, 5);
        }
        GlStateManager.popMatrix();

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}
