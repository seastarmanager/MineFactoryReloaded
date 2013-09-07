package powercrystals.minefactoryreloaded;

import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import powercrystals.minefactoryreloaded.core.GrindingDamage;

import java.util.List;

public class EventHandler {

    @ForgeSubscribe
    public void handleDrops(LivingDropsEvent e) {
        if (e.source instanceof GrindingDamage) {
            GrindingDamage damage = (GrindingDamage) e.source;
            List<EntityItem> drops = e.drops;

            for (EntityItem item : drops)
                damage.grinder.doDrop(item.getEntityItem());
            e.setCanceled(true);
        }
    }
}
