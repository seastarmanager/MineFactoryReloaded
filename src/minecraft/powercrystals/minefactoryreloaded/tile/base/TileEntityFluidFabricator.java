package powercrystals.minefactoryreloaded.tile.base;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.setup.Machine;

public abstract class TileEntityFluidFabricator extends TileEntityFactoryPowered implements IFluidContainerItem, IFluidTank {
    private int _fluidId;
    private int _fluidFabPerTick;

    private FluidTank _tank;

    protected TileEntityFluidFabricator(int fluidId, int fluidFabPerTick, Machine machine) {
        super(machine, machine.getActivationEnergyMJ() * fluidFabPerTick);
        _fluidId = fluidId;
        _fluidFabPerTick = fluidFabPerTick;
        _tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    protected boolean activateMachine() {
        if (_fluidId < 0) {
            setIdleTicks(getIdleTicksMax());
            return false;
        }

        if (_tank.getFluid() != null && _tank.getCapacity() - _tank.getFluidAmount() < _fluidFabPerTick)
            return false;

        _tank.fill(new FluidStack(_fluidId, _fluidFabPerTick), true);
        return true;
    }

    @Override
    public int getEnergyStoredMax() {
        return 16000;
    }

    @Override
    public int getWorkMax() {
        return 0;
    }

    @Override
    public int getIdleTicksMax() {
        return 200;
    }

    @Override
    public FluidTank getTank() {
        return _tank;
    }

    @Override
    protected boolean shouldPumpFluid() {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override // IFluidTank
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override // IFluidContainerItem
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public boolean allowBucketDrain() {
        return true;
    }

    @Override // IFluidTank
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    @Override // IFluidContainerItem
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return null;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return FluidStack representing the fluid in the container, null if the container is empty.
     */
    @Override
    public FluidStack getFluid(ItemStack container) {
        return _tank.getFluid();
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return Capacity of this fluid container.
     */
    @Override
    public int getCapacity(ItemStack container) {
        return _tank.getCapacity();
    }

    /**
     * @return FluidStack representing the fluid in the tank, null if the tank is empty.
     */
    @Override
    public FluidStack getFluid() {
        return _tank.getFluid();
    }

    /**
     * @return Current amount of fluid in the tank.
     */
    @Override
    public int getFluidAmount() {
        return _tank.getFluidAmount();
    }

    /**
     * @return Capacity of this fluid tank.
     */
    @Override
    public int getCapacity() {
        return _tank.getCapacity();
    }

    /**
     * Returns a wrapper object {@link net.minecraftforge.fluids.FluidTankInfo } containing the capacity of the tank and the
     * FluidStack it holds.
     * <p/>
     * Should prevent manipulation of the IFluidTank. See {@link FluidTank}.
     *
     * @return State information for the IFluidTank.
     */
    @Override
    public FluidTankInfo getInfo() {
        return _tank.getInfo();
    }
}
