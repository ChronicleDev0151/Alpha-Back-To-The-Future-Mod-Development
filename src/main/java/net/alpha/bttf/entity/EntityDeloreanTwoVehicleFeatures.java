package net.alpha.bttf.entity;

import net.alpha.bttf.init.ModSounds;
import net.alpha.bttf.network.PacketHandler;
import net.alpha.bttf.network.messages.MessageBrake;
import net.alpha.bttf.network.messages.MessageDrift;
import net.alpha.bttf.network.messages.MessageHover;
import net.alpha.bttf.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.input.Keyboard;

import javax.swing.text.JTextComponent;
import java.security.Key;

public abstract class EntityDeloreanTwoVehicleFeatures extends EntityTimeTravelVehicle
{

    private static final DataParameter<Boolean> DRIFTING = EntityDataManager.createKey(EntityDeloreanTwoVehicleFeatures.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> LIFT = EntityDataManager.createKey(EntityDeloreanTwoVehicleFeatures.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> HOVER_DIRECTION = EntityDataManager.createKey(EntityDeloreanTwoVehicleFeatures.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HOVER_TYPES = EntityDataManager.createKey(EntityDeloreanTwoVehicleFeatures.class, DataSerializers.VARINT);

    public static EntityDeloreanTwoVehicleFeatures INSTANCE;

    public float drifting;
    public float additionalYaw;
    public float prevAdditionalYaw;

    // Normal Vehicle Physic's

    public float frontWheelRotation;
    public float prevFrontWheelRotation;
    public float rearWheelRotation;
    public float prevRearWheelRotation;

    // WIP Hover
    private float lift;
    private float prevBoostSpeed;
    private float boostSpeed;

    public float prevBodyRotationX;
    public float prevBodyRotationY;
    public float prevBodyRotationZ;

    public float bodyRotationX;
    public float bodyRotationY;
    public float bodyRotationZ;



    @Override
    public void entityInit()
    {
        this.dataManager.register(HOVER_DIRECTION, HoverDirection.NONE.ordinal());
        this.dataManager.register(HOVER_TYPES, HoverTypes.NONE.ordinal());
        this.dataManager.register(DRIFTING, false);
        this.dataManager.register(LIFT, 0F);
        super.entityInit();
    }

    @Override
    public void onClientInit()
    {
        super.onClientInit();
    }

    @Override
    public void updateVehicleMotion()
    {
        float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F) / 20F; //Divide by 20 ticks
        float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F) / 20F;

        this.updateHover();

        this.vehicleMotionX = (-currentSpeed * f1);
        this.vehicleMotionZ = (currentSpeed * f2);
        this.motionY += lift;
        this.motionY -= 0.05;
    }

    @Override
    public void onClientUpdate()
    {
        super.onClientUpdate();

        this.prevBodyRotationX = this.bodyRotationX;
        this.prevBodyRotationY = this.bodyRotationY;
        this.prevBodyRotationZ = this.bodyRotationZ;

        EntityLivingBase entity = (EntityLivingBase) this.getControllingPassenger();
        if(entity != null && entity.equals(Minecraft.getMinecraft().player))
        {
            boolean flapUp = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
            boolean flapDown = Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown();

            HoverDirection flapDirection = HoverDirection.fromEntity(entity);
            if(this.getHoverDirection() != flapDirection)
            {
                this.setHoverDirection(flapDirection);
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
    }

    @Override
    protected void updateSpeed()
    {
        lift = 0;
        currentSpeed = this.getSpeed();

        if(this.getControllingPassenger() != null)
        {

            AccelerationDirection acceleration = getAcceleration();

            if (ClientProxy.KEY_HOVER.isKeyDown()) {
                if (acceleration == AccelerationDirection.FORWARD) {
                    if (this.motionY < 0) {
                        this.motionY *= 0.95;
                    }
                    float accelerationSpeed = this.getAccelerationSpeed();
                    if (this.currentSpeed < this.getMaxSpeed()) {
                        this.currentSpeed += accelerationSpeed;
                    }
                    this.lift = 0.051F * (Math.min(currentSpeed, 15F) / 15F);
                } else if (acceleration == AccelerationDirection.REVERSE) {
                    if (this.isHovering()) {
                        this.currentSpeed *= 0.95F;
                    } else {
                        this.currentSpeed *= 0.9F;
                    }
                }

                if (acceleration != AccelerationDirection.FORWARD) {
                    if (this.isHovering()) {
                        this.currentSpeed *= 0.995F;
                    } else {
                        this.currentSpeed *= 0.98F;
                    }
                    this.lift = 0.04F * (Math.min(currentSpeed, 15F) / 15F);
                }
            }else
            {
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
            }

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

    public void updateHover()
    {
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


    @Override
    public void updateBrakeSystem() {


        if(this.isHovering())
        {
            if(this.isBraking())
            {
                        this.boostSpeed = (float) this.posX / (float) this.posZ;
                        world.playSound((EntityPlayer)null, getPosition(), ModSounds.BOOST, SoundCategory.AMBIENT, 1F, 1F);
            }
        }

        super.updateBrakeSystem();
    }



    private void updateDrifting()
    {
        TurnDirection turnDirection = this.getTurnDirection();
        if(this.getControllingPassenger() != null && this.isDrifting() && turnDirection != TurnDirection.FORWARD)
        {
            AccelerationDirection acceleration = this.getAcceleration();
            if(acceleration == AccelerationDirection.FORWARD)
            {
                this.currentSpeed *= 0.95F;
            }
            this.drifting = Math.min(1.0F, this.drifting + (1.0F / 8.0F));
        }
        else
        {
            this.drifting *= 0.85F;
        }
        this.additionalYaw = 35F * (turnAngle / 45F) * drifting;

        //Updates the delta yaw to consider drifting
        this.deltaYaw = this.wheelAngle * (currentSpeed / 30F) / (this.isDrifting() ? 1.5F : 2F);
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
    public void createParticles()
    {

        int x = MathHelper.floor(this.posX);
        int y = MathHelper.floor(this.posY - 0.2D);
        int z = MathHelper.floor(this.posZ);
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = this.world.getBlockState(pos);
        if(state.getMaterial() != Material.AIR && state.getMaterial().isToolNotRequired())
        {
            if(this.getAcceleration() == AccelerationDirection.FORWARD)
            {
                if(this.isDrifting())
                {
                    for(int i = 0; i < 3; i++)
                    {
                        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, Block.getStateId(state));
                    }
                }
                else
                {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, Block.getStateId(state));
                }
            }
        }

        if(this.shouldShowEngineSmoke() && this.ticksExisted % 2 == 0)
        {
            Vec3d smokePosition = this.getEngineSmokePosition().rotateYaw(-(this.rotationYaw - this.additionalYaw) * 0.017453292F);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + smokePosition.x, this.posY + smokePosition.y, this.posZ + smokePosition.z, -this.motionX, 0.0D, -this.motionZ);
        }
    }

    @Override
    protected void removePassenger(Entity passenger)
    {
        super.removePassenger(passenger);
        if(this.getControllingPassenger() == null)
        {
            this.rotationYaw -= this.additionalYaw;
            this.additionalYaw = 0;
            this.drifting = 0;
        }
    }

    public void setDrifting(boolean drifting)
    {
        this.dataManager.set(DRIFTING, drifting);
    }

    public boolean isDrifting()
    {
        return this.dataManager.get(DRIFTING);
    }

    public boolean isHovering()
    {
        return !this.onGround;
    }

    public float getLift()
    {
        return this.dataManager.get(LIFT);
    }

    public void setLift(float lift)
    {
        this.dataManager.set(LIFT, lift);
    }

    public void setHoverDirection(HoverDirection flapDirection)
    {
        this.dataManager.set(HOVER_DIRECTION, flapDirection.ordinal());
    }

    public HoverDirection getHoverDirection()
    {
        return HoverDirection.values()[this.dataManager.get(HOVER_DIRECTION)];
    }

    public HoverTypes getHoverTypes()
    {
        return HoverTypes.values()[this.dataManager.get(HOVER_TYPES)];
    }

    public void setHoverType(HoverTypes hoverType)
    {
        this.dataManager.set(HOVER_TYPES, hoverType.ordinal());
    }

    public enum HoverDirection
    {
        UP, DOWN, NONE;

        public static HoverDirection fromEntity(EntityLivingBase entity) {
            if (entity.moveForward > 0) {
                return UP;
            } else if (entity.moveForward < 0) {
                return DOWN;
            }
            return NONE;
        }
    }


    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("hoverDirection", Constants.NBT.TAG_INT))
        {
            this.setHoverDirection(HoverDirection.values()[compound.getInteger("hoverDirection")]);
        }
        if(compound.hasKey("lift", Constants.NBT.TAG_FLOAT))
        {
            this.setLift(compound.getFloat("lift"));
        }
    }

    public enum HoverTypes
    {
       BOOST, NONE;

       public static HoverTypes fromInput(boolean boost)
       {
           return boost ? BOOST : NONE;
       }
    }

    /*
     * Overridden to prevent players from taking fall damage when landing a plane
     */
    @Override
    public void fall(float distance, float damageMultiplier) {}

    protected EntityDeloreanTwoVehicleFeatures(World worldIn) {
        super(worldIn);
    }
}
