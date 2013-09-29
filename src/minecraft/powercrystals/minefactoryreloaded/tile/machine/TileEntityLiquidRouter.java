package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiLiquidRouter;
import powercrystals.minefactoryreloaded.gui.container.ContainerLiquidRouter;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryInventory;

public class TileEntityLiquidRouter extends TileEntityFactoryInventory implements IFluidHandler {
    private FluidTank[] _bufferTanks = new FluidTank[6];

    public TileEntityLiquidRouter() {
        super(Machine.LiquidRouter);
        for (int i = 0; i < 6; i++)
            _bufferTanks[i] = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        for (FluidTank tank : _bufferTanks) {
            RouteDetector routes = getDrainRoutes(tank.getFluid());
            int distribution = routes.getEqualDistribution();
            for (int i = 0; i < routes.sides.length; i++) {
                if (routes.sides[i]) {
                    ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
                    if (dir == ForgeDirection.WEST || dir == ForgeDirection.EAST)
                        dir = dir.getOpposite();
                    TileEntity te = BlockPosition.getAdjacentTileEntity(this, dir);
                    if (te instanceof IFluidHandler) {
                        FluidStack drained = _bufferTanks[i].drain(distribution, false);
                        int filled = ((IFluidHandler) te).fill(dir.getOpposite(), drained, true);
                        _bufferTanks[i].drain(filled, true);
                    }
                }
            }
        }
    }

    private RouteDetector getDrainRoutes(FluidStack resource) {
        RouteDetector detector = new RouteDetector(resource);
        if (resource == null)
            return detector;
        for (int i = 0; i < getSizeInventory(); i++) {
            if (_bufferTanks[i].getFluidAmount() > 0 && _bufferTanks[i].getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(_inventory[i]))) {
                detector.sides[i] = true;
                detector.availableRoutes++;
            }
        }
        return detector;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || _inventory[from.getOpposite().ordinal()] != null)
            return 0;
        RouteDetector detector = getFillRoutes(resource, from.getOpposite().ordinal());
        FluidStack amount = new FluidStack(resource.fluidID, detector.getEqualDistribution());

        int filled = 0;
        for (int i = 0; i < detector.sides.length; i++) {
            if (detector.sides[i])
                filled += _bufferTanks[i].fill(amount, doFill);
        }

        return filled;
    }

    public RouteDetector getFillRoutes(FluidStack resource, int fromSide) {
        RouteDetector detector = new RouteDetector(resource);
        if (resource == null)
            return detector;
        for (int i = 0; i < getSizeInventory(); i++) {
            FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(_inventory[i]);
            if ((i != fromSide && stack != null) && stack.isFluidEqual(resource)) {
                detector.sides[i] = true;
                detector.availableRoutes++;
            }
        }
        return detector;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    /**
     * Returns true if the given fluid can be inserted into the given direction.
     * <p/>
     * More formally, this should return true if fluid is able to enter from the given direction.
     */
    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    /**
     * Returns true if the given fluid can be extracted from the given direction.
     * <p/>
     * More formally, this should return true if fluid is able to leave from the given direction.
     */
    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection direction) {
        return new FluidTankInfo[]{_bufferTanks[direction.ordinal()].getInfo()};
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public boolean shouldDropSlotWhenBroken(int slot) {
        return false;
    }

    @Override
    public String getGuiBackground() {
        return "liquidrouter.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiLiquidRouter(getContainer(inventoryPlayer), this);
    }

    @Override
    public ContainerLiquidRouter getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerLiquidRouter(this, inventoryPlayer);
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return false;
    }

    private class RouteDetector {
        public final boolean[] sides = new boolean[getSizeInventory()];
        public int availableRoutes = 0;

        private FluidStack resource;

        public RouteDetector(FluidStack resource) {
            this.resource = resource;
        }

        public int getEqualDistribution() {
            return (resource == null) || (availableRoutes <= 0) ? 0 : (resource.amount / availableRoutes);
        }
    }
}
