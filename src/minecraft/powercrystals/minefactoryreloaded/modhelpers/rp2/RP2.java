package powercrystals.minefactoryreloaded.modhelpers.rp2;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableStandard;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableTreeLeaves;

import java.lang.reflect.Method;

@Mod(modid = "MineFactoryReloaded|CompatRP2", name = "MFR Compat: RP2", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded;after:RedPowerWorld")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class RP2 {
    @EventHandler
    public static void load(FMLInitializationEvent e) {
        if (!Loader.isModLoaded("RedPowerWorld")) {
            FMLLog.warning("RedPowerWorld missing - MFR RedPower2 Compat not loading");
            return;
        }
        try {
            Class<?> modClass = Class.forName("com.eloraam.redpower.RedPowerWorld");

            int blockIdLeaves = ((Block) modClass.getField("blockLeaves").get(null)).blockID;
            int blockIdLogs = ((Block) modClass.getField("blockLogs").get(null)).blockID;
            int blockIdPlants = ((Block) modClass.getField("blockPlants").get(null)).blockID;
            int blockIdCrops = ((Block) modClass.getField("blockCrops").get(null)).blockID;

            Method fertilizeMethod = Class.forName("com.eloraam.redpower.world.BlockCustomFlower").getMethod("growTree", World.class, int.class, int.class, int.class);

            MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(blockIdLeaves));
            MFRRegistry.registerHarvestable(new HarvestableStandard(blockIdLogs, HarvestType.Tree));
            MFRRegistry.registerHarvestable(new HarvestableRedPowerPlant(blockIdPlants));
            MFRRegistry.registerHarvestable(new HarvestableRedPowerFlax(blockIdCrops));

            MFRRegistry.registerFertilizable(new FertilizableRedPowerFlax(blockIdCrops));
            MFRRegistry.registerFertilizable(new FertilizableRedPowerRubberTree(blockIdPlants, fertilizeMethod));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
