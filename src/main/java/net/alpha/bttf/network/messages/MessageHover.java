package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityTimeTravelConvertableVehicle;
import net.alpha.bttf.entity.EntityTimeTravelHoverVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHover implements IMessage, IMessageHandler<MessageHover, IMessage>
{
    private EntityTimeTravelConvertableVehicle.HoverDirection flapDirection;

    public MessageHover() {}

    public MessageHover(EntityTimeTravelConvertableVehicle.HoverDirection flapDirection)
    {
        this.flapDirection = flapDirection;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(flapDirection.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.flapDirection = EntityTimeTravelConvertableVehicle.HoverDirection.values()[buf.readInt()];
    }

    @Override
    public IMessage onMessage(MessageHover message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof EntityTimeTravelConvertableVehicle)
        {
            ((EntityTimeTravelConvertableVehicle) riding).setFlapDirection(message.flapDirection);
        }
        return null;
    }
}