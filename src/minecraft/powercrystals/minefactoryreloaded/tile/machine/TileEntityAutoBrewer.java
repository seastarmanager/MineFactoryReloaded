package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import powercrystals.core.util.InventoryUtil;
import powercrystals.minefactoryreloaded.gui.client.GuiAutoBrewer;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.container.ContainerAutoBrewer;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.util.List;

public class TileEntityAutoBrewer extends TileEntityFactoryPowered {
    public TileEntityAutoBrewer() {
        super(Machine.AutoBrewer);
    }

    private int getProcessSlot(int row) {
        return row * 5;
    }

    private int getTemplateSlot(int row) {
        return row * 5 + 1;
    }

    private int getResourceSlot(int row, int slot) {
        return row * 5 + slot + 2;
    }

    @Override
    public int getSizeInventory() {
        // 6 sets of: process, template, res, res, res
        // 30 is output
        return 31;
    }

    @Override
    public String getGuiBackground() {
        return "autobrewer.png";
    }

    @Override
    public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerAutoBrewer(this, inventoryPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiAutoBrewer(getContainer(inventoryPlayer), this);
    }

    @Override
    protected boolean activateMachine() {
        if (_inventory[30] != null) {
            return false;
        }

        boolean hasWorkToDo = false;
        for (int row = 0; row < 6; row++) {
            if (_inventory[getProcessSlot(row)] != null && _inventory[getProcessSlot(row + 1)] == null && canBrew(row)) {
                hasWorkToDo = true;
            }
        }

        if (!hasWorkToDo) {
            setWorkDone(0);
            return false;
        }

        if (getWorkDone() < getWorkMax()) {
            setWorkDone(getWorkDone() + 1);
            return true;
        }

        setWorkDone(0);

        for (int row = 5; row >= 0; row--) {
            ItemStack current = _inventory[getProcessSlot(row)];
            ItemStack next = _inventory[getProcessSlot(row + 1)];
            if (next != null && current != null) {
                return row < 5;
            }

            ItemStack ingredient = _inventory[getTemplateSlot(row)];

            if (_inventory[getProcessSlot(row)] != null && _inventory[getProcessSlot(row)].getItem() instanceof ItemPotion) {
                int existingPotion = _inventory[getProcessSlot(row)].getItemDamage();
                int newPotion = this.getPotionResult(existingPotion, ingredient);
                @SuppressWarnings("unchecked") List<Integer> existingEffects = Item.potion.getEffects(existingPotion);
                @SuppressWarnings("unchecked") List<Integer> newEffects = Item.potion.getEffects(newPotion);

                if ((existingPotion <= 0 || existingEffects != newEffects) && (existingEffects == null || !existingEffects.equals(newEffects) && newEffects != null)) {
                    if (existingPotion != newPotion) {
                        _inventory[getProcessSlot(row)].setItemDamage(newPotion);
                    }
                } else if (!ItemPotion.isSplash(existingPotion) && ItemPotion.isSplash(newPotion)) {
                    _inventory[getProcessSlot(row)].setItemDamage(newPotion);
                }

                _inventory[getProcessSlot(row + 1)] = _inventory[getProcessSlot(row)];
                _inventory[getProcessSlot(row)] = null;

                for (int i = 0; i < 3; i++) {
                    if (!InventoryUtil.stacksEqual(_inventory[getResourceSlot(row, i)], ingredient)) {
                        continue;
                    }

                    if (Item.itemsList[ingredient.itemID].hasContainerItem()) {
                        _inventory[getResourceSlot(row, i)] = Item.itemsList[ingredient.itemID].getContainerItemStack(_inventory[getResourceSlot(row, i)]);
                    } else {
                        _inventory[getResourceSlot(row, i)].stackSize--;

                        if (_inventory[getResourceSlot(row, i)].stackSize <= 0) {
                            _inventory[getResourceSlot(row, i)] = null;
                        }
                    }
                    break;
                }
            }
        }
        return true;
    }

    private boolean canBrew(int row) {
        if (_inventory[getTemplateSlot(row)] == null) {
            return false;
        }

        boolean hasIngredients = false;
        for (int i = 0; i < 3; i++) {
            if (InventoryUtil.stacksEqual(_inventory[getTemplateSlot(row)], _inventory[getResourceSlot(row, i)])) {
                hasIngredients = true;
                break;
            }
        }
        if (!hasIngredients) {
            return false;
        }

        ItemStack ingredient = _inventory[getTemplateSlot(row)];

        if (!Item.itemsList[ingredient.itemID].isPotionIngredient()) {
            return false;
        }

        if (_inventory[getProcessSlot(row)] != null && _inventory[getProcessSlot(row)].getItem() instanceof ItemPotion) {
            int existingPotion = _inventory[getProcessSlot(row)].getItemDamage();
            int newPotion = this.getPotionResult(existingPotion, ingredient);

            if (!ItemPotion.isSplash(existingPotion) && ItemPotion.isSplash(newPotion)) {
                return true;
            }

            @SuppressWarnings("unchecked") List<Integer> existingEffects = Item.potion.getEffects(existingPotion);
            @SuppressWarnings("unchecked") List<Integer> newEffects = Item.potion.getEffects(newPotion);

            if ((existingPotion <= 0 || existingEffects != newEffects) && (existingEffects == null || !existingEffects.equals(newEffects) && newEffects != null) && existingPotion != newPotion) {
                return true;
            }
        }

        return false;
    }

    private int getPotionResult(int existingPotion, ItemStack ingredient) {
        if (ingredient == null || !Item.itemsList[ingredient.itemID].isPotionIngredient()) {
            return existingPotion;
        }
        return PotionHelper.applyIngredient(existingPotion, Item.itemsList[ingredient.itemID].getPotionEffect());
    }

    @Override
    public int getEnergyStoredMax() {
        return 16000;
    }

    @Override
    public int getWorkMax() {
        return 160;
    }

    @Override
    public int getIdleTicksMax() {
        return 1;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        int row = slot / 5;
        int column = slot % 5;

        if (itemstack == null) return false;
        if (row == 6) return false;
        if (column == 1) return false;
        if (column == 0)
            return _inventory[getTemplateSlot(row)] != null && Item.itemsList[itemstack.itemID] instanceof ItemPotion && (row == 0 || _inventory[getTemplateSlot(row - 1)] == null);
        return InventoryUtil.stacksEqual(_inventory[getTemplateSlot(row)], itemstack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        int row = slot / 5;
        int column = slot % 5;

        if (row == 6) return true;
        if (column == 1) return false;
        if (column == 0) return _inventory[getTemplateSlot(row)] == null;
        return !InventoryUtil.stacksEqual(_inventory[getTemplateSlot(row)], itemstack);
    }

    @Override
    public boolean shouldDropSlotWhenBroken(int slot) {
        return slot % 5 != 1;
    }
}
