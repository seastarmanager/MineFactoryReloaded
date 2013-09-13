package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.gui.client.GuiAutoSpawner;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.container.ContainerAutoSpawner;
import powercrystals.minefactoryreloaded.item.ItemSafariNet;
import powercrystals.minefactoryreloaded.setup.MFRConfig;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

public class TileEntityAutoSpawner extends TileEntityFactoryPowered implements IFluidContainerItem, IFluidTank {
    private static final int _spawnRange = 4;
    private FluidTank _tank;
    private boolean _spawnExact = false;

    public TileEntityAutoSpawner() {
        super(Machine.AutoSpawner);
        _tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
    }

    public boolean getSpawnExact() {
        return _spawnExact;
    }

    public void setSpawnExact(boolean spawnExact) {
        _spawnExact = spawnExact;
    }

    @Override
    public String getGuiBackground() {
        return "autospawner.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiAutoSpawner(getContainer(inventoryPlayer), this);
    }

    @Override
    public ContainerAutoSpawner getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerAutoSpawner(this, inventoryPlayer);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    protected boolean activateMachine() {
        ItemStack item = getStackInSlot(0);
        if (!isStackValidForSlot(0, item)) {
            setWorkDone(0);
            return false;
        }
        NBTTagCompound itemTag = item.getTagCompound();
        String entityID = itemTag.getString("id");
        boolean isBlackListed = MFRRegistry.getAutoSpawnerBlacklist().contains(entityID);
        blackList:
        if (!isBlackListed) {
            Class<?> e = (Class<?>) EntityList.stringToClassMapping.get(entityID);
            if (e == null) {
                isBlackListed = true;
                break blackList;
            }
            for (Class<?> t : MFRRegistry.getAutoSpawnerClassBlacklist()) {
                if (t.isAssignableFrom(e)) {
                    isBlackListed = true;
                    break blackList;
                }
            }
        }
        if (isBlackListed) {
            setWorkDone(0);
            return false;
        }
        if (getWorkDone() < getWorkMax()) {
            if (_tank.getFluid() != null && _tank.getFluidAmount() >= 10) {
                _tank.getFluid().amount -= 10;
                setWorkDone(getWorkDone() + 1);
                return true;
            } else {
                return false;
            }
        } else {
            Entity spawnedEntity = EntityList.createEntityByName(entityID, worldObj);

            if (!(spawnedEntity instanceof EntityLiving)) {
                return false;
            }

            EntityLiving spawnedLiving = (EntityLiving) spawnedEntity;

            if (_spawnExact) {
                NBTTagCompound tag = (NBTTagCompound) itemTag.copy();
                spawnedLiving.writeToNBT(tag);
                spawnedLiving.readEntityFromNBT(tag);

				for (int i = 0; i < 5; ++i)
					spawnedLiving.setEquipmentDropChance(i, 0);
            }

            double x = xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * _spawnRange;
            double y = yCoord + worldObj.rand.nextInt(3) - 1;
            double z = zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * _spawnRange;

            spawnedLiving.setLocationAndAngles(x, y, z, worldObj.rand.nextFloat() * 360.0F, 0.0F);

            if (!worldObj.checkNoEntityCollision(spawnedLiving.boundingBox) ||
                    (!worldObj.getCollidingBoundingBoxes(spawnedLiving, spawnedLiving.boundingBox).isEmpty()) ||
                    (worldObj.isAnyLiquid(spawnedLiving.boundingBox) != (spawnedLiving instanceof EntityWaterMob))) {
                return false;
            }

            worldObj.spawnEntityInWorld(spawnedLiving);
            worldObj.playAuxSFX(2004, this.xCoord, this.yCoord, this.zCoord, 0);

            spawnedLiving.spawnExplosionParticle();
            setWorkDone(0);
            return true;
        }
    }

    @Override
    public int getEnergyStoredMax() {
        return 32000;
    }

    @Override
    public int getWorkMax() {
        return _spawnExact ? MFRConfig.autospawnerCostExact.getInt() : MFRConfig.autospawnerCostStandard.getInt();
    }

    @Override
    public int getIdleTicksMax() {
        return 200;
    }

    @Override
    public FluidTank getTank() {
        return _tank;
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
        if (resource == null || resource.fluidID != FluidRegistry.getFluidID("essence")) {
            return 0;
        }
        return _tank.fill(resource, doFill);
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
    public boolean allowBucketFill() {
        return true;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return isStackValidForSlot(slot, itemstack);
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return ItemSafariNet.isSafariNet(itemstack) &&
                !ItemSafariNet.isSingleUse(itemstack) &&
                !ItemSafariNet.isEmpty(itemstack);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        _spawnExact = nbttagcompound.getBoolean("spawnExact");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("spawnExact", _spawnExact);
    }
}
