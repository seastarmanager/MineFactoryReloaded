package powercrystals.minefactoryreloaded.tile.base;

import buildcraft.api.power.PowerHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.core.util.Util;
import powercrystals.minefactoryreloaded.setup.Machine;

public abstract class TileEntityLiquidGenerator extends TileEntityGenerator implements IFluidHandler {
    private int _fluidConsumedPerTick;
    private int _powerProducedPerConsumption;
    private int _ticksBetweenConsumption;
    private int _outputPulseSize;

    private int _ticksSinceLastConsumption = 0;
    private int _bufferMax = 1000;
    private int _buffer;

    private FluidTank _tank;

    public TileEntityLiquidGenerator(Machine machine, int fluidConsumedPerTick, int powerProducedPerConsumption, int ticksBetweenConsumption) {
        super(machine);
        _fluidConsumedPerTick = fluidConsumedPerTick;
        _powerProducedPerConsumption = powerProducedPerConsumption;
        _ticksBetweenConsumption = ticksBetweenConsumption;
        _outputPulseSize = machine.getActivationEnergyMJ() * TileEntityFactoryPowered.energyPerMJ;

        _tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
    }

    protected abstract FluidStack getFluidType();

    public int getBuffer() {
        return _buffer;
    }

    public void setBuffer(int buffer) {
        _buffer = buffer;
    }

    public int getBufferMax() {
        return _bufferMax;
    }

    @Override
    public FluidTank getTank() {
        return _tank;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            setIsActive(_buffer > _outputPulseSize * 2);

            int mjPulse = Math.min(_buffer, _outputPulseSize);
            _buffer -= mjPulse;
            _buffer += producePower(mjPulse);

            if (_ticksSinceLastConsumption < _ticksBetweenConsumption) {
                _ticksSinceLastConsumption++;
                return;
            }
            _ticksSinceLastConsumption = 0;

            if (Util.isRedstonePowered(this)) {
                return;
            }

            if (_tank.getFluid() == null || _tank.getFluid().amount < _fluidConsumedPerTick || _bufferMax - _buffer < _powerProducedPerConsumption) {
                return;
            }

            _tank.drain(_fluidConsumedPerTick, true);
            _buffer += _powerProducedPerConsumption;
        }
    }

    @Override
    public boolean allowBucketFill() {
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("ticksSinceLastConsumption", _ticksSinceLastConsumption);
        nbttagcompound.setInteger("buffer", _buffer);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        _ticksSinceLastConsumption = nbttagcompound.getInteger("ticksSinceLastConsumption");
        _buffer = nbttagcompound.getInteger("buffer");
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection from) {
        return getPowerProvider().getPowerReceiver();
    }

    /**
     * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param from     Orientation the Fluid is pumped in from.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param doFill   If false, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if ((resource == null) || (!getFluidType().isFluidEqual(resource)))
            return 0;
        return _tank.fill(resource, doFill);
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param from     Orientation the Fluid is drained to.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param doDrain  If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * <p/>
     * This method is not Fluid-sensitive.
     *
     * @param from     Orientation the fluid is drained to.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain  If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
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
        return (fluid != null) && (getFluidType().fluidID == fluid.getID());
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

    /**
     * Returns an array of objects which represent the internal tanks. These objects cannot be used
     * to manipulate the internal tanks. See {@link net.minecraftforge.fluids.FluidTankInfo}.
     *
     * @param from Orientation determining which tanks should be queried.
     * @return Info for the relevant internal tanks.
     */
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { _tank.getInfo() };
    }

}
