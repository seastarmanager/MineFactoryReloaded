package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.core.GrindingDamage;
import powercrystals.minefactoryreloaded.core.HarvestAreaManager;
import powercrystals.minefactoryreloaded.core.IHarvestAreaContainer;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityGrinder extends TileEntityFactoryPowered implements IFluidHandler, IHarvestAreaContainer {
    private static Field recentlyHit;

    protected HarvestAreaManager _areaManager;
    protected FluidTank _tank;
    protected Random _rand;
    protected GrindingDamage _damageSource;

    protected TileEntityGrinder(Machine machine) {
        super(machine);
        _areaManager = new HarvestAreaManager(this, 2, 2, 1);
        _tank = new FluidTank(4 * FluidContainerRegistry.BUCKET_VOLUME);
        _rand = new Random();
    }

    public TileEntityGrinder() {
        this(Machine.Grinder);
        _damageSource = new GrindingDamage(this);
    }

    @Override
    public String getGuiBackground() {
        return "grinder.png";
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

    public Random getRandom() {
        return _rand;
    }

    @Override
    protected boolean shouldPumpFluid() {
        return true;
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
    public FluidTank getTank() {
        return _tank;
    }

    @Override
    public HarvestAreaManager getHAM() {
        return _areaManager;
    }

    @Override
    public boolean activateMachine() {
        @SuppressWarnings("unchecked") List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, _areaManager.getHarvestArea().toAxisAlignedBB());

        for (EntityLiving e : entities) {
            if (e instanceof EntityAgeable && ((EntityAgeable) e).getGrowingAge() < 0 || e.isEntityInvulnerable() || e.getHealth() <= 0)
                continue;
            if (MFRRegistry.getGrinderBlacklist().contains(e.getClass()))
                continue;

            e.captureDrops = true;
            damageEntity(e);

            if (e.getHealth() <= 0) {
                _tank.fill(FluidRegistry.getFluidStack("essence", 100), true);
                setIdleTicks(20);
            } else {
                setIdleTicks(10);
            }

            List<EntityItem> items = e.capturedDrops;
            List<ItemStack> stackDrops = new ArrayList<ItemStack>(items.size());
            for (EntityItem ei : items)
                stackDrops.add(ei.getEntityItem());
            doDrop(stackDrops);

            return true;
        }
        setIdleTicks(getIdleTicksMax());
        return false;
    }

    protected void setRecentlyHit(EntityLiving entity, int t) {
        try {
            recentlyHit.set(entity, t);
        } catch (Throwable e) {
        }
    }

    protected void damageEntity(EntityLiving entity) {
        entity.attackEntityFrom(_damageSource, entity.getMaxHealth());
        setRecentlyHit(entity, 0); // prevent exp orbs from spawning
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
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
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
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
    public boolean allowBucketDrain() {
        return true;
    }

    @Override
    public boolean manageSolids() {
        return true;
    }

    @Override
    public boolean canRotate() {
        return true;
    }

    static {
        String[] names = ObfuscationReflectionHelper.remapFieldNames(EntityLivingBase.class.getName(), "field_70718_bc", "recentlyHit");
        recentlyHit = ReflectionHelper.findField(EntityLivingBase.class, names);
    }
}
