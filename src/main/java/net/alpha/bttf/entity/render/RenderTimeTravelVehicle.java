package net.alpha.bttf.entity.render;

import net.alpha.bttf.entity.EntityTimeTravelVehicle;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RenderTimeTravelVehicle<T extends EntityTimeTravelVehicle> extends RenderVehicle<T> {

    protected List<Wheels> wheels = new ArrayList<>();
    protected List<Doors_Hover> doors = new ArrayList<>();
    protected List<Doors_Hover2> doors2 = new ArrayList<>();
 //   protected List<Wheels_Hovering> WHover = new ArrayList<>();

    protected RenderTimeTravelVehicle(RenderManager renderManager) {
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
        //   positions.forEach(positions -> positions.render(entity, partialTicks));
        //   hoverPositions.forEach(hoverPosition -> hoverPosition.render(entity, partialTicks));
      //  WHover.forEach(WHover -> WHover.render(entity, partialTicks));
        doors.forEach(doors -> doors.render(entity, partialTicks));
        doors2.forEach(doors2 -> doors2.render(entity, partialTicks));
        wheels.forEach(wheel -> wheel.render(entity, partialTicks));
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
