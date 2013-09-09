package powercrystals.minefactoryreloaded.modhelpers.ic2;


import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

@Mod(modid = "MineFactoryReloaded|CompatIC2", name = "MFR Compat: IC2", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded;after:IC2")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class IC2 {
    private static boolean isLoaded;

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void postTileLoadEvent(TileEntityFactoryPowered tefp) {
        if (!isLoaded())
            return;
        IC2Helper.postTileLoadEvent(tefp);
    }

    public static void postTileUnloadEvent(TileEntityFactoryPowered tefp) {
        if (!isLoaded())
            return;
        IC2Helper.postTileUnloadEvent(tefp);
    }

    @EventHandler
    public static void load(FMLInitializationEvent e) {
        isLoaded = Loader.isModLoaded("IC2");
        if (!isLoaded) {
            FMLLog.warning("IC2 missing - MFR IC2 Compat not loading");
            return;
        }
        IC2Helper.init();
    }
}
