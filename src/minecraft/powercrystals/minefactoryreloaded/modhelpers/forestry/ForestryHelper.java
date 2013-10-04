package powercrystals.minefactoryreloaded.modhelpers.forestry;

import forestry.core.config.ForestryBlock;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.item.ItemLaserFocus;
import powercrystals.minefactoryreloaded.modhelpers.Weights;

/**
 * @author samrg472
 */
public class ForestryHelper {

    static void init() {
        ItemStack apatiteOre = new ItemStack(ForestryBlock.resources, 1, 0);
        ItemStack copperOre = new ItemStack(ForestryBlock.resources, 1, 1);
        ItemStack tinOre = new ItemStack(ForestryBlock.resources, 1, 2);

        MFRRegistry.registerLaserOre(100, apatiteOre, ItemLaserFocus.Type.BLUE);
        MFRRegistry.registerLaserOre(Weights.COPPER, copperOre);
        MFRRegistry.registerLaserOre(Weights.TIN, tinOre);
    }

}
