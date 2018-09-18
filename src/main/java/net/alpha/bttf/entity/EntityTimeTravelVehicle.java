package net.alpha.bttf.entity;

import net.alpha.bttf.timetravel.Textures;
import net.alpha.bttf.timetravel.TimeTravel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityTimeTravelVehicle extends EntityVehicle {

    private static final DataParameter<Float> TIME_SPEED = EntityDataManager.createKey(EntityTimeTravelVehicle.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TIME_TRAVEL_ENUM = EntityDataManager.createKey(EntityTimeTravelVehicle.class, DataSerializers.VARINT);
 //   private static final DataParameter<Boolean> DRIFTING = EntityDataManager.createKey(EntityTimeTravelVehicle.class, DataSerializers.BOOLEAN);

    protected EntityTimeTravelVehicle(World worldIn) {
        super(worldIn);
    }

    public Minecraft mc;

    // Normal Car Features
    public float additionalYaw;
    public float prevAdditionalYaw;

    public float frontWheelRotation;
    public float prevFrontWheelRotation;
    public float rearWheelRotation;
    public float prevRearWheelRotation;

    // Time Travel Speed/Light Levels
    public static int TIME_TRAVEL_SPEED = 5;
    public static double LIGHT_LEVEL = 2.34;



    @Override
    public void entityInit() {
        this.dataManager.register(TIME_SPEED, getTimeSpeed());
        this.dataManager.register(TIME_TRAVEL_ENUM, TimeTravelEnum.NONE.ordinal());
        super.entityInit();
    }

    @Override
    public void onUpdate() {

        prevAdditionalYaw = additionalYaw;
        prevFrontWheelRotation = frontWheelRotation;
        prevRearWheelRotation = rearWheelRotation;

        prevCurrentSpeed = currentSpeed;

        this.updateWheels();
        this.updateVehicleMotion();
        super.onUpdate();
    }

    public void updateWheels()
    {
        float speedPercent = this.getNormalSpeed();
        AccelerationDirection acceleration = this.getAcceleration();
        if(this.getControllingPassenger() != null && acceleration == AccelerationDirection.FORWARD)
        {
            this.rearWheelRotation -= 68F * (1.0 - speedPercent);
        }
        this.frontWheelRotation -= (68F * speedPercent);
        this.rearWheelRotation -= (68F * speedPercent);
    }


    @Override
    public void updateVehicleMotion() {
        float currentSpeed = this.currentSpeed;

        if (speedMultiplier > 1.0F) {
            speedMultiplier = 1.0F;
        }

        /* Applies the speed multiplier to the current speed */
        currentSpeed = currentSpeed + (currentSpeed * speedMultiplier);

        float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F) / 20F; //Divide by 20 ticks
        float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F) / 20F;
        this.vehicleMotionX = (-currentSpeed * f1);
        this.vehicleMotionZ = (currentSpeed * f2);
    }



        public Vec3d getTimeTravelWheelPosition()
    {
        return new Vec3d(1, 0.3, 1.45);
    }

    public Vec3d getTimeTravelFrontAnimationPosition()
    {
        return new Vec3d(-0.20, 2.5, 3);
    }

    public Vec3d getTimeTravelWheel2Position()
    {
        return new Vec3d(-1, 0.3, 1.45);
    }

    @Override
    protected void removePassenger(Entity passenger)
    {
        super.removePassenger(passenger);
        if(this.getControllingPassenger() == null)
        {
            this.rotationYaw -= this.additionalYaw;
            this.additionalYaw = 0;
        }
    }


    public static void setLightLevel(double lightLevel)
    {
        lightLevel = LIGHT_LEVEL;
    }

    public double getLightLevel()
    {
        return LIGHT_LEVEL;
    }


    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
    }

    public float getTimeSpeed()
    {
        return TIME_TRAVEL_SPEED;
    }

    public void setTimeSpeed(float speed)
    {
        this.dataManager.set(TIME_SPEED, speed);
    }

}
