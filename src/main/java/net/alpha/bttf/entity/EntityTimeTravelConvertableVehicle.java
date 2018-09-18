package net.alpha.bttf.entity;

import net.alpha.bttf.network.PacketHandler;
import net.alpha.bttf.network.messages.MessageHover;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public abstract class EntityTimeTravelConvertableVehicle extends EntityVehicle {

    private static final DataParameter<Float> TIME_SPEED = EntityDataManager.createKey(EntityTimeTravelConvertableVehicle.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TIME_TRAVEL_ENUM = EntityDataManager.createKey(EntityTimeTravelConvertableVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Float> LIFT = EntityDataManager.createKey(EntityTimeTravelConvertableVehicle.class, DataSerializers.FLOAT);
    //   private static final DataParameter<Boolean> DRIFTING = EntityDataManager.createKey(EntityTimeTravelVehicle.class, DataSerializers.BOOLEAN);
 private static final DataParameter<Integer> HOVER_DIRECTION = EntityDataManager.createKey(EntityTimeTravelConvertableVehicle.class, DataSerializers.VARINT);

    protected EntityTimeTravelConvertableVehicle(World worldIn) {
        super(worldIn);
    }


    // Time Travel Types
    public static float CAR_ONE_TT;
    public static float CAR_TWO_TT;
    public static float CAR_THREE_TT;
    public static float TRAIN_TT;

    public float bodyAxis;
    public float rotationAxis;


    // Hover
    public float lift;
    public float prevBodyRotationX;
    public float prevBodyRotationY;
    public float prevBodyRotationZ;

    public float bodyRotationX;
    public float bodyRotationY;
    public float bodyRotationZ;
    public static boolean hover = false;


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
        this.dataManager.register(HOVER_DIRECTION, HoverDirection.NONE.ordinal());
        this.dataManager.register(LIFT, 0F);
        super.entityInit();
    }

    @Override
    public void onUpdate() {

        prevAdditionalYaw = additionalYaw;
        prevFrontWheelRotation = frontWheelRotation;
        prevRearWheelRotation = rearWheelRotation;

        prevCurrentSpeed = currentSpeed;


        this.prevBodyRotationX = this.bodyRotationX;
        this.prevBodyRotationY = this.bodyRotationY;
        this.prevBodyRotationZ = this.bodyRotationZ;

        EntityLivingBase entity = (EntityLivingBase) this.getControllingPassenger();
        if(entity != null && entity.equals(Minecraft.getMinecraft().player))
        {
            boolean flapUp = Keyboard.isKeyDown(Keyboard.KEY_UP);
            boolean flapDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN);

            HoverDirection flapDirection = HoverDirection.fromInput(flapUp, flapDown);
            if(this.getHoverDirection() != flapDirection)
            {
                this.setFlapDirection(flapDirection);
                PacketHandler.INSTANCE.sendToServer(new MessageHover(flapDirection));
            }
        }

        if(this.isHovering())
        {
            this.bodyRotationX = (float) Math.toDegrees(Math.atan2(motionY, currentSpeed / 20F));
            this.bodyRotationZ = (this.turnAngle / (float) getMaxTurnAngle()) * 20F;
        }
        else
        {
            this.bodyRotationX *= 0.5F;
            this.bodyRotationZ *= 0.5F;
        }

        if(this.getPower())
        {
            this.updateTimeTravel();
        }
        this.updateWheels();
        this.updateVehicleMotion();
        this.updateHover();
        this.updateBrakeSystem();
        this.updateTurning();
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

    public abstract SoundEvent getBeginGate();

    public abstract SoundEvent getEndGate();

    public abstract SoundEvent getEmtptyGate();


    @Override
    protected void updateTurning()
    {
        TurnDirection direction = this.getTurnDirection();
        if(this.getControllingPassenger() != null && direction != TurnDirection.FORWARD)
        {
            this.turnAngle += direction.dir * getTurnSensitivity();
            if(Math.abs(this.turnAngle) > getMaxTurnAngle())
            {
                this.turnAngle = getMaxTurnAngle() * direction.dir;
            }
        }
        else
        {
            this.turnAngle *= 0.75;
        }
        this.wheelAngle = this.turnAngle * Math.max(0.25F, 1.0F - Math.abs(Math.min(currentSpeed, 30F) / 30F));
        this.deltaYaw = this.wheelAngle;

        if(this.isHovering())
        {
            this.deltaYaw *= 0.5;
        }
        else
        {
            this.deltaYaw *= 0.5 * (0.5 + 0.5 * (1.0F - Math.min(currentSpeed, 15F) / 15F));
        }
    }

    @Override
    public void updateVehicleMotion()
    {
        float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F) / 20F; //Divide by 20 ticks
        float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F) / 20F;

        float currentSpeed = this.currentSpeed;

        if (speedMultiplier > 1.0F) {
            speedMultiplier = 1.0F;
        }

        /* Applies the speed multiplier to the current speed */
        currentSpeed = currentSpeed + (currentSpeed * speedMultiplier);

        this.updateHover();

        this.vehicleMotionX = (-currentSpeed * f1);
        this.vehicleMotionZ = (currentSpeed * f2);
        this.motionY += lift;
        this.motionY -= 0.05;
    }

    public void updateHover()
    {
        boolean boost = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();

        HoverDirection flapDirection = getHoverDirection();
        if(flapDirection == HoverDirection.UP)
        {
            this.lift += 0.04F * (Math.min(Math.max(currentSpeed - 5F, 0F), 15F) / 15F);
        }
        else if(flapDirection == HoverDirection.DOWN)
        {
            this.lift -= 0.06F * (Math.min(currentSpeed, 15F) / 15F);
        }
        this.setLift(this.lift);
    }


    protected void updateSpeed()
    {
        lift = 0;
        currentSpeed = this.getSpeed();

        if(this.getControllingPassenger() != null)
        {
            AccelerationDirection acceleration = getAcceleration();
            if(Keyboard.isKeyDown(Keyboard.KEY_UP))
            {
                if(this.motionY < 0)
                {
                    this.motionY *= 0.95;
                }

                float accelerationSpeed = this.getAccelerationSpeed();
                if(this.currentSpeed < this.getMaxSpeed())
                {
                    this.currentSpeed += accelerationSpeed;
                }
                this.lift = 0.051F * (Math.min(currentSpeed, 15F) / 15F);
            }
            else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            {
                if(this.isHovering())
                {
                    this.currentSpeed *= 0.95F;
                }
                else
                {
                    this.currentSpeed *= 0.9F;
                }
            }

            if(acceleration == AccelerationDirection.FORWARD)
            {
                if(this.isHovering())
                {
                    this.currentSpeed *= 0.995F;
                }
                else
                {
                    this.currentSpeed *= 0.98F;
                }
                this.lift = 0.04F * (Math.min(currentSpeed, 15F) / 15F);
            }
        }
        else
        {
            if(this.isHovering())
            {
                this.currentSpeed *= 0.98F;
            }
            else
            {
                this.currentSpeed *= 0.85F;
            }
        }
    }

    public void timeTravelOne(TimeTravelEnum timeTravelEnum) {

        if(this.currentSpeed == MAXSPEED)
        {
            // Left Front Wheel
            Vec3d smokePosition = this.getTimeTravelWheelPosition().rotateYaw(-this.rotationYaw * 0.017453292F);
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + smokePosition.x, this.posY + smokePosition.y, this.posZ + smokePosition.z, -this.motionX, 0.0D, -this.motionZ);
            this.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX + smokePosition.x, this.posY + smokePosition.y, this.posZ + smokePosition.z, -this.motionX, 0.0D, -this.motionZ);

            // Right Front Wheel
            Vec3d smokePosition2 = this.getTimeTravelWheel2Position().rotateYaw(-this.rotationYaw * 0.017453292F);
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + smokePosition2.x, this.posY + smokePosition2.y, this.posZ + smokePosition.z, -this.motionX, 0.0D, -this.motionZ);
            this.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX + smokePosition2.x, this.posY + smokePosition2.y, this.posZ + smokePosition.z, -this.motionX, 0.0D, -this.motionZ);

            Vec3d front = this.getTimeTravelFrontAnimationPosition().rotateYaw(-this.rotationYaw * 0.017453292F);
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX + front.x, this.posY + front.y, this.posZ + front.z, -this.motionX, 0.0D, -this.motionZ);

            Vec3d front2 = new Vec3d(-1, 1.6, 1.45);
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX + front2.x, this.posY + front2.y, this.posZ + front2.z, -this.motionX, 0.0D, -this.motionZ);

            Vec3d front3 = new Vec3d(2, 1.6, 1.45);
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX + front3.x, this.posY + front3.y, this.posZ + front3.z, -this.motionX, 0.0D, -this.motionZ);


            this.getEmtptyGate();
            //    System.out.println("Oh No, Something Broke Message Chronicle Or Staff On Discord If This Happens!");

        }

