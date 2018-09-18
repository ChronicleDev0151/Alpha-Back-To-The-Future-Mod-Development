package net.alpha.bttf;

import net.alpha.bttf.client.ClientEvent;
import net.alpha.bttf.entity.EntityVehicle;
import net.alpha.bttf.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class RemoteMovementInput extends MovementInput {
    private MovementInput original;

    public RemoteMovementInput(MovementInput original) {
        this.original = original;
    }

    @Override
    public void updatePlayerMoveState() {
        original.updatePlayerMoveState();

        this.jump = original.jump;
        this.sneak = original.sneak;

        if (EntityVehicle.remoteControlling == true) {
            this.moveForward = 0;
            this.moveStrafe = 0;

            Minecraft mc = Minecraft.getMinecraft();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
        } else {
            this.moveForward = original.moveForward;
            this.moveStrafe = original.moveStrafe;
        }
    }
}
