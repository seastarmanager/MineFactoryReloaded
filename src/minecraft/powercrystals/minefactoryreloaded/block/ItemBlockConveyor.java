package powercrystals.minefactoryreloaded.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockConveyor extends ItemBlockFactory {
    public ItemBlockConveyor(int blockId) {
        super(blockId);
        setMaxDamage(0);
        setHasSubtypes(true);
        setNames(new String[]{"white", "orange", "magenta", "lightblue", "yellow", "lime", "pink", "gray", "lightgray", "cyan", "purple", "blue", "brown", "green", "red", "black", "default"});
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips) {
        infoList.add("Use Sledgehammer to rotate or slope");
    }
}
