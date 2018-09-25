package net.alpha.bttf.entity.render;

import net.alpha.bttf.entity.EntityDeloreanOneVehicleFeatures;
import net.alpha.bttf.entity.EntityDeloreanTwoVehicleFeatures;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RenderDelorean2<T extends EntityDeloreanTwoVehicleFeatures> extends RenderVehicle<T> {

    protected List<WheelsBTTF2> wheels = new ArrayList<>();
    protected List<DoorsBTTF1> doors = new ArrayList<>();
    protected List<Doors2BTTF1> doors2 = new ArrayList<>();
    //   protected List<Wheels_Hovering> WHover = new ArrayList<>();

    protected RenderDelorean2(RenderManager renderManager) {
        super(renderManager);
    }


    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity)
    {
        return null;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        doors.forEach(doors -> doors.render(entity, partialTicks));
        doors2.forEach(doors2 -> doors2.render(entity, partialTicks));
        wheels.forEach(wheel -> wheel.render(entity, partialTicks));
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}