package powercrystals.minefactoryreloaded.farmables.safarinethandlers;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.api.ISafariNetHandler;

import java.util.List;

public class EntityAgeableHandler implements ISafariNetHandler {
    @Override
    public Class<?> validFor() {
        return EntityAgeable.class;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack safariNetStack, EntityPlayer player, List infoList, boolean advancedTooltips) {
        if (safariNetStack.getTagCompound().getInteger("Age") < 0) {
            infoList.add("Baby");
        }
    }
}
