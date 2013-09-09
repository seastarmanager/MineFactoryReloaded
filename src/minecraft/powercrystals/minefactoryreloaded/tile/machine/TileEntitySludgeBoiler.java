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

public class TileEntitySludgeBoiler extends TileEntityFactoryPowered implements IFluidContainerItem, IFluidTank {
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
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null || (resource.fluidID != FluidRegistry.getFluidID("sludge"))) {
            return 0;
        } else {
            return _tank.fill(resource, doFill);
        }
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
        return null;
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
    public int fill(FluidStack resource, boolean doFill) {
        return fill(null, resource, doFill);
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
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean manageSolids() {
        return true;
    }

}