//            this.mc.getTextureManager().bindTexture(Textures.ICE);
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

    public void setFlapDirection(HoverDirection flapDirection)
    {
        this.dataManager.set(HOVER_DIRECTION, flapDirection.ordinal());
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

    public HoverDirection getHoverDirection()
    {
        return HoverDirection.values()[this.dataManager.get(HOVER_DIRECTION)];
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

    public enum TimeTravelEnum
    {
        ONE, TWO, THREE, FOUR, NONE;

        public static TimeTravelEnum getType(EntityLivingBase vehicle)
        {
            if(CAR_ONE_TT > 0)
            {
              return ONE;
            }
            if(CAR_TWO_TT < 0)
            {
                return TWO;
            }
            if(CAR_THREE_TT >= 0)
            {
                return THREE;
            }
            if(TRAIN_TT <= 0)
            {
                return FOUR;
            }
            return NONE;
        }

    }

    public float getLift()
    {
        return this.dataManager.get(LIFT);
    }

    public void setLift(float lift)
    {
        this.dataManager.set(LIFT, lift);
    }

    public boolean isHovering()
    {
        return !this.onGround;
    }

    public enum HoverDirection
    {
        UP, DOWN, NONE;

        public static HoverDirection fromInput(boolean up, boolean down)
        {
            return up && !down ? UP : down && !up ? DOWN : NONE;
        }
    }

}
