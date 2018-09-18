package net.alpha.bttf.proxy;

import net.alpha.bttf.Main;
import net.alpha.bttf.Reference;
import net.alpha.bttf.client.ClientEvent;
import net.alpha.bttf.client.audio.MovingEngineStartupSound;
import net.alpha.bttf.client.audio.MovingSoundVehicle;
import net.alpha.bttf.tileentity.TileEntityCarOne;
import net.alpha.bttf.tileentity.render.RenderTileEntityCarOne;
import net.alpha.bttf.vehicles.EntityCarOne;
import net.alpha.bttf.vehicles.EntityCarTwo;
import net.alpha.bttf.entity.EntityVehicle;
import net.alpha.bttf.vehicles.render.RenderCarOne;
import net.alpha.bttf.vehicles.render.RenderCarTwo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class ClientProxy implements Proxy {

    public static final KeyBinding KEY_HOVER = new KeyBinding("key.hover", Keyboard.KEY_H, "key.categories.bttf");
    public static final KeyBinding KEY_CAMERA = new KeyBinding("key.camera.desc", Keyboard.KEY_C, "key.categories.bttf");
    public static final KeyBinding KEY_HOVER_BRAKE = new KeyBinding("key.hover.brake", Keyboard.KEY_G, "key.categories.bttf");
    public static final KeyBinding KEY_CIRCUITS = new KeyBinding("key.circuits", Keyboard.KEY_Z, "key.categories.bttf");
    public static final KeyBinding KEY_ENGINE_STARTUP = new KeyBinding("key.vehicle", Keyboard.KEY_LCONTROL, "key.categories.bttf");

    @Override
    public void preInit() {
        ClientRegistry.registerKeyBinding(KEY_HOVER);
        ClientRegistry.registerKeyBinding(KEY_CAMERA);
        ClientRegistry.registerKeyBinding(KEY_HOVER_BRAKE);
        ClientRegistry.registerKeyBinding(KEY_ENGINE_STARTUP);
        RenderingRegistry.registerEntityRenderingHandler(EntityCarOne.class, RenderCarOne::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCarTwo.class, RenderCarTwo::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCarOne.class, new RenderTileEntityCarOne());


        MinecraftForge.EVENT_BUS.register(new ClientEvent());

    }

    @Override
    public void Init()
    {
    }

    @Override
    public void playVehicleSound(EntityPlayer player, EntityVehicle vehicle) {

        Minecraft.getMinecraft().addScheduledTask(() ->
        {
            if (vehicle.getMovingSound() != null) {
                Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundVehicle(vehicle));
            }
            if (vehicle.getEngineStartupSound() != null) {
                Minecraft.getMinecraft().getSoundHandler().playSound(new MovingEngineStartupSound(vehicle));
            }
        });
    }



}