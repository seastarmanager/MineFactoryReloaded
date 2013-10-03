package powercrystals.minefactoryreloaded.modhelpers.ic2;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizerStandard;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableTreeLeaves;
import powercrystals.minefactoryreloaded.modhelpers.Weights;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

/**
 * Internal helper class, never call methods here except through the IC2 class as a mod layer
 */
class IC2Helper {

    private static final String notUpdated = "IC2 API is not up to date!";

    static void init() {
        try {
            ItemStack crop = Items.getItem("crop");
            ItemStack rubber = Items.getItem("rubber");
            ItemStack rubberSapling = Items.getItem("rubberSapling");
            ItemStack rubberLeaves = Items.getItem("rubberLeaves");
            ItemStack rubberWood = Items.getItem("rubberWood");
            ItemStack stickyResin = Items.getItem("resin");
            ItemStack plantBall = Items.getItem("plantBall");

            if (rubberSapling != null)
                MFRRegistry.registerFertilizable(new FertilizableIC2RubberTree(rubberSapling.itemID));
            if (rubberLeaves != null)
                MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(rubberLeaves.itemID));
            if (rubberWood != null) {
                MFRRegistry.registerHarvestable(new HarvestableIC2RubberWood(rubberWood.itemID, HarvestType.Tree, stickyResin.itemID));
                MFRRegistry.registerFruitLogBlockId(((ItemBlock) rubberWood.getItem()).getBlockID());
                MFRRegistry.registerFruit(new FruitIC2Resin(rubberWood, stickyResin));
            }

            ItemStack fertilizer = Items.getItem("fertilizer");
            if (fertilizer != null) {
                MFRRegistry.registerFertilizer(new FertilizerStandard(fertilizer.itemID, fertilizer.getItemDamage()));
            }

            ItemStack tinOre = Items.getItem("tinOre");
            ItemStack copperOre = Items.getItem("copperOre");
            ItemStack leadOre = Items.getItem("leadOre");
            ItemStack uraniumOre = Items.getItem("uraniumOre");

            MFRRegistry.registerLaserOre(Weights.TIN.weight, tinOre);
            MFRRegistry.registerLaserOre(Weights.COPPER.weight, copperOre);
            MFRRegistry.registerLaserOre(Weights.LEAD.weight, leadOre);
            MFRRegistry.registerLaserOre(15, uraniumOre);

            MFRRegistry.registerHarvestable(new HarvestableIC2Crop(crop.itemID));

            GameRegistry.addShapedRecipe(plantBall,
                    "LLL",
                    "L L",
                    "LLL",
                    'L'  , new ItemStack(MineFactoryReloadedCore.rubberLeavesBlock));

            Recipes.extractor.addRecipe(new RecipeInputItemStack(new ItemStack(MineFactoryReloadedCore.rubberSaplingBlock)), null, rubber);
        } catch (Throwable t) {
            FMLLog.warning(notUpdated);
            t.printStackTrace();
        }
    }

    static void postTileLoadEvent(TileEntityFactoryPowered tefp) {
        try {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) tefp));
        } catch (Throwable t) {
            FMLLog.warning(notUpdated);
            t.printStackTrace();
        }
    }

    static void postTileUnloadEvent(TileEntityFactoryPowered tefp) {
        try {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) tefp));
        } catch (Throwable t) {
            FMLLog.warning(notUpdated);
            t.printStackTrace();
        }
    }

}
