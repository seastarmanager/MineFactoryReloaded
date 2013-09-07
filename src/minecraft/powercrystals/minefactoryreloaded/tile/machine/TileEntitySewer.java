package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.core.position.Area;
import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.core.HarvestAreaManager;
import powercrystals.minefactoryreloaded.core.IHarvestAreaContainer;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiSewer;
import powercrystals.minefactoryreloaded.gui.container.ContainerSewer;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryInventory;

import java.util.List;

public class TileEntitySewer extends TileEntityFactoryInventory implements IFluidContainerItem, IFluidTank, IHarvestAreaContainer
{
	private FluidTank _tank;
	
	private HarvestAreaManager _areaManager;
	
	private int _tick;
	
	private long _nextSewerCheckTick;
	
	private boolean _jammed;
	
	public TileEntitySewer()
	{
		super(Machine.Sewer);
		_tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
		_areaManager = new HarvestAreaManager(this, 0, 1, 0);
		_areaManager.setOverrideDirection(ForgeDirection.UP);
	}
	
	@Override
	public String getGuiBackground()
	{
		return "sewagecollector.png";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer)
	{
		return new GuiSewer(getContainer(inventoryPlayer), this);
	}
	
	@Override
	public ContainerSewer getContainer(InventoryPlayer inventoryPlayer)
	{
		return new ContainerSewer(this, inventoryPlayer);
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
	protected void onFactoryInventoryChanged()
	{
		_areaManager.updateUpgradeLevel(_inventory[0]);
	}
	
	@Override
	public HarvestAreaManager getHAM()
	{
		return _areaManager;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(worldObj.isRemote)
		{
			return;
		}
		_tick++;
		
		if(_nextSewerCheckTick <= worldObj.getTotalWorldTime())
		{
			Area a = new Area(BlockPosition.fromFactoryTile(this), _areaManager.getRadius(), 0, 0);
			_jammed = false;
			for(BlockPosition bp : a.getPositionsBottomFirst())
			{
				if(worldObj.getBlockId(bp.x, bp.y, bp.z) == MineFactoryReloadedCore.machineBlocks.get(0).blockID &&
						worldObj.getBlockMetadata(bp.x, bp.y, bp.z) == Machine.Sewer.getMeta() &&
						!(bp.x == xCoord && bp.y == yCoord && bp.z == zCoord))
				{
					_jammed = true;
					break;
				}
			}
			
			_nextSewerCheckTick = worldObj.getTotalWorldTime() + 800 + worldObj.rand.nextInt(800);
		}
		
		if(_tick >= 31 && !_jammed)
		{
			_tick = 0;
			List<?> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, _areaManager.getHarvestArea().toAxisAlignedBB());
			double massFound = 0;
			for(Object o : entities)
			{
				if(o instanceof EntityAnimal || o instanceof EntityVillager)
				{
					massFound += Math.pow(((EntityLiving)o).boundingBox.getAverageEdgeLength(), 2);
				}
				else if(o instanceof EntityPlayer && ((EntityPlayer)o).isSneaking())
				{
					massFound += Math.pow(((EntityLiving)o).boundingBox.getAverageEdgeLength(), 2);
				}
			}
			if (massFound > 0)
			{
				_tank.fill(FluidRegistry.getFluidStack("sewage", (int) (25 * massFound)), true);
			}
			// TODO: add a second tank to the sewer for essence
			else if (_tank.getFluid() == null || _tank.getFluid().isFluidEqual(FluidRegistry.getFluidStack("essence", 1)))
			{
				int maxAmount = Math.max(_tank.getCapacity() - (_tank.getFluid() != null ? _tank.getFluidAmount() : 0), 0);
				if (maxAmount < 0)
				{
					return;
				}
				entities = worldObj.getEntitiesWithinAABB(EntityXPOrb.class, _areaManager.getHarvestArea().toAxisAlignedBB());
				for (Object o : entities)
				{
					Entity e = (Entity)o;
					if (e != null & e instanceof EntityXPOrb && !e.isDead)
					{
						EntityXPOrb orb = (EntityXPOrb)o;
						int found = Math.min(orb.xpValue, maxAmount);
						orb.xpValue -= found;
						if (orb.xpValue <= 0)
						{
							orb.setDead();
							found = Math.max(found, 0);
						}
						if (found > 0)
						{
							found = (int)(found * 66.66666667f);
							maxAmount -= found;
							_tank.fill(FluidRegistry.getFluidStack("essence", found), true);
							if (maxAmount <= 0)
							{
								break;
							}
						}
					}
				}
			}
		}
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

    @Override
	public int fill(FluidStack resource, boolean doFill)
	{
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

    @Override
	public boolean allowBucketDrain()
	{
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}

}
