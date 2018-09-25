package net.alpha.bttf.vehicles;

import net.alpha.bttf.entity.EntityDefaultVehicle;
import net.alpha.bttf.entity.EntityVehicle;
import net.alpha.bttf.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityDefaultDelorean extends EntityDefaultVehicle {


    protected EntityDefaultDelorean(World worldIn) {
        super(worldIn);
    }

    @Override
    public void entityInit() {

        body = new ItemStack(ModItems.DEFAULT_BODY);
        wheel = new ItemStack(ModItems.WHEEL);
        door_1 = new ItemStack(ModItems.DOOR_LEFT);
        door_2 = new ItemStack(ModItems.DOOR_RIGHT);

        super.entityInit();
    }

    @Override
    public SoundEvent getMovingSound() {
        return null;
    }

    @Override
    public SoundEvent getEngineStartupSound() {
        return null;
    }

    @Override
    public void initSyncDataCompound() {

    }

    @Override
    public void setSyncDataCompound(NBTTagCompound parCompound) {

    }

    @Override
    public void updateVehicleMotion() {

    }

    @Override
    public double getMountedYOffset() {
        return 0;
    }
}
