package powercrystals.minefactoryreloaded.tile.base;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.core.asm.relauncher.Implementable;
import powercrystals.core.util.Util;
import powercrystals.core.util.UtilInventory;
import powercrystals.minefactoryreloaded.modhelpers.ic2.IC2;
import powercrystals.minefactoryreloaded.setup.Machine;

import java.util.ArrayList;
import java.util.List;

/*
 * There are three pieces of information tracked - energy, work, and idle ticks.
 * 
 * Energy is stored and used when the machine activates. The energy stored must be >= energyActivation for the activateMachine() method to be called.
 * If activateMachine() returns true, energy will be drained.
 * 
 * Work is built up and then when at 100% something happens. This is tracked/used entirely by the derived class. If not used (f.ex. harvester), return max 1.
 * 
 * Idle ticks cause an artificial delay before activateMachine() is called again. Max should be the highest value the machine will use, to draw the
 * progress bar correctly.
 */

@Implementable("ic2.api.energy.tile.IEnergySink")
public abstract class TileEntityFactoryPowered extends TileEntityFactoryInventory implements IPowerReceptor {
    public static final int energyPerEU = 4;
    public static final int energyPerMJ = 10;
    public static final int wPerEnergy = 7;

    private int _energyStored;
    protected int _energyActivation;

    protected int _energyRequiredThisTick = 0;

    private int _workDone;

    private int _idleTicks;

    protected List<ItemStack> failedDrops = null;
    private List<ItemStack> missedDrops = new ArrayList<ItemStack>();

    protected int _failedDropTicksMax = 20;
    private int _failedDropTicks = 0;

    // buildcraft-related fields

    private PowerHandler _powerProvider;

    // IC2-related fields

    private boolean _isAddedToIC2EnergyNet;
    private boolean _addToNetOnNextTick;

    // UE-related fields

    private int _ueBuffer;

    // constructors

    protected TileEntityFactoryPowered(Machine machine) {
        this(machine, machine.getActivationEnergyMJ());
    }

    protected TileEntityFactoryPowered(Machine machine, int activationCostMJ) {
        super(machine);
        _energyActivation = activationCostMJ * energyPerMJ;
        _powerProvider = new PowerHandler(this, PowerHandler.Type.MACHINE);
        configurePowerProvider();
        setIsActive(false);
    }

    // local methods

    private void configurePowerProvider() { // TODO: inline into constructor in 2.8
        int activation = getMaxEnergyPerTick() / energyPerMJ;
        int maxReceived = Math.min(activation * 20, 1000);
        _powerProvider.configure(activation < 10 ? 1F : 10F, maxReceived, 1F, 1000F);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        _energyStored = Math.min(_energyStored, getEnergyStoredMax());

        if (worldObj.isRemote)
            return;

        if (_addToNetOnNextTick) {
            IC2.postTileLoadEvent(this);
            _addToNetOnNextTick = false;
            _isAddedToIC2EnergyNet = true;
        }

        int energyRequired = Math.min(getEnergyStoredMax() - getEnergyStored(), getMaxEnergyPerTick());

        if (energyRequired > 0) {
            PowerHandler pp = getPowerProvider();
            bcpower:
            if (pp != null) {
                int mjRequired = energyRequired / energyPerMJ;
                if (mjRequired <= 0) break bcpower;

                pp.update();

                if (pp.useEnergy(0, mjRequired, false) > 0) {
                    int mjGained = (int) (pp.useEnergy(0, mjRequired, true) * energyPerMJ);
                    _energyStored += mjGained;
                    energyRequired -= mjGained;
                }
            }

            int energyFromUE = Math.min(_ueBuffer / wPerEnergy, energyRequired);
            _energyStored += energyFromUE;
            energyRequired -= energyFromUE;
            _ueBuffer -= (energyFromUE * wPerEnergy);
        }

        _energyRequiredThisTick = energyRequired;

        setIsActive(_energyStored >= _energyActivation * 2);

        if (failedDrops != null) {
            if (_failedDropTicks < _failedDropTicksMax) {
                _failedDropTicks++;
                return;
            }
            _failedDropTicks = 0;
            if (!doDrop(failedDrops)) {
                setIdleTicks(getIdleTicksMax());
                return;
            }
            failedDrops = null;
        }

        if (Util.isRedstonePowered(this)) {
            setIdleTicks(getIdleTicksMax());
        } else if (_idleTicks > 0) {
            _idleTicks--;
        } else if (_energyStored >= _energyActivation) {
            if (activateMachine()) {
                _energyStored -= _energyActivation;
            }
        }
    }

    public boolean doDrop(ItemStack drop) {
        drop = UtilInventory.dropStack(this, drop, this.getDropDirection());
        if (drop != null && drop.stackSize > 0) {
            if (failedDrops == null) {
                failedDrops = new ArrayList<ItemStack>();
            }
            failedDrops.add(drop);
            return false;
        }
        return true;
    }

