package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerAutoAnvil;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.util.Map;

public class TileEntityAutoAnvil extends TileEntityFactoryPowered implements IFluidContainerItem, IFluidTank {
    private FluidTank _tank;

    private int maximumCost;
    private int stackSizeToBeUsedInRepair;

    private ItemStack _output;

    public TileEntityAutoAnvil() {
        super(Machine.AutoAnvil);
        _tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int sideordinal) {
        if (slot == 0 || slot == 1) return true;
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int sideordinal) {
        if (slot == 2) return true;
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public String getGuiBackground() {
        return "autoanvil.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiFactoryPowered(getContainer(inventoryPlayer), this);
    }

    @Override
    public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerAutoAnvil(this, inventoryPlayer);
    }

    @Override
    protected boolean activateMachine() {
        if (_output == null) {
            return false;
        } else {
            if (_tank.getFluid() == null || _tank.getFluidAmount() < 4) {
                return false;
            }
            if (stackSizeToBeUsedInRepair > 0 && (_inventory[1] == null || _inventory[1].stackSize < stackSizeToBeUsedInRepair)) {
                return false;
            }

            _tank.drain(4, true);
            setWorkDone(getWorkDone() + 1);

            if (getWorkDone() >= getWorkMax()) {
                _inventory[0] = null;
                _inventory[2] = _output;

                if (stackSizeToBeUsedInRepair > 0 && _inventory[1].stackSize > stackSizeToBeUsedInRepair) {
                    _inventory[1].stackSize -= stackSizeToBeUsedInRepair;
                } else {
                    _inventory[1] = null;
                }

                setWorkDone(0);
                _output = null;
            }

            return true;
        }
    }

    @Override
    protected void onFactoryInventoryChanged() {
        super.onFactoryInventoryChanged();

        _output = getAnvilOutput();
        setWorkDone(0);
    }

