package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityTimeTravelVehicle;
import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTimeTravel  implements IMessage, IMessageHandler<MessageTimeTravel, IMessage>
{

    public EntityVehicle.TimeTravelEnum timeTravelEnum;

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public IMessage onMessage(MessageTimeTravel message, MessageContext ctx) {

        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if(riding instanceof EntityVehicle)
        {
            ((EntityVehicle) riding).setTimeTravelEnum(message.timeTravelEnum);
        }
        return null;
    }
}
