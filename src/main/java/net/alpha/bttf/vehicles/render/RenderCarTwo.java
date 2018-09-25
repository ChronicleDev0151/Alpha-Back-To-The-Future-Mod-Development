package net.alpha.bttf.vehicles.render;

import net.alpha.bttf.entity.render.*;
import net.alpha.bttf.vehicles.EntityCarTwo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GLSync;

public class RenderCarTwo extends RenderDelorean2<EntityCarTwo> {

    public RenderCarTwo(RenderManager renderManager) {
        super(renderManager);
        wheels.add(new WheelsBTTF2(WheelsBTTF2.Side.LEFT, WheelsBTTF2.Position.FRONT, 5.0F, -2F, 7.9F, 0.3F));
        wheels.add(new WheelsBTTF2(WheelsBTTF2.Side.RIGHT, WheelsBTTF2.Position.FRONT, 5.0F, -2F, 7.9F,  0.3F));
        wheels.add(new WheelsBTTF2(WheelsBTTF2.Side.LEFT, WheelsBTTF2.Position.REAR, 5.0F, -2F, -7.9F, 0.3F));
        wheels.add(new WheelsBTTF2(WheelsBTTF2.Side.RIGHT, WheelsBTTF2.Position.REAR, 5.0F, -2F, -7.9F, 0.3F));

    }

    @Override
    public void doRender(EntityCarTwo entity, double x, double y, double z, float currentYaw, float partialTicks)
    {
        RenderHelper.enableStandardItemLighting();

        float additionalYaw = entity.prevAdditionalYaw + (entity.additionalYaw - entity.prevAdditionalYaw) * partialTicks;

        EntityLivingBase entityLivingBase = (EntityLivingBase) entity.getControllingPassenger();
        if(entityLivingBase != null)
        {
            entityLivingBase.renderYawOffset = currentYaw;
            entityLivingBase.prevRenderYawOffset = currentYaw;
        }

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-currentYaw, 0, 1, 0);
            GlStateManager.translate(0, 11 * 0.0625, -0.5);
            GlStateManager.scale(2.9, 2.9, 2.9);

            this.setupBreakAnimation(entity, partialTicks);

            double bodyLevelToGround = 0.095;

            float bodyPitch = entity.prevBodyRotationX + (entity.bodyRotationX - entity.prevBodyRotationX) * partialTicks;
            float bodyRoll = entity.prevBodyRotationZ + (entity.bodyRotationZ - entity.prevBodyRotationZ) * partialTicks;

            //Render the body
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0, bodyLevelToGround, 0);
                GlStateManager.rotate(-bodyRoll, 0, 0, 1);
                GlStateManager.rotate(-bodyPitch, 1, 0, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(entity.body, ItemCameraTransforms.TransformType.NONE);

                super.doRender(entity, x, y, z, currentYaw, partialTicks);
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
}