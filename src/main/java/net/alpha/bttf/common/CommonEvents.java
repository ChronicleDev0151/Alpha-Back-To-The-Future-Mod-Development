package net.alpha.bttf.common;

import com.google.common.collect.ImmutableList;
import net.alpha.bttf.Reference;
import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class CommonEvents
{
    public static final DataParameter<Boolean> TIME_TRAVEL = EntityDataManager.createKey(EntityVehicle.class, DataSerializers.BOOLEAN);

    private static final List<String> IGNORE_ITEMS;
    private static final List<String> IGNORE_SOUNDS;
    private static final List<String> IGNORE_ENTITIES;

    static
    {
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder = ImmutableList.builder();
        IGNORE_SOUNDS = builder.build();

        IGNORE_ITEMS = builder.build();

        builder = ImmutableList.builder();
        IGNORE_ENTITIES = builder.build();

    }

    @SubscribeEvent
    public void onMissingSound(RegistryEvent.MissingMappings<SoundEvent> event)
    {
        for(RegistryEvent.MissingMappings.Mapping<SoundEvent> missing : event.getMappings())
        {
            if(missing.key.getResourceDomain().equals(Reference.MOD_ID) && IGNORE_SOUNDS.contains(missing.key.getResourcePath()))
            {
                missing.ignore();
            }
        }
    }

    @SubscribeEvent
    public void onMissingItem(RegistryEvent.MissingMappings<Item> event)
    {
        for(RegistryEvent.MissingMappings.Mapping<Item> missing : event.getMappings())
        {
            if(missing.key.getResourceDomain().equals(Reference.MOD_ID) && IGNORE_ITEMS.contains(missing.key.getResourcePath()))
            {
                missing.ignore();
            }
        }
    }





}
