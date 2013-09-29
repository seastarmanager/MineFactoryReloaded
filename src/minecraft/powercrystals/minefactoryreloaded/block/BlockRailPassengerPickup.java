package powercrystals.minefactoryreloaded.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.gui.MFRCreativeTab;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

import java.util.List;

public class BlockRailPassengerPickup extends BlockRailBase {
    public BlockRailPassengerPickup(int blockId) {
        super(blockId, true);
        setUnlocalizedName("mfr.rail.passenger.pickup");
        setHardness(0.5F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(MFRCreativeTab.tab);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (world.isRemote || !(entity instanceof EntityMinecartEmpty)) {
            return;
        }
        EntityMinecart minecart = (EntityMinecart) entity;
        if (minecart.riddenByEntity != null) {
            return;
        }

        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
                x - MFRConfig.getInstance().passengerRailSearchMaxHorizontal,
                y - MFRConfig.getInstance().passengerRailSearchMaxVertical,
                z - MFRConfig.getInstance().passengerRailSearchMaxHorizontal,
                x + MFRConfig.getInstance().passengerRailSearchMaxHorizontal + 1,
                y + MFRConfig.getInstance().passengerRailSearchMaxVertical + 1,
                z + MFRConfig.getInstance().passengerRailSearchMaxHorizontal + 1);

        @SuppressWarnings("rawtypes")
        List entities = world.getEntitiesWithinAABB(EntityPlayer.class, bb);

        for (Object o : entities) {
            if (!(o instanceof EntityPlayer)) {
                continue;
            }
            ((EntityPlayer) o).mountEntity(minecart);
            return;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        blockIcon = par1IconRegister.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName());
    }
}
