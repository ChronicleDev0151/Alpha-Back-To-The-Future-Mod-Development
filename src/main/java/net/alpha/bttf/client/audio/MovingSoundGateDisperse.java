package net.alpha.bttf.client.audio;

import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class MovingSoundGateDisperse extends MovingSound {
    private final EntityPlayer player;
    private final EntityVehicle vehicle;
  //  private final EntityTimeTravelGate gate;

    public MovingSoundGateDisperse(EntityPlayer player, EntityVehicle vehicle /* EntityTimeTravelGate gate */) {
        super(vehicle.getMovingSound(), SoundCategory.NEUTRAL);
      /*  this.gate = gate; */
        this.player = player;
        this.vehicle = vehicle;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = false;
        this.repeatDelay = 0;
        this.volume = 0.001F;
    }

    @Override
    public void update() {
        this.volume = 0.8F;
        if (!vehicle.isDead && player.isRiding() && player.getRidingEntity() == vehicle && player == Minecraft.getMinecraft().player /* && gate.hasDispersed */) {
            //    this.pitch = gate.playSound(ModSounds.DISPERSAL);
        } else {
            this.donePlaying = true;
        }
    }
}
