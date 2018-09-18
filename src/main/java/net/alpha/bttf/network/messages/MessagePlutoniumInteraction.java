package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlutoniumInteraction extends MessageVehicleInteract implements IMessageHandler<MessagePlutoniumInteraction, IMessage>
{
    private EnumHand hand;

    public MessagePlutoniumInteraction(EntityPlayer player, EnumHand hand, Entity target)
    {
        super(target);
        this.hand = hand;
        this.powerDeloreanViaPlutonium(player, hand, target);
    }

    private void powerDeloreanViaPlutonium(EntityPlayer player, EnumHand hand, Entity target)
    {
        if(target instanceof EntityVehicle)
        {
            ((EntityVehicle) target).powerViaPlutonium(player, hand);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(hand.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        hand = EnumHand.values()[buf.readInt()];
    }

    @Override
    public IMessage onMessage(MessagePlutoniumInteraction message, MessageContext ctx) {

        EntityPlayer player = ctx.getServerHandler().player;
        MinecraftServer server = player.world.getMinecraftServer();
        if(server != null)
        {
            server.addScheduledTask(() ->
            {
                Entity targetEntity = server.getEntityFromUuid(message.targetEntityUUID);
                if (targetEntity != null)
                {
                    powerDeloreanViaPlutonium(player, message.hand, targetEntity);
                }
            });
        }

        return null;
    }
}
