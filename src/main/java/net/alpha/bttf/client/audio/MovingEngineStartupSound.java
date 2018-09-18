package net.alpha.bttf.client.audio;

import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class MovingEngineStartupSound extends MovingSound {
    private final EntityVehicle vehicle;

    public MovingEngineStartupSound(EntityVehicle vehicle) {
        super(vehicle.getEngineStartupSound(), SoundCategory.NEUTRAL);
        this.vehicle = vehicle;
        this.repeat = false;
        this.repeatDelay = 0;
        this.volume = 0.006F;
    }

    @Override
    public void update()
    {
        this.volume = 0.8F;
        if(!vehicle.isDead && vehicle.getControllingPassenger() != null && vehicle.getControllingPassenger() != Minecraft.getMinecraft().player)
        {
            EntityPlayer localPlayer = Minecraft.getMinecraft().player;
            this.xPosF = (float) (vehicle.posX + (localPlayer.posX - vehicle.posX) * 0.65);
            this.yPosF = (float) (vehicle.posY + (localPlayer.posY - vehicle.posY) * 0.65);
            this.zPosF = (float) (vehicle.posZ + (localPlayer.posZ - vehicle.posZ) * 0.65);
        }
        else
        {
            this.donePlaying = true;
        }
    }
}