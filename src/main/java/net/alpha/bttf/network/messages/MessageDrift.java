package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityDefaultVehicle;
import net.alpha.bttf.entity.EntityDeloreanOneVehicleFeatures;
import net.alpha.bttf.entity.EntityDeloreanTwoVehicleFeatures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDrift implements IMessage, IMessageHandler<MessageDrift, IMessage>
{
    private boolean drifting;

    public MessageDrift() {}

    public MessageDrift(boolean drifting)
    {
        this.drifting = drifting;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(drifting);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.drifting = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(MessageDrift message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof EntityDeloreanOneVehicleFeatures)
        {
            ((EntityDeloreanOneVehicleFeatures) riding).setDrifting(message.drifting);
        }
        if(riding instanceof EntityDeloreanTwoVehicleFeatures)
        {
            ((EntityDeloreanTwoVehicleFeatures) riding).setDrifting(message.drifting);
        }
        if(riding instanceof EntityDefaultVehicle)
        {
            ((EntityDefaultVehicle) riding).setDrifting(message.drifting);
        }
        return null;
    }
}
