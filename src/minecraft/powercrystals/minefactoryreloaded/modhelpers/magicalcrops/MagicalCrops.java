package powercrystals.minefactoryreloaded.modhelpers.magicalcrops;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizerStandard;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableCropPlant;
import powercrystals.minefactoryreloaded.modhelpers.FertilizableCropReflection;

import java.lang.reflect.Method;

@Mod(modid = "MineFactoryReloaded|CompatMagicalCrops", name = "MFR Compat: Magical Crops", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded;after:Magical-Crops")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class MagicalCrops {
    private static final String lastUpdated = "Magical Crops 2.1.2a, current release as of June 10 2013";

    @EventHandler
    public static void load(FMLInitializationEvent e) {
        if (!Loader.isModLoaded("Magical-Crops")) {
            FMLLog.warning("Magical Crops missing - MFR Compat: Magical Crops not loading");
            return;
        }
        try {
            Class<?> mod = Class.forName("magicCrop.mod_mCrops");

            // the various plants are separated by type to make future changes easier (mostly considering magicFertilizer behavior)
            String[] crops = {"Sberry", "Tomato", "Sweetcorn", "Cucum", "Melon", "Bberry", "Rberry", "Grape", "Chil"};
            String[] namedAsMagicalCropButExtendsBlockCrops = {"Nether"};
            String[] magicalCrops = {"Coal", "Dye", "Iron", "Redstone", "Glowstone", "Gold", "Diamond", "Lapis", "Blaze", "Emerald", "Ender", "Gunpowder", "XP", "Copper", "Tin", "Obsidian"};
            String[] soulCrops = {"Cow", "Pigmen", "Skele", "Spider"};

            int blockId;
            Method fertilize;

            for (String crop : crops) {
                blockId = ((Block) mod.getField("crop" + crop).get(null)).blockID;
                fertilize = Class.forName("magicCrop.crop" + crop).getMethod("func_72272_c_", World.class, int.class, int.class, int.class);
                MFRRegistry.registerHarvestable(new HarvestableCropPlant(blockId, 7));
                MFRRegistry.registerFertilizable(new FertilizableCropReflection(blockId, fertilize, 7));
            }

            for (String crop : namedAsMagicalCropButExtendsBlockCrops) {
                blockId = ((Block) mod.getField("mCrop" + crop).get(null)).blockID;
                fertilize = Class.forName("magicCrop.mCrop" + crop).getMethod("func_72272_c_", World.class, int.class, int.class, int.class);
                MFRRegistry.registerHarvestable(new HarvestableCropPlant(blockId, 7));
                MFRRegistry.registerFertilizable(new FertilizableCropReflection(blockId, fertilize, 7));
            }

            for (String magicalCrop : magicalCrops) {
                blockId = ((Block) mod.getField("mCrop" + magicalCrop).get(null)).blockID;
                fertilize = Class.forName("magicCrop.mCrop" + magicalCrop).getMethod("fertilize", World.class, int.class, int.class, int.class);
                MFRRegistry.registerHarvestable(new HarvestableCropPlant(blockId, 7));
                MFRRegistry.registerFertilizable(new FertilizableMagicalCropReflection(blockId, fertilize, 7));
            }

            for (String soulCrop : soulCrops) {
                blockId = ((Block) mod.getField("soulCrop" + soulCrop).get(null)).blockID;
                fertilize = Class.forName("magicCrop.soulCrop" + soulCrop).getMethod("fertilize", World.class, int.class, int.class, int.class);
                MFRRegistry.registerHarvestable(new HarvestableCropPlant(blockId, 7));
                MFRRegistry.registerFertilizable(new FertilizableMagicalCropReflection(blockId, fertilize, 7));
            }

            Item magicalFertilizer = (Item) mod.getField("magicFertilizer").get(null);
            if (magicalFertilizer != null) {
                MFRRegistry.registerFertilizer(new FertilizerStandard(magicalFertilizer.itemID, 0, FertilizerType.GrowMagicalCrop));
            }

        } catch (Exception x) {
            FMLLog.warning("Something went wrong in MFR Compat: Magical Crops. Probably Emy's fault.");
            System.out.println("Last updated for " + lastUpdated);
            x.printStackTrace();
        }
    }
}
