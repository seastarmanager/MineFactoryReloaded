package powercrystals.minefactoryreloaded.net;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import powercrystals.core.net.PacketBuilder;
import powercrystals.core.net.PacketHandler;
import powercrystals.core.net.PacketRouter;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;

import java.io.DataInputStream;
import java.io.IOException;

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

    private static class CustomPacketRouter extends PacketRouter {

        public CustomPacketRouter(String channel) {
            super(channel);
        }

        @Override
        public void onCustomPacket(DataInputStream stream, Player player, Side side) throws IOException {
            CustomPacketType type = CustomPacketType.getPacketType(stream.readInt());
            if (!type.isValid())
                return;
            switch (type) {
                case ROAD_BLOCK_UPDATE:
                    if (side.isClient()) {
                        int x = stream.readInt();
                        int y = stream.readInt();
                        int z = stream.readInt();
                        EntityPlayer _player = (EntityPlayer) player;
                        _player.worldObj.setBlock(x, y, z, MineFactoryReloadedCore.factoryRoadBlock.blockID, stream.readInt(), 6);
                        _player.worldObj.markBlockForRenderUpdate(x, y, z);
                    }
                    break;
            }
        }
    }

    public static enum CustomPacketType {
        UNKNOWN,
        ROAD_BLOCK_UPDATE;

        public static CustomPacketType getPacketType(int type) {
            if ((type < 0) || (type >= CustomPacketType.values().length))
                return UNKNOWN;
            return CustomPacketType.values()[type];
        }

        public boolean isValid() {
            return this != UNKNOWN;
        }
    }
}
