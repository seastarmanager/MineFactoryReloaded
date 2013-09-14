package powercrystals.minefactoryreloaded.tile.base;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.setup.Machine;

public abstract class TileEntityGenerator extends TileEntityFactoryInventory implements IPowerReceptor, IPowerEmitter {
    private PowerHandler _powerProvider;

    protected TileEntityGenerator(Machine machine) {
        super(machine);
        _powerProvider = new PowerHandler(this, PowerHandler.Type.ENGINE);
        _powerProvider.configure(0, 0, 0, 1000);
    }

    protected final int producePower(int mj) {
        BlockPosition ourbp = BlockPosition.fromFactoryTile(this);

        for (BlockPosition bp : ourbp.getAdjacent(true)) {
            TileEntity te = bp.getTileEntity(worldObj);
            if (!(te instanceof IPowerReceptor))
                continue;

            PowerHandler.PowerReceiver pp = ((IPowerReceptor) te).getPowerReceiver(bp.orientation);
            if (pp != null && pp.getMinEnergyReceived() <= mj) {
                final float mjToUse = Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (float) Math.floor(pp.getEnergyStored()));
                final float mjUsed = pp.receiveEnergy(PowerHandler.Type.ENGINE, mjToUse, bp.orientation.getOpposite());

                mj -= mjUsed;
                if (mj <= 0)
                    return 0;
            }
        }
        return mj;
    }

    public PowerHandler getPowerProvider() {
        return _powerProvider;
    }

    @Override
    public boolean canEmitPowerFrom(ForgeDirection side) {
        return true;
    }

    @Override
    public void doWork(PowerHandler h) {
    }
}
