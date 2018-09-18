package net.alpha.bttf.proxy;

import net.alpha.bttf.Main;
import net.alpha.bttf.entity.EntityVehicle;
import net.alpha.bttf.network.PacketHandler;
import net.alpha.bttf.vehicles.EntityCarOne;
import net.alpha.bttf.vehicles.EntityCarTwo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ServerProxy implements Proxy {

    int nextEntityId;


    @Override
    public void preInit()
    {

    }

    @Override
    public void Init()
    {
    }

    @Override
    public void playVehicleSound(EntityPlayer player, EntityVehicle vehicle) {

    }
}
