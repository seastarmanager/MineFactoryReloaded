package powercrystals.minefactoryreloaded.modhelpers.chococraft;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.block.Block;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;

@Mod(modid = "MineFactoryReloaded|CompatChococraft", name = "MFR Compat: Chococraft",
        version = MineFactoryReloadedCore.version,
        dependencies = "required-after:MineFactoryReloaded;after:chococraft")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class Chococraft {
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        if (!Loader.isModLoaded("chococraft")) {
            FMLLog.info("Chococraft is not available; MFR Chococraft Compat not loaded");
            return;
        }

        try {
            Class<?> mod = Class.forName("chococraft.common.ModChocoCraft");

            FMLLog.info("Registering Gysahls for Planter/Harvester/Fertilizer");
            int blockId = ((Block) (mod.getField("gysahlStemBlock").get(null))).blockID;

            MFRRegistry.registerHarvestable(new HarvestableChococraft(blockId));
            MFRRegistry.registerFertilizable(new FertilizableChococraft(blockId));

        } catch (ClassNotFoundException e) {
            FMLLog.warning("Unable to load support for Chococraft");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
