package net.alpha.bttf.init;

import net.alpha.bttf.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds
{
    public static final SoundEvent TRAVEL_GATE_ONE;
    public static final SoundEvent ENGINE_FORWARD;
    public static final SoundEvent ARRIVAL;
    public static final SoundEvent REV;

    public static final SoundEvent TRAVEL_GATE_ONE_EMPTY;

    public static final SoundEvent PLUTONIUM_CHAMBER;
    public static final SoundEvent PLUTONIUM_CHAMBER_CLOSE;

    public static final SoundEvent CONNECT;
    public static final SoundEvent RC_BRAKE;

    public static final SoundEvent STARTUP;

    public static final SoundEvent IDLE;

    static
    {
        TRAVEL_GATE_ONE = registerSound("bttf:gate_one");
        ENGINE_FORWARD = registerSound("bttf:engine_forward");
        ARRIVAL = registerSound("bttf:arrival");
        REV = registerSound("bttf:rev");
        CONNECT = registerSound("bttf:connect");
        STARTUP = registerSound("bttf:startup");
        TRAVEL_GATE_ONE_EMPTY = registerSound("bttf:gate_one_empty");
        RC_BRAKE = registerSound("bttf:rc_brake");
        PLUTONIUM_CHAMBER = registerSound("bttf:plutonium_chamber");
        PLUTONIUM_CHAMBER_CLOSE = registerSound("bttf:plutonium_chamber_close");
        IDLE = registerSound("bttf:vehicle_idle");

    }

    private static SoundEvent registerSound(String soundIn) {
        ResourceLocation resource = new ResourceLocation(soundIn);
        SoundEvent sound = new SoundEvent(resource).setRegistryName(soundIn);
        return sound;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class RegistryHandler
    {
        @SubscribeEvent
        public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
            IForgeRegistry<SoundEvent> registry = event.getRegistry();
            registry.register(TRAVEL_GATE_ONE);
            registry.register(ENGINE_FORWARD);
            registry.register(ARRIVAL);
            registry.register(REV);
            registry.register(CONNECT);
            registry.register(STARTUP);
            registry.register(TRAVEL_GATE_ONE_EMPTY);
            registry.register(RC_BRAKE);
            registry.register(PLUTONIUM_CHAMBER);
            registry.register(PLUTONIUM_CHAMBER_CLOSE);
            registry.register(IDLE);
        }
    }

}
