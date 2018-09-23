package net.alpha.bttf.entity;

import net.alpha.bttf.timetravel.TimeTravelTypes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class EntityTimeTravelVehicle extends EntityVehicle {

    private static final DataParameter<Integer> TIME_TRAVEL_TYPE = EntityDataManager.createKey(EntityTimeTravelVehicle.class, DataSerializers.VARINT);

    private TimeTravelTypes timeTravelTypes;

    @Override
    public void entityInit() {
        this.dataManager.register(TIME_TRAVEL_TYPE, 0);
        super.entityInit();
    }

    public void setTimeTravelType(TimeTravelTypes timeTravelType)
    {
        this.dataManager.set(TIME_TRAVEL_TYPE, timeTravelType.ordinal());
    }

    public TimeTravelTypes getTimeTravelType()
    {
        return TimeTravelTypes.getType(this.dataManager.get(TIME_TRAVEL_TYPE));
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if(world.isRemote)
        {
            if(TIME_TRAVEL_TYPE.equals(key))
            {
                TimeTravelTypes type = TimeTravelTypes.getType(this.dataManager.get(TIME_TRAVEL_TYPE));
                this.timeTravelTypes = type;
            }
     /*       if(TURBO_TYPE.equals(key))
            {
                TurboType typeTurbo = TurboType.getType(this.dataManager.get(TURBO_TYPE));
                this.turboType = typeTurbo;
                turbo.setItemDamage(typeTurbo.ordinal());
            } */
        }
    }

    protected EntityTimeTravelVehicle(World worldIn) {
        super(worldIn);
    }
}
