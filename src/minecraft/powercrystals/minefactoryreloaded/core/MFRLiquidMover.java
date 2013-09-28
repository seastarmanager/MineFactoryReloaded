package powercrystals.minefactoryreloaded.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import powercrystals.core.position.BlockPosition;
import powercrystals.core.util.InventoryUtil;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryInventory;

public abstract class MFRLiquidMover {
    /**
     * Attempts to fill tank with the player's current item.
     *
     * @param    itcb            the tank the fluid is going into
     * @param    entityplayer    the player trying to fill the tank
     * @return True if fluid was transferred to the tank.
     */
    public static boolean manuallyFillTank(IFluidHandler itcb, EntityPlayer entityplayer) {
        ItemStack ci = entityplayer.inventory.getCurrentItem();
        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(ci);
        if (fluid != null) {
            if (itcb.fill(ForgeDirection.UNKNOWN, fluid, false) == fluid.amount) {
                itcb.fill(ForgeDirection.UNKNOWN, fluid, true);
                if (!entityplayer.capabilities.isCreativeMode) {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, InventoryUtil.consumeItem(ci));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to drain tank into the player's current item.
     *
     * @param    tank            the tank the fluid is coming from
     * @param    entityplayer    the player trying to take fluid from the tank
     * @return True if fluid was transferred from the tank.
     */
    public static boolean manuallyDrainTank(IFluidHandler tank, EntityPlayer entityplayer) {
        ItemStack ci = entityplayer.inventory.getCurrentItem();
        if (FluidContainerRegistry.isEmptyContainer(ci)) {
            FluidStack tankFluid = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
            ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(tankFluid, ci);
            if (FluidContainerRegistry.isFilledContainer(filledBucket)) {
                FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(filledBucket);
                if (entityplayer.capabilities.isCreativeMode) {
                    tank.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
                    return true;
                } else if (ci.stackSize == 1) {
                    tank.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filledBucket);
                    return true;
                } else if (entityplayer.inventory.addItemStackToInventory(filledBucket)) {
                    tank.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
                    ci.stackSize -= 1;
                    return true;
                }
            }
        }
        return false;
    }

    public static void pumpFluid(TileEntityFactoryInventory from) {
        if (from.getTank() == null)
            return;
        FluidTank tank = from.getTank();
        if (tank.getFluidAmount() > 0) {
            FluidStack l = tank.getFluid().copy();
            l.amount = Math.min(l.amount, FluidContainerRegistry.BUCKET_VOLUME);
            for (BlockPosition adj : new BlockPosition(from).getAdjacent(true)) {
                TileEntity tile = from.worldObj.getBlockTileEntity(adj.x, adj.y, adj.z);
                if (tile instanceof IFluidHandler) {
                    int filled = ((IFluidHandler) tile).fill(adj.orientation.getOpposite(), l, true);
                    tank.drain(filled, true);

                    l.amount -= filled;
                    if (l.amount <= 0) {
                        break;
                    }
                }
            }
        }
    }
}