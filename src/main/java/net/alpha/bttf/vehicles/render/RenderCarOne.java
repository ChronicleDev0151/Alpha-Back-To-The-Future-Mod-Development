package net.alpha.bttf.vehicles.render;

import net.alpha.bttf.Reference;
import net.alpha.bttf.entity.render.*;
import net.alpha.bttf.vehicles.EntityCarOne;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderCarOne extends RenderDeloreanOne<EntityCarOne>
{

    public ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/travel/overlay.png");

    public RenderCarOne(RenderManager renderManager)
    {
        super(renderManager);
        // Testing ZexusPositions
     /*   positions.add(new ZexusPosition(1, 5.0F, 0.5F, 7.9F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false));
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, -7.9F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false));
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, 1.7F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false));
        positions.add(new ZexusPosition(1, 5.0F, 1.7F, -1.7F, 1.0F, 1.0F, ZexusPosition.Position.FRONT, ZexusPosition.Side.LEFT, null, null, false)); */

        wheels.add(new WheelsBTTF(WheelsBTTF.Side.LEFT, WheelsBTTF.Position.FRONT, 5.0F, -1.7F, 7.9F, 0.3F));
        wheels.add(new WheelsBTTF(WheelsBTTF.Side.RIGHT, WheelsBTTF.Position.FRONT, 5.0F, -1.7F, 7.9F,  0.3F));
        wheels.add(new WheelsBTTF(WheelsBTTF.Side.LEFT, WheelsBTTF.Position.REAR, 5.0F, -1.7F, -7.9F, 0.3F));
        wheels.add(new WheelsBTTF(WheelsBTTF.Side.RIGHT, WheelsBTTF.Position.REAR, 5.0F, -1.7F, -7.9F, 0.3F));


        doors.add(new Doors(Doors.Side.RIGHT, Doors.Position.RIGHT, 3.57F, 1.2F, 0.120F, 0.50F));
        doors2.add(new Doors2(Doors2.Side.LEFT, Doors2.Position.LEFT, 3.44F, 1.2F, 0.120F, 0.50F));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCarOne entity)
    {
        return null;
    }

    @Override
    public void doRender(EntityCarOne entity, double x, double y, double z, float currentYaw, float partialTicks)
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
