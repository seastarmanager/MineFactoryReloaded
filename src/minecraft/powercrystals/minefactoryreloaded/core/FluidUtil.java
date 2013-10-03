package powercrystals.minefactoryreloaded.core;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

/**
 * @author samrg472
 */
public class FluidUtil {

    private final FluidTank[] tanks;

    public FluidUtil(int size, int tankCapacity) {
        tanks = new FluidTank[size];
        for (int i = 0; i < size; i++)
            tanks[i] = new FluidTank(tankCapacity);
    }

    public int getTankCount() {
        return tanks.length;
    }

    public FluidTank getTank(int index) {
        return tanks[index];
    }

    public FluidStack getFluidFromTank(int index) {
        return getTank(index).getFluid();
    }

    public FluidTankInfo[] getTankInfo() {
        FluidTankInfo[] r = new FluidTankInfo[getTankCount()];
        for (int i = 0; i < r.length; i++)
            r[i] = tanks[i].getInfo();
        return r;
    }

    public int findFirstEmptyTank() {
        for (int i = 0; i < tanks.length; i++)
            if (tanks[i].getFluid() == null || tanks[i].getFluid().amount == 0)
                return i;
        return -1;
    }

    public int findFirstNonEmptyTank() {
        for (int i = 0; i < tanks.length; i++)
            if (tanks[i].getFluidAmount() > 0)
                return i;
        return -1;
    }

    public int findFirstMatchingTank(FluidStack fluid) {
        if (fluid == null)
            return -1;
        for (int i = 0; i < tanks.length; i++)
            if (fluid.isFluidEqual(tanks[i].getFluid()))
                return i;
        return -1;
    }

}
