package net.alpha.bttf.proxy;

import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.entity.player.EntityPlayer;

public interface Proxy
{

     void preInit();

     void Init();


    void playVehicleSound(EntityPlayer player, EntityVehicle vehicle);


}
