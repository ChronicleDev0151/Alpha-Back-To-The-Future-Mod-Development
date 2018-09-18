package net.alpha.bttf.tileentity.render;

import net.alpha.bttf.tileentity.TileEntityCarOne;
import net.alpha.bttf.tileentity.TileEntityCarTwo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderTileEntityCarTwo extends TileEntitySpecialRenderer<TileEntityCarTwo>
{
    @Override
    public void render(TileEntityCarTwo te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {
            GlStateManager.scale(1, 1, 1);
        }
        GlStateManager.popMatrix();

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}
