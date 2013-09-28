package powercrystals.minefactoryreloaded.net;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercrystals.core.net.PacketBuilder;
import powercrystals.core.net.PacketHandler;
import powercrystals.core.net.PacketRouter;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.entity.EntityRocket;

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
        PacketRouter router = PacketHandler.registerPacketHandler(new CustomPacketRouter(MineFactoryReloadedCore.modNetworkChannel));
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
                case FAKE_SLOT_CHANGE:
                    if (side.isServer()) {
                        int x = stream.readInt();
                        int y = stream.readInt();
                        int z = stream.readInt();
                        int slotNum = stream.readInt();
                        EntityPlayer _player = (EntityPlayer) player;
                        TileEntity te = _player.worldObj.getBlockTileEntity(x, y, z);
                        if (te instanceof ISidedInventory) {
                            ISidedInventory inv = (ISidedInventory) te;
                            ItemStack stack = _player.inventory.getItemStack();
                            if (stack == null)
                                inv.setInventorySlotContents(slotNum, null);
                            else {
                                stack = stack.copy();
                                stack.stackSize = 1;
                                inv.setInventorySlotContents(slotNum, stack);
                            }
                        }
                    }
                    break;
                case ROCKETLAUNCH_WITH_LOCK:
                    if (side.isServer()) {
                        World world = ((EntityPlayer) player).worldObj;
                        Entity owner = world.getEntityByID(stream.readInt());
                        Entity target = null;
                        if (owner instanceof EntityLivingBase) {
                            int id;
                            if ((id = stream.readInt()) != Integer.MIN_VALUE)
                                target = world.getEntityByID(id);
                            EntityRocket r = new EntityRocket(world, ((EntityLivingBase) owner), target);
                            world.spawnEntityInWorld(r);
                        }
                    }
                    break;
            }
        }
    }

    public static enum CustomPacketType {
        UNKNOWN,
        ROAD_BLOCK_UPDATE,
        FAKE_SLOT_CHANGE,
        ROCKETLAUNCH_WITH_LOCK;

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
