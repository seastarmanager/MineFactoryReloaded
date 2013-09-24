package powercrystals.minefactoryreloaded.item;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.core.net.PacketHandler;
import powercrystals.minefactoryreloaded.MineFactoryReloadedClient;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.entity.EntityRocket;
import powercrystals.minefactoryreloaded.net.NetworkHandler;

public class ItemRocketLauncher extends ItemFactory {
    public ItemRocketLauncher(int id) {
        super(id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.inventory.hasItem(MineFactoryReloadedCore.rocketItem.itemID)) {
            player.inventory.consumeInventoryItem(MineFactoryReloadedCore.rocketItem.itemID);

            if (world.isRemote) {
                PacketDispatcher.sendPacketToServer(NetworkHandler.getBuilder().startBuild(PacketHandler.PacketType.CUSTOM)
                        .append(player.entityId, MineFactoryReloadedClient.instance.getLockedEntity()).build());
                //PacketDispatcher.sendPacketToServer(PacketWrapper.createPacket(MineFactoryReloadedCore.modNetworkChannel, Packets.RocketLaunchWithLock, new Object[]
                //        {player.entityId, MineFactoryReloadedClient.instance.getLockedEntity()}));
            }
            if (!player.worldObj.isRemote) {
                EntityRocket rocket = new EntityRocket(world, player);
                world.spawnEntityInWorld(rocket);
            }
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
    }
}
