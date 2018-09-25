package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityDeloreanTwoVehicleFeatures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHover implements IMessage, IMessageHandler<MessageHover, IMessage>
{
    private EntityDeloreanTwoVehicleFeatures.HoverDirection flapDirection;

    public MessageHover() {}

    public MessageHover(EntityDeloreanTwoVehicleFeatures.HoverDirection flapDirection)
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
        this.flapDirection = EntityDeloreanTwoVehicleFeatures.HoverDirection.values()[buf.readInt()];
    }

    @Override
    public IMessage onMessage(MessageHover message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof EntityDeloreanTwoVehicleFeatures)
        {
            ((EntityDeloreanTwoVehicleFeatures) riding).setHoverDirection(message.flapDirection);
        }
        return null;
    }
}