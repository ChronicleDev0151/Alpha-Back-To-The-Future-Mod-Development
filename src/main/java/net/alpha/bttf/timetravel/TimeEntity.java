package net.alpha.bttf.timetravel;


import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class TimeEntity extends Entity
{

    @Override
    public void onUpdate() {

        super.onUpdate();
    }

    public void startTimeTravelStages()
    {
        TimeTravel.INSTANCE.TimeTravel(1000);
    }


    public TimeEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {

    }

}
