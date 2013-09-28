package powercrystals.minefactoryreloaded.modhelpers.xycraft;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.block.Block;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;

@Mod(modid = "MineFactoryReloaded|CompatXyCraft", name = "MFR Compat: XyCraft", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded;after:XyCraftWorld")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class Xycraft {
    @EventHandler
    public static void load(FMLInitializationEvent e) {
        if (!Loader.isModLoaded("XyCraftWorld")) {
            FMLLog.warning("XyCraft missing - MFR Xycraft Compat not loading");
            return;
        }
        try {
            Class<?> blockClass = Class.forName("soaryn.xycraft.world.XyCraftWorldBlocks");

            int CornCropsID = ((Block) blockClass.getField("corn").get(null)).blockID;
            int HenequenCropsID = ((Block) blockClass.getField("henequen").get(null)).blockID;

            MFRRegistry.registerHarvestable(new HarvestableXycraftCorn(CornCropsID));
            MFRRegistry.registerHarvestable(new HarvestableHenequen(HenequenCropsID));

            MFRRegistry.registerFertilizable(new FertilizableCorn(CornCropsID));
            MFRRegistry.registerFertilizable(new FertilizableHenequen(HenequenCropsID));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}