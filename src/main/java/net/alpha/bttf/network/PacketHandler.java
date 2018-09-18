package net.alpha.bttf.network;

import net.alpha.bttf.Reference;
import net.alpha.bttf.network.messages.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void init()
    {
        INSTANCE.registerMessage(MessageAcceleration.class, MessageAcceleration.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageTurn.class, MessageTurn.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessageHover.class, MessageHover.class, 3, Side.SERVER);
        INSTANCE.registerMessage(MessageBrake.class, MessageBrake.class, 4, Side.SERVER);
        INSTANCE.registerMessage(MessageTimeTravel.class, MessageTimeTravel.class, 5, Side.SERVER);
        INSTANCE.registerMessage(MessagePlutoniumInteraction.class, MessagePlutoniumInteraction.class, 6, Side.SERVER);
    }



}
