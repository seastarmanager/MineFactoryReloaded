package powercrystals.minefactoryreloaded.tile.base;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.PneumaticPowerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.setup.Machine;

public abstract class TileEntityGenerator extends TileEntityFactoryInventory implements IPowerReceptor
{
	private IPowerProvider _powerProvider;
	
	protected TileEntityGenerator(Machine machine)
	{
		super(machine);
		_powerProvider = new PneumaticPowerProvider();
		_powerProvider.configure(0, 0, 0, 0, 0);
	}
	
	protected final int producePower(int mj)
	{
		BlockPosition ourbp = BlockPosition.fromFactoryTile(this);
		
		for(BlockPosition bp : ourbp.getAdjacent(true))
		{
			TileEntity te = worldObj.getBlockTileEntity(bp.x, bp.y, bp.z);
			if(te == null || !(te instanceof IPowerReceptor))
			{
				continue;
			}

			IPowerProvider pp = getPowerProvider();
			if(pp != null && pp.getMinEnergyReceived() <= mj)
			{
				int mjUsed = Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int)Math.floor(pp.getEnergyStored()));
				//pp.receiveEnergy(mjUsed, bp.orientation);
                if (pp.useEnergy(mjUsed, mjUsed, true) <= 0)
                    return 0;
				
				mj -= mjUsed;
				if(mj <= 0)
				{
					return 0;
				}
			}
		}
		
		return mj;
	}

    @Override
	public IPowerProvider getPowerProvider()
	{
		return _powerProvider;
	}

    @Override
    public void setPowerProvider(IPowerProvider provider) {
        _powerProvider = provider;
        _powerProvider.configure(0, 0, 0, 0, 0);
    }

    @Override
    public int powerRequest(ForgeDirection direction) {
        return 0;
    }
	
	@Override
	public void doWork()
	{
	}
}
