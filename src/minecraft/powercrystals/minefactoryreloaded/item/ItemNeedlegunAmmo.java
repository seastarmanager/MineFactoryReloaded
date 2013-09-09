package powercrystals.minefactoryreloaded.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.api.INeedleAmmo;

import java.util.List;

public abstract class ItemNeedlegunAmmo extends ItemFactory implements INeedleAmmo {
    public ItemNeedlegunAmmo(int id) {
        super(id);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips) {
        super.addInformation(stack, player, infoList, advancedTooltips);
        infoList.add((stack.getMaxDamage() - stack.getItemDamage() + 1) + " rounds");
    }
}
