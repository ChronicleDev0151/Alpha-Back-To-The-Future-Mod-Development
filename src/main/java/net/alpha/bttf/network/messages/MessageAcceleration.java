package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.alpha.bttf.entity.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAcceleration implements IMessage, IMessageHandler<MessageAcceleration, IMessage>
{
    public EntityVehicle.AccelerationDirection acceleration;

    public MessageAcceleration() {}

    public MessageAcceleration(EntityVehicle.AccelerationDirection acceleration)
    {
        this.acceleration = acceleration;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(acceleration.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.acceleration = EntityVehicle.AccelerationDirection.values()[buf.readInt()];
    }

    @Override
    public IMessage onMessage(MessageAcceleration message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof  EntityVehicle)
        {
            ((EntityVehicle) riding).setAcceleration(message.acceleration);
        }

        return null;
    }
}