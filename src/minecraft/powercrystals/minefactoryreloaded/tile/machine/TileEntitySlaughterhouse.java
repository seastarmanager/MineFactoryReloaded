package powercrystals.minefactoryreloaded.tile.machine;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fluids.FluidRegistry;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.core.GrindingDamage;
import powercrystals.minefactoryreloaded.setup.Machine;

import java.util.List;

public class TileEntitySlaughterhouse extends TileEntityGrinder {
    public TileEntitySlaughterhouse() {
        super(Machine.Slaughterhouse);
        _damageSource = new GrindingDamage(this, "mfr.slaughterhouse", 2);
    }

    @Override
    public String getGuiBackground() {
        return "slaughterhouse.png";
    }

    @Override
    public boolean activateMachine() {
        List<?> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, _areaManager.getHarvestArea().toAxisAlignedBB());

        entityList:
        for (Object o : entities) {
            EntityLiving e = (EntityLiving) o;
            for (Class<?> t : MFRRegistry.getSlaughterhouseBlacklist()) {
                if (t.isInstance(e)) {
                    continue entityList;
                }
            }
            if ((e instanceof EntityAgeable && ((EntityAgeable) e).getGrowingAge() < 0) || e.isEntityInvulnerable() || e.getHealth() <= 0) {
                continue;
            }
            double massFound = Math.pow(e.boundingBox.getAverageEdgeLength(), 2);
            damageEntity(e);
            if (e.getHealth() <= 0) {
                _tank.fill(FluidRegistry.getFluidStack(_rand.nextInt(8) == 0 ? "pinkslime" : "meat", (int) (100 * massFound)), true);
                setIdleTicks(10);
            } else {
                setIdleTicks(5);
            }
            return true;
        }
        setIdleTicks(getIdleTicksMax());
        return false;
    }

    @Override
    protected void damageEntity(EntityLiving entity) {
        setRecentlyHit(entity, 0);
        entity.attackEntityFrom(_damageSource, entity.getMaxHealth());
    }

    @Override
    public int getEnergyStoredMax() {
        return 16000;
    }

    @Override
    public boolean manageSolids() {
        return false;
    }
}
