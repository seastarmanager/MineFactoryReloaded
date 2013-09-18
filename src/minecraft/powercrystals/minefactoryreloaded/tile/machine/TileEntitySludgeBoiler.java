package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.core.position.Area;
import powercrystals.core.position.BlockPosition;
import powercrystals.core.random.WeightedRandomItemStack;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.util.List;
import java.util.Random;

public class TileEntitySludgeBoiler extends TileEntityFactoryPowered implements IFluidHandler {
    private FluidTank _tank;
    private Random _rand;
    private int _tick;

    public TileEntitySludgeBoiler() {
        super(Machine.SludgeBoiler);
        _tank = new FluidTank(4 * FluidContainerRegistry.BUCKET_VOLUME);

        _rand = new Random();
    }

    @Override
    public String getGuiBackground() {
        return "sludgeboiler.png";
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
    protected boolean activateMachine() {
        if (_tank.getFluid() != null && _tank.getFluidAmount() > 10) {
            _tank.drain(10, true);
            setWorkDone(getWorkDone() + 1);
            _tick++;

            if (getWorkDone() >= getWorkMax()) {
                ItemStack s = ((WeightedRandomItemStack) WeightedRandom.getRandomItem(_rand, MFRRegistry.getSludgeDrops())).getStack();

                doDrop(s);

                setWorkDone(0);
            }

            if (_tick >= 23) {
                Area a = new Area(new BlockPosition(this), 3, 3, 3);
                List<?> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, a.toAxisAlignedBB());
                for (Object o : entities) {
                    if (o instanceof EntityPlayer) {
                        ((EntityPlayer) o).addPotionEffect(new PotionEffect(Potion.hunger.id, 20 * 20, 0));
                    }
                    if (o instanceof EntityPlayer) {
                        ((EntityPlayer) o).addPotionEffect(new PotionEffect(Potion.poison.id, 6 * 20, 0));
                    }
                }
                _tick = 0;
            }
            return true;
        }
        return false;
    }

    @Override
    public ForgeDirection getDropDirection() {
        return ForgeDirection.DOWN;
    }

    @Override
    public boolean allowBucketFill() {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || (resource.fluidID != FluidRegistry.getFluidID("sludge")))
            return 0;
        return _tank.fill(resource, doFill);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return (fluid != null) && (fluid.getID() == FluidRegistry.getFluidID("sludge"));
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
