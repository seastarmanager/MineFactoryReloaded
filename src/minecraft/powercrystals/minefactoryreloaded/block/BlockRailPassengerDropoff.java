package powercrystals.minefactoryreloaded.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.gui.MFRCreativeTab;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

public class BlockRailPassengerDropoff extends BlockRailBase {
    public BlockRailPassengerDropoff(int blockId) {
        super(blockId, true);
        setUnlocalizedName("mfr.rail.passenger.dropoff");
        setHardness(0.5F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(MFRCreativeTab.tab);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (world.isRemote || !(entity instanceof EntityMinecartEmpty)) {
            return;
        }
        EntityMinecartEmpty minecart = (EntityMinecartEmpty) entity;
        if (minecart.riddenByEntity == null || !(minecart.riddenByEntity instanceof EntityPlayer)) {
            return;
        }

        int[] dropCoords = findSpaceForPlayer(x, y, z, world);
        if (dropCoords[1] < 0) {
            return;
        }
        Entity player = minecart.riddenByEntity;
        player.mountEntity(minecart);
        MineFactoryReloadedCore.proxy.movePlayerToCoordinates((EntityPlayer) player, dropCoords[0] + 0.5, dropCoords[1] + 0.5, dropCoords[2] + 0.5);
    }

    private int[] findSpaceForPlayer(int x, int y, int z, World world) {
        int[] targetCoords = new int[3];
        targetCoords[1] = -1;

        int offsetX;
        int offsetY;
        int offsetZ;

        int targetX;
        int targetY;
        int targetZ;

        for (offsetX = -MFRConfig.getInstance().passengerRailSearchMaxHorizontal; offsetX < MFRConfig.getInstance().passengerRailSearchMaxHorizontal; offsetX++) {
            for (offsetY = -MFRConfig.getInstance().passengerRailSearchMaxVertical; offsetY < MFRConfig.getInstance().passengerRailSearchMaxVertical; offsetY++) {
                for (offsetZ = -MFRConfig.getInstance().passengerRailSearchMaxHorizontal; offsetZ < MFRConfig.getInstance().passengerRailSearchMaxHorizontal; offsetZ++) {
                    targetX = x + offsetX;
                    targetY = y + offsetY;
                    targetZ = z + offsetZ;

                    if (world.getBlockId(targetX, targetY, targetZ) == 0 && world.getBlockId(targetX, targetY + 1, targetZ) == 0
                            && !isBadBlockToStandOn(world.getBlockId(targetX, targetY - 1, targetZ))) {
                        targetCoords[0] = targetX;
                        targetCoords[1] = targetY;
                        targetCoords[2] = targetZ;
                        return targetCoords;
                    }
                }
            }
        }

        return targetCoords;
    }

    private boolean isBadBlockToStandOn(int blockId) {
        if (blockId == 0
                || Block.blocksList[blockId].blockMaterial == Material.lava
                || Block.blocksList[blockId].blockMaterial == Material.water
                || Block.blocksList[blockId].blockMaterial == Material.fire
                || Block.blocksList[blockId] instanceof BlockRailBase) {
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        blockIcon = par1IconRegister.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName());
    }
}
