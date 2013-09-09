package powercrystals.minefactoryreloaded.tile.base;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import net.minecraft.tileentity.TileEntity;
import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.setup.Machine;

public abstract class TileEntityGenerator extends TileEntityFactoryInventory implements IPowerReceptor {
    private PowerHandler _powerProvider;

    protected TileEntityGenerator(Machine machine) {
        super(machine);
        _powerProvider = new PowerHandler(this, PowerHandler.Type.MACHINE);
        _powerProvider.configure(0, 0, 0, 0);
    }

    protected final int producePower(int mj) {
        BlockPosition ourbp = BlockPosition.fromFactoryTile(this);

        for (BlockPosition bp : ourbp.getAdjacent(true)) {
            TileEntity te = worldObj.getBlockTileEntity(bp.x, bp.y, bp.z);
            if (te == null || !(te instanceof IPowerReceptor)) {
                continue;
            }

            PowerHandler pp = getPowerProvider();
            if (pp != null && pp.getMinEnergyReceived() <= mj) {
                int mjUsed = (int) Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int) Math.floor(pp.getEnergyStored()));
                //pp.receiveEnergy(mjUsed, bp.orientation);
                if (pp.useEnergy(mjUsed, mjUsed, true) <= 0)
                    return 0;

                mj -= mjUsed;
                if (mj <= 0) {
                    return 0;
                }
            }
        }

        return mj;
    }

    public PowerHandler getPowerProvider() {
        return _powerProvider;
    }

    @Override
    public void doWork(PowerHandler h) {
    }
}
