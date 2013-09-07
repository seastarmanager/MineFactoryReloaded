package powercrystals.minefactoryreloaded.modhelpers.forestry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;

@Mod(modid = "MineFactoryReloaded|CompatForestryPre", name = "MFR Compat: Forestry (2)", version = MineFactoryReloadedCore.version, dependencies = "before:Forestry")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class ForestryPre
{
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e)
	{
        /*
		if(!Loader.isModLoaded("Forestry"))
		{
			return;
		}
		*/
	}
}
