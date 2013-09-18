package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

public class TileEntityComposter extends TileEntityFactoryPowered implements IFluidHandler {
    private FluidTank _tank;

    public TileEntityComposter() {
        super(Machine.Composter);
        _tank = new FluidTank(4 * FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public String getGuiBackground() {
        return "composter.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiFactoryPowered(getContainer(inventoryPlayer), this);
    }

    @Override
    public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerFactoryPowered(this, inventoryPlayer);
    }

    @Override
    public FluidTank getTank() {
        return _tank;
    }

    @Override
    protected boolean activateMachine() {
        if (_tank.getFluid() != null && _tank.getFluidAmount() >= 20) {
            setWorkDone(getWorkDone() + 1);

            if (getWorkDone() >= getWorkMax()) {
                doDrop(new ItemStack(MineFactoryReloadedCore.fertilizerItem));
                setWorkDone(0);
            }
            _tank.drain(20, true);
            return true;
        }
        return false;
    }

    @Override
    public ForgeDirection getDropDirection() {
        return ForgeDirection.UP;
    }

    @Override
    public int getEnergyStoredMax() {
        return 16000;
    }

    @Override
    public int getWorkMax() {
        return 100;
    }

    @Override
    public int getIdleTicksMax() {
        return 1;
    }

    @Override
    public boolean allowBucketFill() {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || (resource.fluidID != FluidRegistry.getFluidID("sewage")))
            return 0;
        return _tank.fill(resource, doFill);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return (fluid != null) && (fluid.getID() == FluidRegistry.getFluidID("sewage"));
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { _tank.getInfo() };
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean manageSolids() {
        return true;
    }
}
