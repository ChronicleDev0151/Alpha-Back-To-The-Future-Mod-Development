package net.alpha.bttf.entity.render;


import net.alpha.bttf.entity.EntityTimeTravelVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;

public class Doors2
{
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float width;
    private float scale;
    private Side side;
    private Position position;
    public static boolean open = false;

    public Doors2(Side side, Position position, float width, float scale, float offsetX, float offsetY, float offsetZ)
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.width = width;
        this.scale = scale;
        this.side = side;
        this.position = position;
    }

    public Doors2(Side side, Position position, float offsetX, float offsetZ)
    {
        this(side, position, 2.0F, 1.0F, offsetX, 0F, offsetZ);
    }

    public Doors2(Side side, Position position, float offsetX, float offsetZ, float scale)
    {
        this(side, position, 2.0F, scale, offsetX, 0F, offsetZ);
    }

    public Doors2(Side side, Position position, float offsetX, float offsetY, float offsetZ, float scale)
    {
        this(side, position, 2.0F, scale, offsetX, offsetY, offsetZ);
    }

    public void render(EntityTimeTravelVehicle vehicle, float partialTicks) {

        GlStateManager.rotate(1.0F, 1.0F, 1.0F, 5.0F);
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate((offsetX / 16F) * side.offset, offsetY / 16F, offsetZ / 16F);
            GlStateManager.pushMatrix();
            {
                if(position == Position.RIGHT) {
                        float doorAngle = vehicle.doorAngle + (vehicle.doorAngle - vehicle.prevDoorAngle) * partialTicks;
                        GlStateManager.rotate(doorAngle, 0, 1, 0);
                }


                GlStateManager.translate((((width * scale) / 2) / 16F) * side.offset, 0, 0);
                GlStateManager.scale(scale, scale, scale);
                if(side == Side.RIGHT) {
                        GlStateManager.rotate(0F, 0, 1, 0);
                }
                if(open == true)
                {
                    GlStateManager.rotate(-getWheelRotation(vehicle, partialTicks), 0, 0, 0);
                }
                Minecraft.getMinecraft().getRenderItem().renderItem(vehicle.door_2, ItemCameraTransforms.TransformType.NONE);
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    private float getWheelRotation(EntityTimeTravelVehicle vehicle, float partialTicks)
    {
        if(position == Doors2.Position.LEFT)
        {
            return vehicle.prevDoorAngle + (vehicle.doorAngle - vehicle.prevDoorAngle) * partialTicks;
        }
        return vehicle.prevDoorAngle + (vehicle.wheelAngle - vehicle.prevWheelAngle) * partialTicks;
    }

    public enum Side
    {
        LEFT(-1), RIGHT(1), NONE(0);

        int offset;

        Side(int offset)
        {
            this.offset = offset;
        }
    }

    public enum Position
    {
        LEFT, RIGHT;
    }
}
