package powercrystals.minefactoryreloaded.tile.machine;

import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.api.IFactoryRanchable;
import powercrystals.minefactoryreloaded.core.HarvestAreaManager;
import powercrystals.minefactoryreloaded.core.IHarvestAreaContainer;
import powercrystals.minefactoryreloaded.core.MFRLiquidMover;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.util.List;

public class TileEntityRancher extends TileEntityFactoryPowered implements IFluidContainerItem, IFluidTank, IHarvestAreaContainer {
    private HarvestAreaManager _areaManager;
    private FluidTank _tank;

    public TileEntityRancher() {
        super(Machine.Rancher);
        _areaManager = new HarvestAreaManager(this, 2, 2, 1);
        _tank = new FluidTank(4 * FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public String getGuiBackground() {
        return "rancher.png";
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
    protected boolean shouldPumpFluid() {
        return true;
    }

    @Override
    public FluidTank getTank() {
        return _tank;
    }

    @Override
    public int getEnergyStoredMax() {
        return 32000;
    }

    @Override
    public int getWorkMax() {
        return 1;
    }

    @Override
    public int getIdleTicksMax() {
        return 200;
    }

    @Override
    public HarvestAreaManager getHAM() {
        return _areaManager;
    }

    @Override
    public boolean activateMachine() {
        MFRLiquidMover.pumpFluid(_tank, this);

        boolean didDrop = false;

        List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, _areaManager.getHarvestArea().toAxisAlignedBB());

        for (EntityLiving e : entities) {
            Class<? extends EntityLiving> clazz = e.getClass();
            if (MFRRegistry.getRanchables().containsKey(clazz)) {
                IFactoryRanchable r = MFRRegistry.getRanchables().get(clazz);
                List<ItemStack> drops = r.ranch(worldObj, e, this);
                if (drops != null) {
                    for (ItemStack s : drops) {
                        if ((s.getItemDamage() == Integer.MAX_VALUE) && (FluidRegistry.getFluid(s.itemID) != null)) {
                            //_tank.fill(new LiquidStack(s.itemID, LiquidContainerRegistry.BUCKET_VOLUME, s.getItemDamage()), true);
                            _tank.fill(FluidRegistry.getFluidStack(FluidRegistry.getFluidName(s.itemID), s.stackSize), true);
                            didDrop = true;
                            continue;
                        }

                        doDrop(s);
                        didDrop = true;
                    }
                    if (didDrop) {
                        setIdleTicks(20);
                        return true;
                    }
                }
            }
        }

        setIdleTicks(getIdleTicksMax());
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public boolean allowBucketDrain() {
        return true;
    }

    @Override
    public boolean canRotate() {
        return true;
    }

    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection) {
        return getPowerProvider().getPowerReceiver();
    }

    @Override
    public World getWorld() {
        return worldObj;
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

    /**
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack attempting to fill the container.
     * @param doFill    If false, the fill will only be simulated.
     * @return Amount of fluid that was (or would have been, if simulated) filled into the
     * container.
     */
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
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
        return 0;
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
        return null;
    }

    /**
     * @param resource FluidStack attempting to fill the tank.
     * @param doFill   If false, the fill will only be simulated.
     * @return Amount of fluid that was accepted by the tank.
     */
    @Override
    public int fill(FluidStack resource, boolean doFill) {
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
}