    private ItemStack getAnvilOutput() {
        ItemStack startingItem = _inventory[0];
        this.maximumCost = 0;
        int totalEnchCost = 0;

        if (startingItem == null) {
            return null;
        } else {
            ItemStack outputItem = startingItem.copy();
            ItemStack addedItem = _inventory[1];

            @SuppressWarnings("unchecked")
            Map<Integer, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(outputItem);

            boolean enchantingWithBook = false;
            int repairCost = outputItem.getRepairCost() + (addedItem == null ? 0 : addedItem.getRepairCost());
            this.stackSizeToBeUsedInRepair = 0;

            if (addedItem != null) {
                enchantingWithBook = addedItem.itemID == Item.enchantedBook.itemID && Item.enchantedBook.func_92110_g(addedItem).tagCount() > 0;

                if (outputItem.isItemStackDamageable() && Item.itemsList[outputItem.itemID].getIsRepairable(outputItem, addedItem)) {
                    int currentDamage = Math.min(outputItem.getItemDamageForDisplay(), outputItem.getMaxDamage() / 4);

                    if (currentDamage <= 0) {
                        return null;
                    }

                    int repairStackSize;
                    for (repairStackSize = 0; currentDamage > 0 && repairStackSize < addedItem.stackSize; repairStackSize++) {
                        outputItem.setItemDamage(outputItem.getItemDamageForDisplay() - currentDamage);
                        totalEnchCost += Math.max(1, currentDamage / 100) + existingEnchantments.size();
                        currentDamage = Math.min(outputItem.getItemDamageForDisplay(), outputItem.getMaxDamage() / 4);
                    }

                    this.stackSizeToBeUsedInRepair = repairStackSize;
                } else {
                    if (!enchantingWithBook && (outputItem.itemID != addedItem.itemID || !outputItem.isItemStackDamageable())) {
                        return null;
                    }

                    if (outputItem.isItemStackDamageable() && !enchantingWithBook) {
                        int currentDamage = outputItem.getMaxDamage() - outputItem.getItemDamageForDisplay();
                        int addedItemDamage = addedItem.getMaxDamage() - addedItem.getItemDamageForDisplay();
                        int newDamage = addedItemDamage + outputItem.getMaxDamage() * 12 / 100;
                        int leftoverDamage = currentDamage + newDamage;
                        int repairedDamage = outputItem.getMaxDamage() - leftoverDamage;

                        if (repairedDamage < 0) {
                            repairedDamage = 0;
                        }

                        if (repairedDamage < outputItem.getItemDamage()) {
                            outputItem.setItemDamage(repairedDamage);
                            totalEnchCost += Math.max(1, newDamage / 100);
                        }
                    }

                    @SuppressWarnings("unchecked")
                    Map<Integer, Integer> addedEnchantments = EnchantmentHelper.getEnchantments(addedItem);

                    for (Integer addedEnchId : addedEnchantments.keySet()) {
                        Enchantment enchantment = Enchantment.enchantmentsList[addedEnchId];
                        int existingEnchLevel = existingEnchantments.containsKey(addedEnchId) ? existingEnchantments.get(addedEnchId) : 0;
                        int addedEnchLevel = addedEnchantments.get(addedEnchId);
                        int newEnchLevel;

                        if (existingEnchLevel == addedEnchLevel) {
                            ++addedEnchLevel;
                            newEnchLevel = addedEnchLevel;
                        } else {
                            newEnchLevel = Math.max(addedEnchLevel, existingEnchLevel);
                        }

                        addedEnchLevel = newEnchLevel;
                        int levelDifference = addedEnchLevel - existingEnchLevel;
                        boolean canEnchantmentBeAdded = enchantment.canApply(outputItem);

                        if (outputItem.itemID == ItemEnchantedBook.enchantedBook.itemID) {
                            canEnchantmentBeAdded = true;
                        }

                        for (Integer existingEnchId : existingEnchantments.keySet()) {
                            if (existingEnchId != addedEnchId && !enchantment.canApplyTogether(Enchantment.enchantmentsList[existingEnchId])) {
                                canEnchantmentBeAdded = false;
                                totalEnchCost += levelDifference;
                            }
                        }

                        if (canEnchantmentBeAdded) {
                            if (newEnchLevel > enchantment.getMaxLevel()) {
                                newEnchLevel = enchantment.getMaxLevel();
                            }

                            existingEnchantments.put(Integer.valueOf(addedEnchId), Integer.valueOf(newEnchLevel));
                            int enchCost = 0;

                            switch (enchantment.getWeight()) {
                                case 1:
                                    enchCost = 8;
                                    break;
                                case 2:
                                    enchCost = 4;
                                case 3:
                                case 4:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                default:
                                    break;
                                case 5:
                                    enchCost = 2;
                                    break;
                                case 10:
                                    enchCost = 1;
                            }

                            if (enchantingWithBook) {
                                enchCost = Math.max(1, enchCost / 2);
                            }

                            totalEnchCost += enchCost * levelDifference;
                        }
                    }
                }
            }

            int enchCount = 0;

            for (Integer existingEnchId : existingEnchantments.keySet()) {
                Enchantment enchantment = Enchantment.enchantmentsList[existingEnchId];
                int existingEnchLevel = existingEnchantments.get(existingEnchId);
                int enchCost = 0;
                ++enchCount;

                switch (enchantment.getWeight()) {
                    case 1:
                        enchCost = 8;
                        break;
                    case 2:
                        enchCost = 4;
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;
                    case 5:
                        enchCost = 2;
                        break;
                    case 10:
                        enchCost = 1;
                }

                if (enchantingWithBook) {
                    enchCost = Math.max(1, enchCost / 2);
                }
                repairCost += enchCount + existingEnchLevel * enchCost;
            }

            if (enchantingWithBook) {
                repairCost = Math.max(1, repairCost / 2);
            }

            if (enchantingWithBook && !Item.itemsList[outputItem.itemID].isBookEnchantable(outputItem, addedItem)) {
                outputItem = null;
            }

            this.maximumCost = repairCost + totalEnchCost;

            if (totalEnchCost <= 0) {
                outputItem = null;
            }

            if (outputItem != null) {
                EnchantmentHelper.setEnchantments(existingEnchantments, outputItem);
            }

            return outputItem;
        }
    }

    @Override
    public int getEnergyStoredMax() {
        return 16000;
    }

    @Override
    public int getWorkMax() {
        return 100 * maximumCost;
    }

    @Override
    public int getIdleTicksMax() {
        return 1;
    }

    @Override
    public FluidTank getTank() {
        return _tank;
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
        if (resource == null || (resource.fluidID != FluidRegistry.getFluidID("essence"))) {
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
}
