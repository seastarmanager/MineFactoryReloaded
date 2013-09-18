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

public class TileEntityRancher extends TileEntityFactoryPowered implements IFluidHandler, IHarvestAreaContainer {
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
        MFRLiquidMover.pumpFluid(this);

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

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
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
}
