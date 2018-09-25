package net.alpha.bttf.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.buffer.ByteBuf;
import net.alpha.bttf.Main;
import net.alpha.bttf.client.ClientEvent;
import net.alpha.bttf.entity.render.Doors;
import net.alpha.bttf.entity.render.Doors2;
import net.alpha.bttf.init.ModItems;
import net.alpha.bttf.init.ModSounds;
import net.alpha.bttf.item.ItemPlutonium;
import net.alpha.bttf.network.PacketHandler;
import net.alpha.bttf.network.messages.MessageAcceleration;
import net.alpha.bttf.network.messages.MessageBrake;
import net.alpha.bttf.network.messages.MessageEngine;
import net.alpha.bttf.network.messages.MessageTurn;
import net.alpha.bttf.proxy.ClientProxy;
import net.alpha.bttf.timetravel.TimeTravelTypes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import scala.tools.nsc.interpreter.Power;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EntityVehicle extends Entity implements IEntityAdditionalSpawnData {
    private static final DataParameter<Float> CURRENT_SPEED = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> ACCELERATION_SPEED = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TURN_DIRECTION = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TURN_SENSITIVITY = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MAX_TURN_ANGLE = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ACCELERATION_DIRECTION = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> ENGINE_TYPE = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> BRAKING = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> REMOTE_CONTROLLING = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> CURRENT_FUEL = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> ENGINE_POWER = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.BOOLEAN);

    public static Entity renderEntity = null;
    public static boolean renderCarView = false;
    public static boolean remoteControlling = false;
    public static boolean doorIsOpen = false;

    public static boolean engineToglle = true;

    public float prevCurrentSpeed;
    public float currentSpeed;
    public float MAXSPEEDDecelerated;
    public float prevMAXSPEEDDecelerated;

    public EntityVehicle INSTANCE;

    public float speedMultiplier;

    public float drifting;

    @SideOnly(Side.CLIENT)
    public PowerPort powerport;

    public int turnAngle;
    public int prevTurnAngle;

    public float additionalYaw;
    public float prevAdditionalYaw;

    public static int MAXSPEED = 88;

    public float deltaYaw;
    public float wheelAngle;
    public float prevWheelAngle;

    public float vehicleMotionX;
    public float vehicleMotionY;
    public float vehicleMotionZ;

    public float prevDoorAngle;
    public float doorAngle;

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;

    private Vec3d heldOffset = Vec3d.ZERO;

    // Time Travel Types
    public static float CAR_ONE_TT;
    public static float CAR_TWO_TT;
    public static float CAR_THREE_TT;
    public static float TRAIN_TT;

    /**
     * ItemStack instances used for rendering
     */
    @SideOnly(Side.CLIENT)
    public ItemStack body, wheel, door_1, door_2, wheel_hovering;

    public void playChamberOpenSound()
    {
        powerport.playOpenSound();
    }

    public void playChamberClosedSound()
    {
        powerport.playClosedSound();
    }

    protected EntityVehicle(World worldIn) {
        super(worldIn);
        this.setSize(1F, 1F);
        this.stepHeight = 1.0F;
    }

    public EntityVehicle(World worldIn, double posX, double posY, double posZ) {
        this(worldIn);
        this.setPosition(posX, posY, posZ);
    }

    public abstract SoundEvent getMovingSound();

    public abstract SoundEvent getEngineStartupSound();



    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void entityInit() {
        this.dataManager.register(CURRENT_SPEED, 0F);
        this.dataManager.register(ACCELERATION_SPEED, 0.5F);
        this.dataManager.register(TURN_DIRECTION, TurnDirection.FORWARD.ordinal());
        this.dataManager.register(TURN_SENSITIVITY, 10);
        this.dataManager.register(MAX_TURN_ANGLE, 45);
        this.dataManager.register(ACCELERATION_DIRECTION, AccelerationDirection.NONE.ordinal());
        this.dataManager.register(TIME_SINCE_HIT, 0);
        this.dataManager.register(DAMAGE_TAKEN, 0F);
        this.dataManager.register(ENGINE_TYPE, 0);
        this.dataManager.register(BRAKING, false);
        this.dataManager.register(REMOTE_CONTROLLING, false);
        this.dataManager.register(CURRENT_FUEL, 0F);
        this.dataManager.register(ENGINE_POWER, true);

        if (this.world.isRemote) {
            this.onClientInit();
            this.onUpdate();
        }
    }

    public abstract void initSyncDataCompound();

    @SideOnly(Side.CLIENT)
    public void onClientInit() {
        powerport = PowerPort.PLUTONIUM_CHAMBER;

    }

    public void powerViaPlutonium(EntityPlayer player, EnumHand hand)
    {
        ItemStack item = player.getHeldItem(hand);
        if(!item.isEmpty() && item.getItem() instanceof ItemPlutonium)
        {
            ItemPlutonium plutonium = (ItemPlutonium) item.getItem();
            int drained = plutonium.drain(item, 100);
            int remaining = this.addFuel(drained);
            plutonium.fill(item, remaining);
        }
    }

    public boolean getPower()
    {
        Entity entity = this.getControllingPassenger();
        if(entity instanceof EntityPlayer)
        {
            if(((EntityPlayer) entity).isCreative())
            {
                return true;
            }
        }
        return this.getCurrentFuel() > 0F;
    }

    public int addFuel(int fuel)
    {
        float currentFuel = this.getCurrentFuel();
        currentFuel += fuel;
        int remaining = Math.max(0, Math.round(currentFuel - 1000F));
        currentFuel = Math.min(currentFuel, 1000F);
        this.setCurrentFuel(currentFuel);
        return remaining;
    }

    public void setCurrentFuel(float fuel)
    {
        this.dataManager.set(CURRENT_FUEL, fuel);
    }

    public float getCurrentFuel()
    {
        return this.dataManager.get(CURRENT_FUEL);
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public void onUpdate() {
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        MAXSPEEDDecelerated = prevCurrentSpeed;
        prevAdditionalYaw = additionalYaw;

        super.onUpdate();
        this.tickLerp();


        prevDoorAngle = doorAngle;

        prevCurrentSpeed = currentSpeed;
        prevTurnAngle = turnAngle;
        prevWheelAngle = wheelAngle;

        this.updateDoors();


        if (world.isRemote) {
            this.onClientUpdate();
        }

        /* If there driver, create particles */
        if (this.getControllingPassenger() != null) {
            this.createParticles();
        }

        /* Handle the current speed of the vehicle based on rider's forward movement */
        this.updateTurning();
        this.updateSpeed();
        this.updateBrakeSystem();
        this.updateVehicle();
        this.updateEngineSystem();
        this.setSpeed(currentSpeed);
        this.updateTurning();
        this.updateVehicleMotion();
        this.onClientUpdate();

        this.updateTurning();

        /* Updates the direction of the vehicle */
        rotationYaw -= deltaYaw;

        move(MoverType.SELF, motionX + vehicleMotionX, motionY + vehicleMotionY, motionZ + vehicleMotionZ);

        /* Reduces the motion and speed multiplier */
        if (this.onGround) {
            motionX *= 0.8;
            motionY *= 0.98D;
            motionZ *= 0.8;
        } else {
            motionX *= 0.98;
            motionY *= 0.98D;
            motionZ *= 0.98;
        }
        speedMultiplier *= 0.85;

        /* Checks for block collisions */
        this.doBlockCollisions();

        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), entity -> entity instanceof EntityVehicle);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                this.applyEntityCollision(entity);
            }
        }
    }

    public void updateVehicle() {
    }

    public abstract void setSyncDataCompound(NBTTagCompound parCompound);

    public abstract void updateVehicleMotion();

    public void updateDoors() {
        if (doorIsOpen == true) {
            Doors.open = true;
            Doors2.open = true;
        }
    }

    /**
     * Smooths the rendering on servers
     */
    private void tickLerp() {
        if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
            double d0 = this.posX + (this.lerpX - this.posX) / (double) this.lerpSteps;
            double d1 = this.posY + (this.lerpY - this.posY) / (double) this.lerpSteps;
            double d2 = this.posZ + (this.lerpZ - this.posZ) / (double) this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double) this.rotationYaw);
            this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.lerpSteps);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.lerpPitch - (double) this.rotationPitch) / (double) this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }

    protected void updateSpeed() {
        currentSpeed = this.getSpeed();

        AccelerationDirection acceleration = this.getAcceleration();
        if (this.getControllingPassenger() != null) {
            if (acceleration == AccelerationDirection.FORWARD) {
                this.currentSpeed += this.getAccelerationSpeed();
                if (this.currentSpeed > this.getMaxSpeed()) {
                    this.currentSpeed = this.getMaxSpeed();
                }
            } else if (acceleration == AccelerationDirection.REVERSE) {
                this.currentSpeed -= this.getAccelerationSpeed();
                if (this.currentSpeed < -(4.0F / 2)) {
                    this.currentSpeed = -(4.0F / 2);
                }
            } else {
                this.currentSpeed *= 0.9;
            }
        } else {
            this.currentSpeed *= 0.9;
        }
    }

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
        this.wheelAngle = this.turnAngle * Math.max(0.25F, 1.0F - Math.abs(currentSpeed / 30F));
        this.deltaYaw = this.wheelAngle * (currentSpeed / 30F) / 2F;
    }

    protected void updateEngineSystem()
    {
        boolean toggle = ClientProxy.KEY_ENGINE_STARTUP.isPressed();

        if(this.isOnline() != toggle)
        {
            this.setEngineOn(toggle);
            PacketHandler.INSTANCE.sendToServer(new MessageEngine(toggle));
            this.currentSpeed = 0F;
        }
        else
        {

        }
    }


    public void updateBrakeSystem() {
        boolean brake = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();

        if (this.isBraking() != brake) {
            this.setBrakeSystemOn(brake);
            PacketHandler.INSTANCE.sendToServer(new MessageBrake());
        }

        AccelerationDirection Accdirection = getAcceleration();
        TurnDirection direction = this.getTurnDirection();
        if (this.getControllingPassenger() != null && this.isBraking()) {
            this.currentSpeed -= this.getAccelerationSpeed();
            if (this.isBraking() && this.currentSpeed < -(0.0F)) {
                this.currentSpeed = -(0.0F);
            }

            this.deltaYaw = this.wheelAngle * (currentSpeed / 30F) / (this.isBraking() ? 1.5F : 2F);
        }
        if (this.remoteControlling == true && isBraking() & currentSpeed == 0F) {
            world.playSound((EntityPlayer) null, getPosition(), ModSounds.RC_BRAKE, SoundCategory.AMBIENT, 5.0F, 1.0F);
        }
        if (Accdirection == AccelerationDirection.FORWARD && this.isBraking() & currentSpeed == 0F) {
            world.playSound((EntityPlayer) null, getPosition(), ModSounds.REV, SoundCategory.AMBIENT, 2.5F, 1.0F);
        }
    }


    public void createParticles() {
        if (this.shouldShowEngineSmoke() && this.ticksExisted % 2 == 0) {
            Vec3d smokePosition = this.getEngineSmokePosition().rotateYaw(-this.rotationYaw * 0.017453292F);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + smokePosition.x, this.posY + smokePosition.y, this.posZ + smokePosition.z, -this.motionX, 0.0D, -this.motionZ);
        }
    }



    @SideOnly(Side.CLIENT)
    public void onClientUpdate()
    {
        EntityLivingBase entity = (EntityLivingBase) this.getControllingPassenger();
        if(entity != null && entity.equals(Minecraft.getMinecraft().player))
        {
            AccelerationDirection acceleration = AccelerationDirection.fromEntity(entity);
            if(this.getAcceleration() != acceleration)
            {
                this.setAcceleration(acceleration);
                PacketHandler.INSTANCE.sendToServer(new MessageAcceleration(acceleration));
            }

            TurnDirection direction = TurnDirection.FORWARD;
            if(entity.moveStrafing < 0)
            {
                direction = TurnDirection.RIGHT;
            }
            else if(entity.moveStrafing > 0)
            {
                direction = TurnDirection.LEFT;
            }
            if(this.getTurnDirection() != direction)
            {
                this.setTurnDirection(direction);
                PacketHandler.INSTANCE.sendToServer(new MessageTurn(direction));
            }
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            this.doorIsOpen = true;
            player.startRiding(this);
            world.playSound((EntityPlayer) null, getPosition(), ModSounds.STARTUP, SoundCategory.AMBIENT, 5.0F, 1.0F);
        } else {
            ItemStack stack = player.getHeldItem(hand);
            world.playSound((EntityPlayer) null, getPosition(), ModSounds.STARTUP, SoundCategory.AMBIENT, 5.0F, 1.0F);
            if(!stack.isEmpty())
            {
                if(stack.getItem() == ModItems.CONTROLLER)
                {
                    if(!stack.hasTagCompound())
                    {
                        stack.setTagCompound(new NBTTagCompound());
                    }
                    if(!stack.getTagCompound().hasKey("linked_car"))
                    {
                        stack.getTagCompound().setString("linked_car", getUniqueID().toString());
                        if(!world.isRemote)
                        {
                            world.playSound((EntityPlayer)null, getPosition(), ModSounds.CONNECT, SoundCategory.AMBIENT, 1.0F, 1.0F);
                            player.sendMessage(new TextComponentString(TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + "Car succesfully linked!"));
                            player.sendMessage(new TextComponentString("Use WASD to drive around. Press C to view the car's camera."));
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        remoteControlling = false;
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else if (!this.world.isRemote && !this.isDead) {
            if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() != null && this.isPassenger(source.getTrueSource())) {
                return false;
            } else {
                this.setTimeSinceHit(10);
                this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);

                boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).capabilities.isCreativeMode;
                if (flag || this.getDamageTaken() > 40.0F) {
                    if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
                        //this.dropItemWithOffset(this.getItemBoat(), 1, 0.0F);
                    }

                    this.setDead();
                }

                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public abstract double getMountedYOffset();

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return true;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("maxSpeed", Constants.NBT.TAG_FLOAT)) {
            this.getMaxSpeed();
        }
        if (compound.hasKey("accelerationSpeed", Constants.NBT.TAG_FLOAT)) {
            this.setAccelerationSpeed(compound.getFloat("accelerationSpeed"));
        }
        if (compound.hasKey("turnSensitivity", Constants.NBT.TAG_INT)) {
            this.setTurnSensitivity(compound.getInteger("turnSensitivity"));
        }
        if (compound.hasKey("maxTurnAngle", Constants.NBT.TAG_INT)) {
            this.setMaxTurnAngle(compound.getInteger("maxTurnAngle"));
        }
        if (compound.hasKey("stepHeight", Constants.NBT.TAG_FLOAT)) {
            this.stepHeight = compound.getFloat("stepHeight");
        }

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("maxSpeed", this.getMaxSpeed());
        compound.setFloat("accelerationSpeed", this.getAccelerationSpeed());
        compound.setInteger("turnSensitivity", this.getTurnSensitivity());
        compound.setInteger("maxTurnAngle", this.getMaxTurnAngle());
        compound.setFloat("stepHeight", this.stepHeight);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        //TODO change to config option
        passenger.rotationYaw -= deltaYaw;
        passenger.setRotationYawHead(passenger.rotationYaw);
        applyYawToEntity(passenger);
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -120.0F, 120.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation() {
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0F);
    }

    public boolean isMoving() {
        return this.currentSpeed != 0;
    }

    public void setSpeed(float speed) {
        this.dataManager.set(CURRENT_SPEED, speed);
    }

    public float getSpeed() {
        return this.currentSpeed;
    }

    public float getNormalSpeed() {
        return this.currentSpeed;
    }

    public float getAccelerationSpeed() {
        return this.dataManager.get(ACCELERATION_SPEED);
    }

    public void setAccelerationSpeed(float speed) {
        this.dataManager.set(ACCELERATION_SPEED, speed);
    }

    public double getKilometersPreHour() {
        return Math.sqrt(Math.pow(this.posX - this.prevPosX, 2) + Math.pow(this.posZ - this.prevPosZ, 2)) * 20;
    }

    public void setTurnDirection(TurnDirection turnDirection) {
        this.dataManager.set(TURN_DIRECTION, turnDirection.ordinal());
    }

    public TurnDirection getTurnDirection() {
        return TurnDirection.values()[this.dataManager.get(TURN_DIRECTION)];
    }

    public void setAcceleration(AccelerationDirection direction) {
        this.dataManager.set(ACCELERATION_DIRECTION, direction.ordinal());
    }

    public AccelerationDirection getAcceleration() {
        return AccelerationDirection.values()[this.dataManager.get(ACCELERATION_DIRECTION)];
    }

    public void setTurnSensitivity(int sensitivity) {
        this.dataManager.set(TURN_SENSITIVITY, sensitivity);
    }

    public int getTurnSensitivity() {
        return this.dataManager.get(TURN_SENSITIVITY);
    }

    public void setMaxTurnAngle(int turnAngle) {
        this.dataManager.set(MAX_TURN_ANGLE, turnAngle);
    }

    public int getMaxTurnAngle() {
        return this.dataManager.get(MAX_TURN_ANGLE);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit) {
        this.dataManager.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return this.dataManager.get(TIME_SINCE_HIT);
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(float damageTaken) {
        this.dataManager.set(DAMAGE_TAKEN, damageTaken);
    }

    public boolean isRemoteControlling()
    {
        return this.dataManager.get(REMOTE_CONTROLLING);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken() {
        return this.dataManager.get(DAMAGE_TAKEN);
    }

    public float getMaxSpeed() {
        return MAXSPEED;
    }

    public void setBrakeSystemOn(boolean braking) {
        this.dataManager.set(BRAKING, braking);
    }

    public boolean isBraking() {
        return this.dataManager.get(BRAKING);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRenderEngine() {
        return true;
    }

    public Vec3d getEngineSmokePosition() {
        return new Vec3d(0, 0, 0);
    }

    public boolean shouldShowEngineSmoke() {
        return false;
    }

    public boolean isOnline()
    {
        return this.dataManager.get(ENGINE_POWER);
    }

    public void setEngineOn(boolean activated) {
        this.dataManager.set(ENGINE_POWER, false);
    }

    public boolean getEngineOn() {
        return this.dataManager.get(ENGINE_POWER);
    }


    public void setHeldOffset(Vec3d heldOffset) {
        this.heldOffset = heldOffset;
    }

    public Vec3d getHeldOffset() {
        return heldOffset;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
    }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = (double) yaw;
        this.lerpPitch = (double) pitch;
        this.lerpSteps = 10;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.canPassengerSteer() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.posX = this.lerpX;
            this.posY = this.lerpY;
            this.posZ = this.lerpZ;
            this.rotationYaw = (float) this.lerpYaw;
            this.rotationPitch = (float) this.lerpPitch;
        }
        if (passenger instanceof EntityPlayer && world.isRemote) {
            Main.proxy.playVehicleSound((EntityPlayer) passenger, this);
        }
    }

    public enum TurnDirection {
        LEFT(1), FORWARD(0), RIGHT(-1);

        final int dir;

        TurnDirection(int dir) {
            this.dir = dir;
        }

        public int getDir() {
            return dir;
        }
    }

    public Vec3d getTimeTravelWheelPosition() {
        return new Vec3d(1, 0.3, 1.45);
    }

    public Vec3d getTimeTravelFrontAnimationPosition() {
        return new Vec3d(-0.20, 2.5, 3);
    }

    public Vec3d getTimeTravelWheel2Position() {
        return new Vec3d(-1, 0.3, 1.45);
    }


    public enum AccelerationDirection {
        FORWARD, NONE, REVERSE;

    public static AccelerationDirection fromEntity(EntityLivingBase entity) {
        if (entity.moveForward > 0) {
            return FORWARD;
        } else if (entity.moveForward < 0) {
            return REVERSE;
        }
        return NONE;
    }
}

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        this.readSpawnData(buffer);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }

    public enum  PowerPort
    {
        PLUTONIUM_CHAMBER(ModItems.PLUTONIUM_PLUG, ModSounds.PLUTONIUM_CHAMBER, ModSounds.PLUTONIUM_CHAMBER_CLOSE, 1F, 1F);

        private ItemStack PLUTONIUM;
        private SoundEvent soundOpen, soundClose;
        private float pitchOpen, pitchClose;

        private PowerPort(Item PLUTONIUM,SoundEvent soundOpen, SoundEvent soundClose, float pitchOpen, float pitchClose) {
            this.PLUTONIUM = new ItemStack(PLUTONIUM);
            this.soundClose = soundClose;
            this.soundOpen = soundOpen;
            this.pitchClose = pitchClose;
            this.pitchOpen = pitchOpen;
        }

        public ItemStack getPLUTONIUM() {
            return PLUTONIUM;
        }

        public float getPitchClose() {
            return pitchClose;
        }

        public SoundEvent getSoundClose() {
        return soundClose;
        }

        public void playOpenSound()
        {

        }

        public void playClosedSound()
        {

        }

        public float getPitchOpen() {
            return pitchOpen;
        }

    }


}