package powercrystals.minefactoryreloaded.tile.base;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.setup.Machine;

public abstract class TileEntityFluidFabricator extends TileEntityFactoryPowered implements IFluidHandler {
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

    @Override
    public boolean allowBucketDrain() {
        return true;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override // IFluidHandler
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override // IFluidHandler
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return (_tank.getFluidAmount() > 0) && (_tank.getFluid().isFluidEqual(new FluidStack(fluid, 0)));
    }

    @Override // IFluidHandler
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return _tank.drain(maxDrain, doDrain);
    }

    @Override // IFluidHandler
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return (resource == null) ? null : _tank.drain(resource.amount, doDrain);
    }

    @Override // IFluidHandler
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { _tank.getInfo() };
    }
}
