package net.alpha.bttf.vehicles.render;

import net.alpha.bttf.entity.render.*;
import net.alpha.bttf.vehicles.EntityDefaultDelorean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class RenderDefaultDelorean extends RenderDefaultVehicle<EntityDefaultDelorean>
{

    public RenderDefaultDelorean(RenderManager renderManager) {
        super(renderManager);

        wheels.add(new DefaultWheels(DefaultWheels.Side.LEFT, DefaultWheels.Position.FRONT, 5.0F, -1.7F, 7.9F, 0.3F));
        wheels.add(new DefaultWheels(DefaultWheels.Side.RIGHT, DefaultWheels.Position.FRONT, 5.0F, -1.7F, 7.9F,  0.3F));
        wheels.add(new DefaultWheels(DefaultWheels.Side.LEFT, DefaultWheels.Position.REAR, 5.0F, -1.7F, -7.9F, 0.3F));
        wheels.add(new DefaultWheels(DefaultWheels.Side.RIGHT, DefaultWheels.Position.REAR, 5.0F, -1.7F, -7.9F, 0.3F));

        doors.add(new DefaultDoors(DefaultDoors.Side.RIGHT, DefaultDoors.Position.RIGHT, 3.57F, 1.2F, 0.120F, 0.50F));
        doors2.add(new DefaultDoors2(DefaultDoors2.Side.LEFT, DefaultDoors2.Position.LEFT, 3.44F, 1.2F, 0.120F, 0.50F));
    }

    @Override
    public void doRender(EntityDefaultDelorean entity, double x, double y, double z, float currentYaw, float partialTicks)
    {
        RenderHelper.enableStandardItemLighting();

        float additionalYaw = entity.prevAdditionalYaw + (entity.additionalYaw - entity.prevAdditionalYaw) * partialTicks;

        EntityLivingBase entityLivingBase = (EntityLivingBase) entity.getControllingPassenger();
        if(entityLivingBase != null)
        {
            entityLivingBase.renderYawOffset = currentYaw - additionalYaw;
            entityLivingBase.prevRenderYawOffset = currentYaw - additionalYaw;
        }

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-currentYaw, 0, 1, 0);
            GlStateManager.rotate(additionalYaw, 0, 1, 0);
            GlStateManager.scale(2.9, 2.9, 2.9);
            GlStateManager.translate(0, -0.03125, 0.2);

            this.setupBreakAnimation(entity, partialTicks);

            double bodyLevelToGround = 0.095;
            double bodyOffset = 4.375 * 0.0625;

            //Render the body
            GlStateManager.pushMatrix();
            {
                GlStateManager.scale(1, 0.95, 1);
                GlStateManager.translate(0, bodyLevelToGround + bodyOffset, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(entity.body, ItemCameraTransforms.TransformType.NONE);
            }
            GlStateManager.popMatrix();

            float wheelAngle = entity.prevWheelAngle + (entity.wheelAngle - entity.prevWheelAngle) * partialTicks;

            GlStateManager.translate(0, bodyOffset + 0.03125, 0);
            super.doRender(entity, x, y, z, currentYaw, partialTicks);
        }
        GlStateManager.popMatrix();
    }


}
