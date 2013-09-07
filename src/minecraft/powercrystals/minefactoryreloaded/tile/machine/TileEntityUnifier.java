package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.core.oredict.OreDictTracker;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiUnifier;
import powercrystals.minefactoryreloaded.gui.container.ContainerUnifier;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityUnifier extends TileEntityFactoryInventory implements IFluidContainerItem, IFluidTank
{
	private FluidTank _tank;
	
	private static FluidStack _biofuel;
	private static FluidStack _ethanol;
	private static FluidStack _essence;
	private static FluidStack _fluidxp;
	private int _roundingCompensation;
	
	private Map<String, ItemStack> _preferredOutputs = new HashMap<String, ItemStack>();
	
	public TileEntityUnifier()
	{
		super(Machine.Unifier);
		_tank = new FluidTank(4 * FluidContainerRegistry.BUCKET_VOLUME);
		_roundingCompensation = 1;
	}

	public static void updateUnifierFluids()
	{
		_biofuel = FluidRegistry.getFluidStack("biofuel", 1);
		_ethanol = FluidRegistry.getFluidStack("ethanol", 1);
		_essence = FluidRegistry.getFluidStack("essence", 1);
		_fluidxp = FluidRegistry.getFluidStack("immibis.liquidxp", 1);
    }
	
	@Override
	public String getGuiBackground()
	{
		return "unifier.png";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer)
	{
		return new GuiUnifier(getContainer(inventoryPlayer), this);
	}
	
	@Override
	public ContainerUnifier getContainer(InventoryPlayer inventoryPlayer)
	{
		return new ContainerUnifier(this, inventoryPlayer);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!worldObj.isRemote)
		{
			ItemStack output;
			if(_inventory[0] != null)
			{
				List<String> names = OreDictTracker.getNamesFromItem(_inventory[0]);
				
				if(names == null || names.size() != 1)
				{
					output = _inventory[0].copy();
				}
				else if(_preferredOutputs.containsKey(names.get(0)))
				{
					output = _preferredOutputs.get(names.get(0)).copy();
					output.stackSize = _inventory[0].stackSize;
				}
				else
				{
					output = OreDictionary.getOres(names.get(0)).get(0).copy();
					output.stackSize = _inventory[0].stackSize;
				}
				
				moveItemStack(output);
			}
		}
	}
	
	private void moveItemStack(ItemStack source)
	{
		if(source == null)
		{
			return;
		}
		
		int amt;
		
		if(_inventory[1] == null)
		{
			amt = Math.min(getInventoryStackLimit(), source.getMaxStackSize());
		}
		else if(source.itemID != _inventory[1].itemID || source.getItemDamage() != _inventory[1].getItemDamage())
		{
			return;
		}
		else if(source.getTagCompound() != null || _inventory[1].getTagCompound() != null)
		{
			return;
		}
		else
		{
			amt = Math.min(_inventory[0].stackSize, _inventory[1].getMaxStackSize() - _inventory[1].stackSize);
		}
		
		if(_inventory[1] == null)
		{
			_inventory[1] = source.copy();
			_inventory[0].stackSize -= source.stackSize;
		}
		else
		{
			_inventory[1].stackSize += amt;
			_inventory[0].stackSize -= amt;
		}
		
		if(_inventory[0].stackSize == 0)
		{
			_inventory[0] = null;
		}
	}
	
	@Override
	protected void onFactoryInventoryChanged()
	{
		_preferredOutputs.clear();
		for(int i = 2; i < 11; i++)
		{
			if(_inventory[i] == null)
			{
				continue;
			}
			List<String> names = OreDictTracker.getNamesFromItem(_inventory[i]);
			if(names != null)
			{
				for(String name : names)
				{
					_preferredOutputs.put(name, _inventory[i].copy());
				}
			}
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return 11;
	}
	
	@Override
	public boolean shouldDropSlotWhenBroken(int slot)
	{
		return slot < 2;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 11;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int sideordinal)
	{
        return slot == 0;
    }
	
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int sideordinal)
	{
        return slot == 1;
    }
	
	@Override
	public FluidTank getTank()
	{
		return _tank;
	}
	
	@Override
	public boolean allowBucketFill()
	{
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

    @Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		if(resource == null || resource.amount == 0) return 0;
		
		FluidStack converted = unifierTransformFluid(resource, doFill);
		
		if(converted == null || converted.amount == 0) return 0;
		
		int filled = _tank.fill(converted, doFill);
		
		if(filled == converted.amount)
		{
			return resource.amount;
		}
		else
		{
			return filled * resource.amount / converted.amount + (resource.amount & _roundingCompensation);
		}
	}
	
	private FluidStack unifierTransformFluid(FluidStack resource, boolean doFill)
	{
		if(_ethanol != null && _biofuel != null &&
				resource.fluidID == _ethanol.fluidID)
		{
			return new FluidStack(_biofuel.fluidID, resource.amount);
		}
		else if(_ethanol != null && _biofuel != null &&
				resource.fluidID == _biofuel.fluidID)
		{
			return new FluidStack(_ethanol.fluidID, resource.amount);
		}
		else if(_essence != null && _fluidxp != null &&
				resource.fluidID == _essence.fluidID)
		{
			return new FluidStack(_fluidxp.fluidID, resource.amount * 2);
		}
		else if(_essence != null && _fluidxp != null &&
				resource.fluidID == _fluidxp.fluidID)
		{
			if(doFill)
			{
				_roundingCompensation ^= (resource.amount & 1);
			}
			return new FluidStack(_essence.fluidID, resource.amount / 2 + (resource.amount & _roundingCompensation));
		}
		
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

    @Override
	public int fill(FluidStack resource, boolean doFill)
	{
		return fill(null, resource, doFill);
	}
	
	@Override
	public boolean allowBucketDrain()
	{
		return true;
	}
	
	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		return _tank.drain(maxDrain, doDrain);
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		return _tank.drain(maxDrain, doDrain);
	}
}
