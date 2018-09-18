package net.alpha.bttf.network.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class MessageVehicleInteract implements IMessage
{

    protected UUID targetEntityUUID;

    public MessageVehicleInteract() {}

    public MessageVehicleInteract(Entity target)
    {
        targetEntityUUID = target.getUniqueID();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, targetEntityUUID.toString());

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        targetEntityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));

    }
}
