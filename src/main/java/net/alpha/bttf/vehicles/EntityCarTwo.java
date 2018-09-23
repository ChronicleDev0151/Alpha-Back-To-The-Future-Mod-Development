package net.alpha.bttf.vehicles;

import net.alpha.bttf.entity.EntityTimeTravelConvertableVehicle;
import net.alpha.bttf.entity.EntityTimeTravelHoverVehicle;
import net.alpha.bttf.entity.EntityTimeTravelVehicle;
import net.alpha.bttf.entity.EntityVehicle;
import net.alpha.bttf.init.ModItems;
import net.alpha.bttf.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.Time;

public class EntityCarTwo extends EntityTimeTravelConvertableVehicle
{
    public EntityCarTwo(World worldIn){
        super(worldIn);
        this.setSize(1.0F, 1.0F); // 1.0F 0.3F
        this.setTimeTravelEnum(EntityVehicle.TimeTravelEnum.ONE);
    }


    @Override
    public void entityInit() {
        super.entityInit();
        body = new ItemStack(ModItems.BODY_TWO);
    //    wheel = new ItemStack(ModItems.WHEEL);
        door_1 = new ItemStack(ModItems.DOOR_LEFT);
        door_2 = new ItemStack(ModItems.DOOR_RIGHT);
        wheel_hovering = new ItemStack(ModItems.WHEEL);
    }

    @Override
    public void initSyncDataCompound() {

    }

    @Override
    public void setSyncDataCompound(NBTTagCompound parCompound) {

    }


  /*  @Override
    public SoundEvent getBeginGate() {
        return null;
    }

    @Override
    public SoundEvent getEndGate() {
        return null;
    }

    @Override
    public SoundEvent getEmtptyGate() {
        return null;
    } */

    @Override
    public SoundEvent getMovingSound() {
        return ModSounds.IDLE;
    }

    @Override
    public SoundEvent getEngineStartupSound() {
        return ModSounds.STARTUP;
    }

    @Override
    public SoundEvent getEmptyGate() {
        return null;
    }


    @Override
    public double getMountedYOffset()
    {
        return 1 * -0.015;
    }


    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            float xOffset = 0.3872F;
            float yOffset = (float)((this.isDead ? 0.01D : this.getMountedYOffset() + passenger.getYOffset()));
            float zOffset = -0.2F;

            if (this.getPassengers().size() > 0)
            {
                int index = this.getPassengers().indexOf(passenger);
                if (index > 0)
                {
                    xOffset -= (index / 2) * 0.3872F;
                    zOffset -= (index % 2) * 0.2F;
                }

                Vec3d vec3d = (new Vec3d(xOffset, 0.0D, zOffset)).rotateYaw(-(this.rotationYaw - additionalYaw) * 0.017453292F - ((float)Math.PI / 2F));
                passenger.setPosition(this.posX + vec3d.x, this.posY + (double)yOffset, this.posZ + vec3d.z);
                passenger.rotationYaw -= deltaYaw;
                passenger.setRotationYawHead(passenger.rotationYaw);
                this.applyYawToEntity(passenger, index > 1);
            }
        }
    }

    private void applyYawToEntity(Entity entityToUpdate, boolean isBackSeat)
    {
        entityToUpdate.setRenderYawOffset(this.rotationYaw - this.additionalYaw + (isBackSeat ? 180F : 0F));
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw + (isBackSeat ? 180F : 0F));
        float f1 = MathHelper.clamp(f, -120.0F, 120.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate)
    {
        int index = this.getPassengers().indexOf(entityToUpdate);
        this.applyYawToEntity(entityToUpdate, index > 1);
    }

    @Override
    protected boolean canFitPassenger(Entity passenger)
    {
        return this.getPassengers().size() < 2;
    }

}