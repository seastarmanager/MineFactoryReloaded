package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.core.util.Util;
import powercrystals.core.util.UtilInventory;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.gui.client.GuiBioReactor;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.container.ContainerBioReactor;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryInventory;

public class TileEntityBioReactor extends TileEntityFactoryInventory implements IFluidContainerItem, IFluidTank
{
	private FluidTank _tank;
	private int _burnTime;
	private static final int _burnTimeMax = 8000;
	private static final int _bioFuelPerTick = 1;
	private static final int _burnTimeDecreasePerTick = 1;
	
	// start at 0 for 0 slots; increase by 5, then an additional 10 each time (upward-sloping curve)
	private static final int[] _outputValues = { 0, 5, 25, 70, 150, 275, 455, 700, 1020, 1425 };
	
	public TileEntityBioReactor()
	{
		super(Machine.BioReactor);
		_tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
	}
	
	public int getBurnTime()
	{
		return _burnTime;
	}
	
	public void setBurnTime(int burnTime)
	{
		_burnTime = burnTime;
	}
	
	public int getBurnTimeMax()
	{
		return _burnTimeMax;
	}
	
	public int getOutputValue()
	{
		int occupiedSlots = 0;
		for(int i = 9; i < 18; i++)
		{
			if(_inventory[i] != null)
			{
				occupiedSlots++;
			}
		}
		
		return _outputValues[occupiedSlots];
	}
	
	public int getOutputValueMax()
	{
		return _outputValues[9];
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(!worldObj.isRemote)
		{
			for(int i = 0; i < 9; i++)
			{
				if(_inventory[i] != null && MFRRegistry.getPlantables().containsKey(_inventory[i].itemID))
				{
					int targetSlot = findMatchingSlot(_inventory[i]);
					if(targetSlot < 0)
					{
						continue;
					}
					
					if(_inventory[targetSlot] == null)
					{
						_inventory[targetSlot] = _inventory[i];
						_inventory[i] = null;
					}
					else
					{
						UtilInventory.mergeStacks(_inventory[targetSlot], _inventory[i]);
						if(_inventory[i].stackSize <= 0)
						{
							_inventory[i] = null;
						}
					}
				}
			}
			
			if(Util.isRedstonePowered(this))
			{
				return;
			}
			
			int newBurn = getOutputValue();
			if(_burnTimeMax - _burnTime >= newBurn)
			{
				_burnTime += newBurn;
				for(int i = 9; i < 18; i++)
				{
					if(_inventory[i] != null)
					{
						decrStackSize(i, 1);
					}
				}
			}
			
			if(_burnTime > 0 && (_tank.getFluid() == null || _tank.getFluidAmount() <= _tank.getCapacity() - _bioFuelPerTick))
			{
				_burnTime -= _burnTimeDecreasePerTick;
                _tank.fill(new FluidStack(MineFactoryReloadedCore.biofuelLiquid.getFluid(), _bioFuelPerTick), true);
			}
		}
	}
	
	private int findMatchingSlot(ItemStack s)
	{
		for(int i = 9; i < 18; i++)
		{
			if(_inventory[i] != null && _inventory[i].itemID == s.itemID && _inventory[i].getItemDamage() == s.getItemDamage())
			{
				return i;
			}
		}
		return findEmptySlot();
	}
	
	private int findEmptySlot()
	{
		for(int i = 9; i < 18; i++)
		{
			if(_inventory[i] == null)
			{
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public String getGuiBackground()
	{
		return "bioreactor.png";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer)
	{
		return new GuiBioReactor(getContainer(inventoryPlayer), this);
	}
	
	@Override
	public ContainerBioReactor getContainer(InventoryPlayer inventoryPlayer)
	{
		return new ContainerBioReactor(this, inventoryPlayer);
	}
	
	@Override
	public FluidTank getTank()
	{
		return _tank;
	}
	
	@Override
	protected boolean shouldPumpFluid()
	{
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 18;
	}
	
	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		return 0;
	}
	
	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 9;
	}
	
	@Override
	public boolean allowBucketDrain()
	{
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("burnTime", _burnTime);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		_burnTime = nbttagcompound.getInteger("burnTime");
	}
	
	@Override
	public boolean isStackValidForSlot(int slot, ItemStack itemstack)
	{
		if(itemstack == null)
		{
			return false;
		}
		for(int i = 0; i < 9; i++)
		{
			if(i != slot && _inventory[i] != null && UtilInventory.stacksEqual(_inventory[i], itemstack))
			{
				return false;
			}
		}
		return true;
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
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack attempting to fill the container.
     * @param doFill    If false, the fill will only be simulated.
     * @return Amount of fluid that was (or would have been, if simulated) filled into the
     * container.
     */
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        return 0;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param maxDrain  Maximum amount of fluid to be removed from the container.
     * @param doFill    If false, the drain will only be simulated.
     * @return Amount of fluid that was (or would have been, if simulated) drained from the
     * container.
     */
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return null;
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
     * Should prevent manipulation of the IFluidTank. See {@link net.minecraftforge.fluids.FluidTank}.
     *
     * @return State information for the IFluidTank.
     */
    @Override
    public FluidTankInfo getInfo() {
        return _tank.getInfo();
    }

    /**
     * @param resource FluidStack attempting to fill the tank.
     * @param doFill   If false, the fill will only be simulated.
     * @return Amount of fluid that was accepted by the tank.
     */
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    /**
     * @param maxDrain Maximum amount of fluid to be removed from the container.
     * @param doFill   If false, the fill will only be simulated.
     * @return Amount of fluid that was removed from the tank.
     */
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }
}
