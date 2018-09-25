package net.alpha.bttf.entity.render;

import net.alpha.bttf.entity.EntityDefaultVehicle;
import net.minecraft.client.renderer.entity.RenderManager;

import java.util.ArrayList;
import java.util.List;

public class RenderDefaultVehicle<T extends EntityDefaultVehicle> extends RenderVehicle<T>
{

    protected List<DefaultWheels> wheels = new ArrayList<>();
    protected List<DefaultDoors> doors = new ArrayList<>();
    protected List<DefaultDoors2> doors2 = new ArrayList<>();

    protected RenderDefaultVehicle(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

        doors.forEach(doors -> doors.render(entity, partialTicks));
        doors2.forEach(doors2 -> doors2.render(entity, partialTicks));
        wheels.forEach(wheel -> wheel.render(entity, partialTicks));
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