    public boolean doDrop(List<ItemStack> drops) {
        if (drops == null || drops.size() <= 0) {
            return true;
        }
        List<ItemStack> missed = missedDrops;
        missed.clear();
        for (int i = drops.size(); i-- > 0; ) {
            ItemStack dropStack = drops.get(i);
            dropStack = UtilInventory.dropStack(this, dropStack, this.getDropDirection());
            if (dropStack != null && dropStack.stackSize > 0) {
                missed.add(dropStack);
            }
        }

        if (missed.size() != 0) {
            if (drops != failedDrops) {
                if (failedDrops == null) {
                    failedDrops = new ArrayList<ItemStack>();
                }
                failedDrops.addAll(missed);
            } else {
                failedDrops.clear();
                failedDrops.addAll(missed);
            }
            return false;
        }

        return true;
    }

    @Override
    public void validate() {
        super.validate();
        if (!_isAddedToIC2EnergyNet) {
            _addToNetOnNextTick = true;
        }
    }

    @Override
    public void invalidate() {
        if (_isAddedToIC2EnergyNet) {
            if (!worldObj.isRemote) {
                IC2.postTileUnloadEvent(this);
            }
            _isAddedToIC2EnergyNet = false;
        }
        super.invalidate();
    }

    protected abstract boolean activateMachine();

    @Override
    public void onBlockBroken() {
        super.onBlockBroken();
        if (_isAddedToIC2EnergyNet) {
            _isAddedToIC2EnergyNet = false;
            IC2.postTileUnloadEvent(this);
        }
    }

    public int getMaxEnergyPerTick() {
        return _energyActivation;
    }

    public int getEnergyStored() {
        return _energyStored;
    }

    public abstract int getEnergyStoredMax();

    public void setEnergyStored(int energy) {
        _energyStored = energy;
    }

    public int getWorkDone() {
        return _workDone;
    }

    public abstract int getWorkMax();

    public void setWorkDone(int work) {
        _workDone = work;
    }

    public int getIdleTicks() {
        return _idleTicks;
    }

    public abstract int getIdleTicksMax();

    public void setIdleTicks(int ticks) {
        _idleTicks = ticks;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("energyStored", _energyStored);
        tag.setInteger("workDone", _workDone);
        tag.setInteger("ueBuffer", _ueBuffer);
        NBTTagCompound pp = new NBTTagCompound();
        _powerProvider.writeToNBT(pp);
        tag.setCompoundTag("powerProvider", pp);

        if (failedDrops != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (ItemStack item : failedDrops) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                item.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
            tag.setTag("DropItems", nbttaglist);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        _energyStored = Math.min(tag.getInteger("energyStored"), getEnergyStoredMax());
        _workDone = Math.min(tag.getInteger("workDone"), getWorkMax());
        _ueBuffer = tag.getInteger("ueBuffer");
        _powerProvider.readFromNBT(tag.getCompoundTag("powerProvider"));

        if (tag.hasKey("DropItems")) {
            List<ItemStack> drops = new ArrayList<ItemStack>();
            NBTTagList nbttaglist = tag.getTagList("DropItems");
            for (int i = nbttaglist.tagCount(); i-- > 0; ) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
                ItemStack item = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                if (item != null && item.stackSize > 0) {
                    drops.add(item);
                }
            }
            if (drops.size() != 0) {
                failedDrops = drops;
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack s) {
        return true;
    }

    public int getEnergyRequired() {
        return Math.min(getEnergyStoredMax() - getEnergyStored(), _energyRequiredThisTick);
    }

    // BC methods

    public PowerHandler getPowerProvider() {
        return _powerProvider;
    }

    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection from) {
        return _powerProvider.getPowerReceiver();
    }

    @Override
    public void doWork(PowerHandler handler) {
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    // IC2 methods

    /* IEnergySink */
    public double demandedEnergyUnits() {
        return Math.max(getEnergyRequired() / energyPerEU, 0);
    }

    /* IEnergySink */
    @SuppressWarnings("UnusedDeclaration")
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
        double euInjected = Math.max(Math.min(demandedEnergyUnits(), amount), 0);
        double energyInjected = euInjected * energyPerEU;
        _energyStored += energyInjected;
        _energyRequiredThisTick -= energyInjected;
        return amount - euInjected;
    }

    /* IEnergySink */
    @SuppressWarnings("UnusedDeclaration")
    public boolean acceptsEnergyFrom(TileEntity entity, ForgeDirection from) {
        return true;
    }

    /* IEnergySink */
    @SuppressWarnings("UnusedDeclaration")
    public int getMaxSafeInput() {
        return 128;
    }


}
