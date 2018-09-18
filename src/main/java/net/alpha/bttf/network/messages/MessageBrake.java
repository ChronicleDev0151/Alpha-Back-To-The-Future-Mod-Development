package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBrake implements IMessage, IMessageHandler<MessageBrake, IMessage>
{
    private boolean braking;

    public MessageBrake() {}

    public MessageBrake(boolean braking)
    {
        this.braking = braking;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(braking);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
            this.braking = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(MessageBrake message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof EntityVehicle)
        {
            ((EntityVehicle) riding).setBrakeSystemOn(braking);
        }
        return null;
    }
}
