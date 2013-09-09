package powercrystals.minefactoryreloaded.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.item.ItemSafariNet;

public class SlotAcceptReusableSafariNet extends Slot {
    public SlotAcceptReusableSafariNet(IInventory inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !ItemSafariNet.isEmpty(stack) && !ItemSafariNet.isSingleUse(stack);
    }
}
