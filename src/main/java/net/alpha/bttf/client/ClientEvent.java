package net.alpha.bttf.client;

import ca.weblite.objc.Client;
import net.alpha.bttf.Reference;
import net.alpha.bttf.RemoteMovementInput;
import net.alpha.bttf.entity.*;
import net.alpha.bttf.gui.TimeCircuits;
import net.alpha.bttf.init.ModItems;
import net.alpha.bttf.network.PacketHandler;
import net.alpha.bttf.network.messages.MessageAcceleration;
import net.alpha.bttf.network.messages.MessageTurn;
import net.alpha.bttf.proxy.ClientProxy;
import net.alpha.bttf.vehicles.EntityCarOne;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class ClientEvent
{
    public static Entity renderEntity = null;
    public static boolean renderCarView = false;
    public static boolean isRemoteControlling = false;
    public EntityVehicle vehicle;
    public Entity riding;
    private EntityPlayer playerIn;
    public EntityLivingBase entity;
    private MessageTurn messageTurn;
    private BlockPos pos;
    private World worldIn;

    public void onPreRender(EntityLivingBase base) {
        Entity ridingEntity = base.getRidingEntity().getRidingEntity();
        if (ridingEntity instanceof EntityCarOne) {
            EntityTimeTravelVehicle vehicle = (EntityTimeTravelVehicle) ridingEntity;
            double offset = vehicle.getMountedYOffset() * 3 - 3 * 0.0625;
            GlStateManager.translate(0, offset, 0);
            float currentSpeedNormal = (vehicle.prevCurrentSpeed + (vehicle.currentSpeed - vehicle.prevCurrentSpeed) * vehicle.getMaxSpeed());
            float turnAngleNormal = (vehicle.prevTurnAngle + (vehicle.turnAngle - vehicle.prevTurnAngle) * 45F);
            GlStateManager.rotate(turnAngleNormal * currentSpeedNormal * 20F, 0, 0, 1);
            GlStateManager.translate(0, -offset, 0);
        }
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            EntityPlayerSP player = mc.player;
            if (!(player.movementInput instanceof RemoteMovementInput)) {
                player.movementInput = new RemoteMovementInput(player.movementInput);
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
        if(!stack.isEmpty() && stack.getItem() == ModItems.CONTROLLER)
        {
            event.getRenderer().getMainModel().rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            event.getRenderer().getMainModel().leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;

        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if(event.player == player)
        {
            isRemoteControlling = false;

            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() == ModItems.CONTROLLER)
            {
                if(stack.hasTagCompound())
                {
                    if(stack.getTagCompound().hasKey("linked_car"))
                    {
                        isRemoteControlling = true;

                        if(Keyboard.isKeyDown(Keyboard.KEY_W))
                        {
                       //.     moveCarForward();
                        }
                        if(Keyboard.isKeyDown(Keyboard.KEY_A))
                        {
                      //      turnCar(EntityVehicle.TurnDirection.LEFT);
                        }
                        if(Keyboard.isKeyDown(Keyboard.KEY_D))
                        {
                      //      turnCar(EntityVehicle.TurnDirection.RIGHT);
                        }
                        setView(renderEntity);
                    }
                }
            }
        }
    }

    public static void setView(Entity entity)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if(entity != null)
        {
            renderEntity = entity;
            mc.setRenderViewEntity(renderEntity);
            renderCarView = true;
        }
        else
        {
            renderEntity = null;
            mc.setRenderViewEntity(player);
            renderCarView = false;
        }
    }

 /*   public void moveCarForward()
    {
        Entity entity = Minecraft.getMinecraft().player;

        EntityLivingBase entityBase = (EntityLivingBase) entity.getControllingPassenger();
        if (entity != null && entity.equals(entityBase)) {
            EntityVehicle.AccelerationDirection acceleration = EntityVehicle.AccelerationDirection.fromEntity(entityBase);
            if (vehicle.getAcceleration() != acceleration) {
                vehicle.setAcceleration(acceleration);
                PacketHandler.INSTANCE.sendToServer(new MessageAcceleration(acceleration));
            }
        }
    } */

 /*   public void turnCar(EntityVehicle.TurnDirection turn)
    {
        Entity entity = Minecraft.getMinecraft().player;

        EntityLivingBase entityBase = (EntityLivingBase) entity.getControllingPassenger();

        EntityVehicle.TurnDirection direction = EntityVehicle.TurnDirection.FORWARD;
        if (entityBase.moveStrafing < 0) {
            direction = EntityVehicle.TurnDirection.RIGHT;
        } else if (entityBase.moveStrafing > 0) {
            direction = EntityVehicle.TurnDirection.LEFT;
        }
        if (vehicle.getTurnDirection() != direction) {
            vehicle.setTurnDirection(direction);
            PacketHandler.INSTANCE.sendToServer(new MessageTurn(direction));
        }
    } */

    @SubscribeEvent
    public void onRnderLast(RenderWorldLastEvent event)
    {
        if(renderCarView && renderEntity != null)
        {
            Minecraft mc = Minecraft.getMinecraft();
            Render render = mc.getRenderManager().getEntityRenderObject(mc.player);
            if(render instanceof RenderPlayer)
            {
                RenderPlayer renderPlayer = (RenderPlayer) render;
                ModelPlayer playerModel = renderPlayer.getMainModel();
                mc.getTextureManager().bindTexture(mc.player.getLocationSkin());

                double deltaX = mc.player.posX - renderEntity.prevPosX - (renderEntity.posX - renderEntity.prevPosX) * event.getPartialTicks();
                double deltaY = mc.player.posY - renderEntity.prevPosY - (renderEntity.posY - renderEntity.prevPosY) * event.getPartialTicks();
                double deltaZ = mc.player.posZ - renderEntity.prevPosZ - (renderEntity.posZ - renderEntity.prevPosZ) * event.getPartialTicks();

                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(deltaX, deltaY, deltaZ);
                    GlStateManager.rotate(180F, 1, 0, 0);
                    GlStateManager.translate(0, -1.5, 0);
                    GlStateManager.rotate(mc.player.rotationYaw, 0, 1, 0);
                    RenderHelper.enableGUIStandardItemLighting();
                    GlStateManager.enableAlpha();
                    playerModel.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    playerModel.isChild = false;
                    playerModel.render(mc.player, 0F, 0F, 0, 0f, mc.player.rotationPitch, 0.0625F);
                }
                GlStateManager.popMatrix();
            }
        }
    }




    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if(mc.inGameHasFocus)
            {
                EntityPlayer player = mc.player;
                if(player != null) {
                    Entity entity = player.getRidingEntity();
                    if (entity instanceof EntityTimeTravelVehicle) {
                        EntityTimeTravelVehicle vehicle = (EntityTimeTravelVehicle) entity;

                        String speed = new DecimalFormat("0.0").format(vehicle.getKilometersPreHour());
                        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "BPS: " + TextFormatting.RED + speed, 10, 10, Color.RED.getRGB());
                        String PLUTONIUM_LEVEL = new DecimalFormat("0").format(vehicle.getCurrentFuel());
                        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Plutonium Level: " + TextFormatting.YELLOW + PLUTONIUM_LEVEL, 10, 20, Color.WHITE.getRGB());
                    }
                    if (entity instanceof EntityDeloreanTwoVehicleFeatures) {
                        EntityDeloreanTwoVehicleFeatures vehicle = (EntityDeloreanTwoVehicleFeatures) entity;

                        String speed = new DecimalFormat("0.0").format(vehicle.getKilometersPreHour());
                        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "BPS: " + TextFormatting.RED + speed, 10, 10, Color.RED.getRGB());
                        String PLUTONIUM_LEVEL = new DecimalFormat("0").format(vehicle.getCurrentFuel());
                        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Plutonium Level: " + TextFormatting.YELLOW + PLUTONIUM_LEVEL, 10, 20, Color.WHITE.getRGB());

                  /*      String Hspeed = new DecimalFormat("0.0").format(vehicle.getHoverSpeed());
                        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Hover BPS: " + TextFormatting.RED + Hspeed, 10, 30, Color.RED.getRGB()); */
                    }
                    if(entity instanceof EntityDefaultVehicle)
                    {
                        EntityDefaultVehicle vehicle = (EntityDefaultVehicle) entity;
                        String speed = new DecimalFormat("0.0").format(vehicle.getKilometersPreHour());
                        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "BPS: " + TextFormatting.RED + speed, 10, 10, Color.RED.getRGB());
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if(ClientProxy.KEY_ENGINE_STARTUP.isPressed())
        {
            EntityVehicle.engineToglle = false;
        }

        if(ClientProxy.KEY_CIRCUITS.isKeyDown())
        {
           Minecraft.getMinecraft().displayGuiScreen(new TimeCircuits());
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer entity = mc.player;
        Entity ridimng = entity.getRidingEntity();

     /*   if(ridimng instanceof EntityVehicle)
        {
                Vec3d vec3 = mc.player.getLookVec();
                double dx = vec3.x * -10;
                double dy = vec3.y * -10;
                double dz = vec3.z * -10;

                mc.getRenderViewEntity().copyLocationAndAnglesFrom(mc.player);

                mc.getRenderViewEntity().posX += dx;
                mc.getRenderViewEntity().posY += dy;
                mc.getRenderViewEntity().posZ += dz;

                vec3 = null;
        } */

    }
}

