package powercrystals.minefactoryreloaded.net;

import powercrystals.core.net.PacketBuilder;
import powercrystals.core.net.PacketHandler;
import powercrystals.core.net.PacketRouter;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;

/**
 * @author samrg472
 */
public final class NetworkHandler {

    private static PacketBuilder builder;

    private NetworkHandler() {}

    public static void init() {
        if (builder != null)
            return;
        PacketRouter router = PacketHandler.registerPacketHandler(MineFactoryReloadedCore.modNetworkChannel);
        builder = new PacketBuilder(router);
    }

    public static String getName() {
        return builder.channel;
    }

    public static PacketBuilder getBuilder() {
        return builder;
    }
}
