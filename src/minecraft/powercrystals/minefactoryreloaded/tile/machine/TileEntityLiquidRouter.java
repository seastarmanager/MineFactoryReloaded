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

public class TileEntityLiquidRouter extends TileEntityFactoryInventory implements IFluidHandler
{
	private FluidTank[] _bufferTanks = new FluidTank[6];
	private static final ForgeDirection[] _outputDirections = new ForgeDirection[]
			{ ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST };
	
	public TileEntityLiquidRouter()
	{
		super(Machine.LiquidRouter);
		for(int i = 0; i < 6; i++)
		{
			_bufferTanks[i] = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
			//_bufferTanks[i].setTankPressure(-1);
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		for(int i = 0; i < 6; i++)
		{
			if(_bufferTanks[i].getFluidAmount() > 0)
			{
				_bufferTanks[i].getFluid().amount -= pumpLiquid(_bufferTanks[i].getFluid(), true);
			}
		}
	}
	
	private int pumpLiquid(FluidStack resource, boolean doFill)
	{
		if(resource == null || resource.fluidID <= 0 || resource.amount <= 0) return 0;
		
		int amountRemaining = resource.amount;
		int[] routes = getRoutesForLiquid(resource);
		int[] defaultRoutes = getDefaultRoutes();
		
		if(hasRoutes(routes))
		{
			amountRemaining = weightedRouteLiquid(resource, routes, amountRemaining, doFill);
		}
		else if(hasRoutes(defaultRoutes))
		{
			amountRemaining = weightedRouteLiquid(resource, defaultRoutes, amountRemaining, doFill);
		}
		
		return resource.amount - amountRemaining;
	}
	
	private int weightedRouteLiquid(FluidStack resource, int[] routes, int amountRemaining, boolean doFill)
	{
		if(amountRemaining >= totalWeight(routes))
		{
			int startingAmount = amountRemaining;
			for(int i = 0; i < routes.length; i++)
			{
				TileEntity te = BlockPosition.getAdjacentTileEntity(this, _outputDirections[i]);
				int amountForThisRoute = startingAmount * routes[i] / totalWeight(routes);
				if(te instanceof IFluidHandler && amountForThisRoute > 0)
				{
					amountRemaining -= ((IFluidHandler)te).fill(_outputDirections[i].getOpposite(),	new FluidStack(resource.fluidID, amountForThisRoute), doFill);
					if(amountRemaining <= 0)
					{
						break;
					}
				}
			}
		}
		
		if(0 < amountRemaining && amountRemaining < totalWeight(routes))
		{
			int outdir = weightedRandomSide(routes);
			TileEntity te = BlockPosition.getAdjacentTileEntity(this, _outputDirections[outdir]);
			if(te instanceof IFluidHandler)
			{
				amountRemaining -= ((IFluidHandler)te).fill(_outputDirections[outdir].getOpposite(), new FluidStack(resource.fluidID, amountRemaining), doFill);
			}
		}
		
		return amountRemaining;
	}
	
	private int weightedRandomSide(int[] routeWeights)
	{
		int random = worldObj.rand.nextInt(totalWeight(routeWeights));
		for(int i = 0; i < routeWeights.length; i++)
		{
			random -= routeWeights[i];
			if(random < 0)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	private int totalWeight(int[] routeWeights)
	{
		int total = 0;
		
		for(int weight : routeWeights)
		{
			total += weight;
		}
		return total;
	}
	
	private boolean hasRoutes(int[] routeWeights)
	{
		for(int weight : routeWeights)
		{
			if(weight > 0) return true;
		}
		return false;
	}
	
	
	private int[] getRoutesForLiquid(FluidStack resource)
	{
		int[] routeWeights = new int[6];
		
		for(int i = 0; i < 6; i++)
		{
			if(FluidContainerRegistry.containsFluid(_inventory[i], resource))
			{
				routeWeights[i] = _inventory[i].stackSize;
			}
			else
			{
				routeWeights[i] = 0;
			}
		}
		return routeWeights;
	}
	
	private int[] getDefaultRoutes()
	{
		int[] routeWeights = new int[6];
		
		for(int i = 0; i < 6; i++)
		{
			if(FluidContainerRegistry.isEmptyContainer(_inventory[i]))
			{
				routeWeights[i] = _inventory[i].stackSize;
			}
			else
			{
				routeWeights[i] = 0;
			}
		}
		return routeWeights;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return pumpLiquid(resource, doFill);
	}

    /*
	@Override
	public int fill(int tankIndex, FluidStack resource, boolean doFill)
	{
		return pumpFluid(resource, doFill);
	}
	*/
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
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
	public FluidTankInfo[] getTankInfo(ForgeDirection direction)
	{
		return new FluidTankInfo[] { _bufferTanks[direction.ordinal()].getInfo() };
	}

    /*
	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type)
	{
		if(LiquidContainerRegistry.containsLiquid(_inventory[direction.ordinal()], type))
		{
			return _bufferTanks[direction.ordinal()];
		}
		return null;
	}
	*/
	
	@Override
	public int getSizeInventory()
	{
		return 6;
	}
	
	@Override
	public boolean shouldDropSlotWhenBroken(int slot)
	{
		return false;
	}
	
	@Override
	public String getGuiBackground()
	{
		return "liquidrouter.png";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer)
	{
		return new GuiLiquidRouter(getContainer(inventoryPlayer), this);
	}
	
	@Override
	public ContainerLiquidRouter getContainer(InventoryPlayer inventoryPlayer)
	{
		return new ContainerLiquidRouter(this, inventoryPlayer);
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int side)
	{
		return false;
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side)
	{
		return false;
	}
}
