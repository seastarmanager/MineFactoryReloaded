package powercrystals.minefactoryreloaded.modhelpers.ic2;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizerStandard;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableTreeLeaves;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Internal helper class, never call methods here except through the IC2 class as a mod layer
 */
class IC2Helper {

    static void init() {
        try {
            ItemStack crop = Items.getItem("crop");
            ItemStack rubber = Items.getItem("rubber");
            ItemStack rubberSapling = Items.getItem("rubberSapling");
            ItemStack rubberLeaves = Items.getItem("rubberLeaves");
            ItemStack rubberWood = Items.getItem("rubberWood");
            ItemStack stickyResin = Items.getItem("resin");
            ItemStack plantBall = Items.getItem("plantBall");

            if (rubberSapling != null) {
                MFRRegistry.registerFertilizable(new FertilizableIC2RubberTree(rubberSapling.itemID));
            }
            if (rubberLeaves != null) {
                MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(rubberLeaves.itemID));
            }
            if (rubberWood != null) {
                MFRRegistry.registerHarvestable(new HarvestableIC2RubberWood(rubberWood.itemID, HarvestType.Tree, stickyResin.itemID));
                MFRRegistry.registerFruitLogBlockId(((ItemBlock) rubberWood.getItem()).getBlockID());
                MFRRegistry.registerFruit(new FruitIC2Resin(rubberWood, stickyResin));
            }

            ItemStack fertilizer = Items.getItem("fertilizer");
            if (fertilizer != null) {
                MFRRegistry.registerFertilizer(new FertilizerStandard(fertilizer.itemID, fertilizer.getItemDamage()));
            }

            MFRRegistry.registerHarvestable(new HarvestableIC2Crop(crop.itemID));

            GameRegistry.addShapedRecipe(plantBall, new Object[]
                    {
                            "LLL",
                            "L L",
                            "LLL",
                            Character.valueOf('L'), new ItemStack(MineFactoryReloadedCore.rubberLeavesBlock)
                    });

            try {
                Method m = Recipes.extractor.getClass().getMethod("addRecipe", IRecipeInput.class, NBTTagCompound.class, ItemStack[].class);
                m.invoke(Recipes.extractor, new RecipeInputItemStack(new ItemStack(MineFactoryReloadedCore.rubberSaplingBlock)), null, new ItemStack[]{rubber});
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } catch (Throwable t) {
            FMLLog.warning("IC2 API is not up to date!");
            t.printStackTrace();
        }
    }

    static void postTileLoadEvent(TileEntityFactoryPowered tefp) {
        try {
            Constructor con = EnergyTileLoadEvent.class.getConstructor(IEnergyTile.class);
            MinecraftForge.EVENT_BUS.post((EnergyTileLoadEvent) con.newInstance(tefp));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void postTileUnloadEvent(TileEntityFactoryPowered tefp) {
        try {
            Constructor con = EnergyTileUnloadEvent.class.getConstructor(IEnergyTile.class);
            MinecraftForge.EVENT_BUS.post((EnergyTileUnloadEvent) con.newInstance(tefp));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
