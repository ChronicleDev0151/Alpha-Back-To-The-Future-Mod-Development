package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEngine implements IMessage, IMessageHandler<MessageEngine, IMessage> {
    private boolean horn;

    public MessageEngine() {
    }

    public MessageEngine(boolean horn) {
        this.horn = horn;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(horn);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.horn = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(MessageEngine message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        Entity riding = player.getRidingEntity();
        if (riding instanceof EntityVehicle) {
            ((EntityVehicle) riding).setEngineOn(message.horn);
        }
        return null;
    }
}