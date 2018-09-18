package net.alpha.bttf.vehicles.render;

import net.alpha.bttf.entity.render.*;
import net.alpha.bttf.vehicles.EntityCarTwo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class RenderCarTwo extends RenderTimeTravelConvertableVehicle<EntityCarTwo> {

    public RenderCarTwo(RenderManager renderManager) {
        super(renderManager);
     /* DEBUGGING
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, 7.9F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false));
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, -7.9F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false));
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, 1.7F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false));
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, -1.7F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false)); */

     doors.add(new HoverDoors(HoverDoors.Side.RIGHT, HoverDoors.Position.RIGHT, 3.57F, 1.71F, 0.120F, 0.50F));
        doors2.add(new HoverDoors2(HoverDoors2.Side.LEFT, HoverDoors2.Position.LEFT, 3.44F, 1.71F, 0.120F, 0.50F));

    }

    @Override
    public void doRender(EntityCarTwo entity, double x, double y, double z, float currentYaw, float partialTicks) {
        RenderHelper.enableStandardItemLighting();

        float additionalYaw = entity.prevAdditionalYaw + (entity.additionalYaw - entity.prevAdditionalYaw) * partialTicks;

        EntityLivingBase entityLivingBase = (EntityLivingBase) entity.getControllingPassenger();
        if (entityLivingBase != null) {
            entityLivingBase.renderYawOffset = currentYaw - additionalYaw;
            entityLivingBase.prevRenderYawOffset = currentYaw - additionalYaw;
        }

      /*  GlStateManager.pushMatrix();
        {
            if (vehicle.hover == true) {
                hoverPositions.add(new ZexusHoverPosition(1, 5.0F, 1.7F, 7.9F, 1.0F, 1.0F, ZexusHoverPosition.Position.FRONT, ZexusHoverPosition.Side.LEFT, null, null, true));
            }
        } */
       // GlStateManager.popMatrix();

     //   float bodyPitch = entity.prevBodyRotationX + (entity.bodyRotationX - entity.prevBodyRotationX) * partialTicks;
      //  float bodyRoll = entity.prevBodyRotationZ + (entity.bodyRotationZ - entity.prevBodyRotationZ) * partialTicks;

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-currentYaw, 0, 1, 0);
            GlStateManager.rotate(additionalYaw, 0, 1, 0);
            GlStateManager.scale(2.9, 2.9, 2.9);
            GlStateManager.translate(0, -0.03125, 0.2);
            this.setupBreakAnimation(entity, partialTicks);

            double bodyLevelToGround = 0.15;
            double bodyOffset = 4.375 * 0.0625;

            //Render the body
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(90F, 0, 1, 0);
                GlStateManager.translate(0, bodyLevelToGround, 0);
     //           GlStateManager.rotate(bodyRoll, 1, 0, 0);
     //           GlStateManager.rotate(-bodyPitch, 0, 0, 1);
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