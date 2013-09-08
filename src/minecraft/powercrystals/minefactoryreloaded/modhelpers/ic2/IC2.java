package powercrystals.minefactoryreloaded.modhelpers.ic2;


import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
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
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableStandard;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Mod(modid = "MineFactoryReloaded|CompatIC2", name = "MFR Compat: IC2", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded;after:IC2")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class IC2
{
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
