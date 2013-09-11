package powercrystals.minefactoryreloaded.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.core.position.BlockPosition;
import powercrystals.core.util.UtilInventory;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactory;

public abstract class MFRLiquidMover {
    /**
     * Attempts to fill tank with the player's current item.
     *
     * @param    itcb            the tank the fluid is going into
     * @param    entityplayer    the player trying to fill the tank
     * @return True if fluid was transferred to the tank.
     */
    public static boolean manuallyFillTank(IFluidContainerItem itcb, EntityPlayer entityplayer) {
        ItemStack ci = entityplayer.inventory.getCurrentItem();
        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(ci);
        if (fluid != null) {
            if (itcb.fill(ci, fluid, false) == fluid.amount) {
                itcb.fill(ci, fluid, true);
                if (!entityplayer.capabilities.isCreativeMode) {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, UtilInventory.consumeItem(ci));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to drain tank into the player's current item.
     *
     * @param    itcb            the tank the fluid is coming from
     * @param    entityplayer    the player trying to take fluid from the tank
     * @return True if fluid was transferred from the tank.
     */
    public static boolean manuallyDrainTank(IFluidTank tank, EntityPlayer entityplayer) {
        ItemStack ci = entityplayer.inventory.getCurrentItem();
        if (FluidContainerRegistry.isEmptyContainer(ci)) {
            FluidStack tankFluid = tank.getFluid();
            ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(tankFluid, ci);
            if (FluidContainerRegistry.isFilledContainer(filledBucket)) {
                FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(filledBucket);
                if (entityplayer.capabilities.isCreativeMode) {
                    tank.drain(fluid.amount, true);
                    return true;
                } else if (ci.stackSize == 1) {
                    tank.drain(fluid.amount, true);
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filledBucket);
                    return true;
                } else if (entityplayer.inventory.addItemStackToInventory(filledBucket)) {
                    tank.drain(fluid.amount, true);
                    ci.stackSize -= 1;
                    return true;
                }
            }
        }
        return false;
    }

    public static void pumpFluid(IFluidTank tank, TileEntityFactory from) {
        if (tank != null && tank.getFluidAmount() > 0) {
            FluidStack l = tank.getFluid().copy();
            l.amount = Math.min(l.amount, FluidContainerRegistry.BUCKET_VOLUME);
            for (BlockPosition adj : new BlockPosition(from).getAdjacent(true)) {
                TileEntity tile = from.worldObj.getBlockTileEntity(adj.x, adj.y, adj.z);
                TankClassWrapper wrapper = TankClassWrapper.newInstance(adj.orientation.getOpposite(), tile);
                if (wrapper != null) {
                    int filled = wrapper.fill(l, true);
                    tank.drain(filled, true);

                    l.amount -= filled;
                    if (l.amount <= 0) {
                        break;
                    }
                }
            }
        }
    }

    private static class TankClassWrapper {

        private ForgeDirection direction;
        private boolean useTankA;

        private IFluidTank tankA;
        private IFluidHandler tankB;

        public static TankClassWrapper newInstance(ForgeDirection dir, TileEntity tile) {
            if (tile instanceof IFluidTank)
                return new TankClassWrapper((IFluidTank) tile);
            else if (tile instanceof IFluidHandler)
                return new TankClassWrapper(dir, (IFluidHandler) tile);
            return null;
        }

        public TankClassWrapper(IFluidTank tank) {
            tankA = tank;
            useTankA = true;
        }

        public TankClassWrapper(ForgeDirection direction, IFluidHandler tank) {
            this.direction = direction;
            tankB = tank;
            useTankA = false;
        }

        public int fill(FluidStack resource, boolean doFill) {
            if (useTankA)
                return tankA.fill(resource, doFill);
            return tankB.fill(direction, resource, doFill);
        }
    }

}