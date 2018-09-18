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

public class MessageTurn implements IMessage, IMessageHandler<MessageTurn, IMessage>
{
    public EntityVehicle.TurnDirection direction;
    public EntityVehicle vehicle;

    public MessageTurn() {}

    public MessageTurn(EntityVehicle.TurnDirection direction)
    {
        this.direction = direction;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(direction.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
            this.direction = EntityVehicle.TurnDirection.values()[buf.readInt()];
    }

    @Override
    public IMessage onMessage(MessageTurn message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof EntityVehicle)
        {
            ((EntityVehicle) riding).setTurnDirection(message.direction);
        }
        return null;
    }
}
