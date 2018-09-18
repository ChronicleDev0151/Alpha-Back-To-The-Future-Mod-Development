package net.alpha.bttf.entity.render;

import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class RenderVehicle<T extends EntityVehicle> extends Render<T> {

    protected RenderVehicle(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {}

    public void setupBreakAnimation(EntityVehicle vehicle, float partialTicks) {
        float timeSinceHit = (float) vehicle.getTimeSinceHit() - partialTicks;
        float damageTaken = vehicle.getDamageTaken() - partialTicks;

        if (damageTaken < 0.0F) {
            damageTaken = 0.0F;
        }

        if (timeSinceHit > 0.0F) {
            GlStateManager.rotate(MathHelper.sin(timeSinceHit) * timeSinceHit * damageTaken / 10.0F, 0, 0, 1);
        }
    }

}