package powercrystals.minefactoryreloaded.modhelpers.vanilla;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.INeedleAmmo;
import powercrystals.minefactoryreloaded.circuits.*;
import powercrystals.minefactoryreloaded.circuits.analog.*;
import powercrystals.minefactoryreloaded.circuits.digital.*;
import powercrystals.minefactoryreloaded.circuits.latch.*;
import powercrystals.minefactoryreloaded.circuits.logic.*;
import powercrystals.minefactoryreloaded.circuits.logicboolean.*;
import powercrystals.minefactoryreloaded.circuits.timing.Delay;
import powercrystals.minefactoryreloaded.circuits.timing.Multipulse;
import powercrystals.minefactoryreloaded.circuits.timing.OneShot;
import powercrystals.minefactoryreloaded.circuits.timing.PulseLengthener;
import powercrystals.minefactoryreloaded.circuits.wave.*;
import powercrystals.minefactoryreloaded.farmables.drinkhandlers.*;
import powercrystals.minefactoryreloaded.farmables.egghandlers.VanillaEggHandler;
import powercrystals.minefactoryreloaded.farmables.fertilizables.*;
import powercrystals.minefactoryreloaded.farmables.fruits.FruitCocoa;
import powercrystals.minefactoryreloaded.farmables.harvestables.*;
import powercrystals.minefactoryreloaded.farmables.ranchables.*;
import powercrystals.minefactoryreloaded.farmables.safarinethandlers.EntityAgeableHandler;
import powercrystals.minefactoryreloaded.farmables.safarinethandlers.EntityLivingHandler;
import powercrystals.minefactoryreloaded.farmables.safarinethandlers.SheepHandler;
import powercrystals.minefactoryreloaded.farmables.safarinethandlers.SlimeHandler;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

@Mod(modid = "MineFactoryReloaded|CompatVanilla", name = "MFR Compat: Vanilla", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class Vanilla {
    @EventHandler
    public void load(FMLInitializationEvent event) {
        MFRRegistry.registerHarvestable(new HarvestableWood());
        MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(Block.leaves.blockID));
        MFRRegistry.registerHarvestable(new HarvestableStandard(Block.reed.blockID, HarvestType.LeaveBottom));
        MFRRegistry.registerHarvestable(new HarvestableStandard(Block.cactus.blockID, HarvestType.LeaveBottom));
        MFRRegistry.registerHarvestable(new HarvestableStandard(Block.plantRed.blockID, HarvestType.Normal));
        MFRRegistry.registerHarvestable(new HarvestableStandard(Block.plantYellow.blockID, HarvestType.Normal));
        MFRRegistry.registerHarvestable(new HarvestableShrub(Block.tallGrass.blockID));
        MFRRegistry.registerHarvestable(new HarvestableShrub(Block.deadBush.blockID));
        MFRRegistry.registerHarvestable(new HarvestableStandard(Block.mushroomCapBrown.blockID, HarvestType.Tree));
        MFRRegistry.registerHarvestable(new HarvestableStandard(Block.mushroomCapRed.blockID, HarvestType.Tree));
        MFRRegistry.registerHarvestable(new HarvestableMushroom(Block.mushroomBrown.blockID));
        MFRRegistry.registerHarvestable(new HarvestableMushroom(Block.mushroomRed.blockID));
        MFRRegistry.registerHarvestable(new HarvestableStemPlant(Block.pumpkin.blockID, HarvestType.Normal));
        MFRRegistry.registerHarvestable(new HarvestableStemPlant(Block.melon.blockID, HarvestType.Normal));
        MFRRegistry.registerHarvestable(new HarvestableCropPlant(Block.crops.blockID, 7));
        MFRRegistry.registerHarvestable(new HarvestableCropPlant(Block.carrot.blockID, 7));
        MFRRegistry.registerHarvestable(new HarvestableCropPlant(Block.potato.blockID, 7));
        MFRRegistry.registerHarvestable(new HarvestableVine());
        MFRRegistry.registerHarvestable(new HarvestableNetherWart());
        MFRRegistry.registerHarvestable(new HarvestableCocoa());
        MFRRegistry.registerHarvestable(new HarvestableStandard(MineFactoryReloadedCore.rubberWoodBlock.blockID, HarvestType.Tree));
        MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(MineFactoryReloadedCore.rubberLeavesBlock.blockID));

        MFRRegistry.registerFertilizable(new FertilizableSapling(Block.sapling.blockID));
        MFRRegistry.registerFertilizable(new FertilizableCropPlant(Block.crops.blockID, 7));
        MFRRegistry.registerFertilizable(new FertilizableCropPlant(Block.carrot.blockID, 7));
        MFRRegistry.registerFertilizable(new FertilizableCropPlant(Block.potato.blockID, 7));
        MFRRegistry.registerFertilizable(new FertilizableGiantMushroom(Block.mushroomBrown.blockID));
        MFRRegistry.registerFertilizable(new FertilizableGiantMushroom(Block.mushroomRed.blockID));
        MFRRegistry.registerFertilizable(new FertilizableStemPlants(Block.pumpkinStem.blockID));
        MFRRegistry.registerFertilizable(new FertilizableStemPlants(Block.melonStem.blockID));
        MFRRegistry.registerFertilizable(new FertilizableNetherWart());
        MFRRegistry.registerFertilizable(new FertilizableCocoa());
        MFRRegistry.registerFertilizable(new FertilizableGrass());
        MFRRegistry.registerFertilizable(new FertilizableRubberSapling());

        MFRRegistry.registerFertilizer(new FertilizerStandard(MineFactoryReloadedCore.fertilizerItem.itemID, 0));
        if (MFRConfig.enableBonemealFertilizing.getBoolean(false)) {
            MFRRegistry.registerFertilizer(new FertilizerStandard(Item.dyePowder.itemID, 15));
        } else {
            MFRRegistry.registerFertilizer(new FertilizerStandard(Item.dyePowder.itemID, 15, FertilizerType.Grass));
        }

        MFRRegistry.registerRanchable(new RanchableCow());
        MFRRegistry.registerRanchable(new RanchableMooshroom());
        MFRRegistry.registerRanchable(new RanchableSheep());
        MFRRegistry.registerRanchable(new RanchableSquid());
        MFRRegistry.registerRanchable(new RanchableChicken());

        MFRRegistry.registerGrinderBlacklist(EntityPlayer.class);
        MFRRegistry.registerGrinderBlacklist(EntityDragon.class);
        MFRRegistry.registerGrinderBlacklist(EntityWither.class);
        MFRRegistry.registerGrinderBlacklist(EntityVillager.class);

        MFRRegistry.registerSludgeDrop(50, new ItemStack(Block.sand));
        MFRRegistry.registerSludgeDrop(40, new ItemStack(Block.dirt));
        MFRRegistry.registerSludgeDrop(30, new ItemStack(Item.clay, 4));
        MFRRegistry.registerSludgeDrop(3, new ItemStack(Block.mycelium));
        MFRRegistry.registerSludgeDrop(5, new ItemStack(Block.slowSand));

        MFRRegistry.registerSafariNetHandler(new EntityLivingHandler());
        MFRRegistry.registerSafariNetHandler(new EntityAgeableHandler());
        MFRRegistry.registerSafariNetHandler(new SheepHandler());
        MFRRegistry.registerSafariNetHandler(new SlimeHandler());

        MFRRegistry.registerMobEggHandler(new VanillaEggHandler());

        MFRRegistry.registerRubberTreeBiome("Swampland");
        MFRRegistry.registerRubberTreeBiome("Forest");
        MFRRegistry.registerRubberTreeBiome("Taiga");
        MFRRegistry.registerRubberTreeBiome("TaigaHills");
        MFRRegistry.registerRubberTreeBiome("Jungle");
        MFRRegistry.registerRubberTreeBiome("JungleHills");

        MFRRegistry.registerSafariNetBlacklist(EntityPlayer.class);
        MFRRegistry.registerSafariNetBlacklist(EntityDragon.class);
        MFRRegistry.registerSafariNetBlacklist(EntityWither.class);

        MFRRegistry.registerRandomMobProvider(new VanillaMobProvider());

        MFRRegistry.registerFluidDrinkHandler(Block.waterStill.blockID, new DrinkHandlerWater());
        MFRRegistry.registerFluidDrinkHandler(Block.waterMoving.blockID, new DrinkHandlerWater());
        MFRRegistry.registerFluidDrinkHandler(Block.lavaStill.blockID, new DrinkHandlerLava());
        MFRRegistry.registerFluidDrinkHandler(Block.lavaMoving.blockID, new DrinkHandlerLava());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.milkLiquid.blockID, new DrinkHandlerMilk());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.biofuelLiquid.blockID, new DrinkHandlerBiofuel());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.sewageLiquid.blockID, new DrinkHandlerSewage());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.sludgeLiquid.blockID, new DrinkHandlerSludge());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.essenceLiquid.blockID, new DrinkHandlerMobEssence());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.meatLiquid.blockID, new DrinkHandlerMeat());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.pinkSlimeLiquid.blockID, new DrinkHandlerPinkSlime());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.chocolateMilkLiquid.blockID, new DrinkHandlerChocolateMilk());
        MFRRegistry.registerFluidDrinkHandler(MineFactoryReloadedCore.mushroomSoupLiquid.blockID, new DrinkHandlerMushroomSoup());

        MFRRegistry.registerRedNetLogicCircuit(new AdderAnalog());
        MFRRegistry.registerRedNetLogicCircuit(new AdderDigitalFull());
        MFRRegistry.registerRedNetLogicCircuit(new AdderDigitalHalf());
        MFRRegistry.registerRedNetLogicCircuit(new And2());
        MFRRegistry.registerRedNetLogicCircuit(new And3());
        MFRRegistry.registerRedNetLogicCircuit(new And4());
        MFRRegistry.registerRedNetLogicCircuit(new Counter());
        MFRRegistry.registerRedNetLogicCircuit(new DecomposeIntToDecimal());
        MFRRegistry.registerRedNetLogicCircuit(new Delay());
        MFRRegistry.registerRedNetLogicCircuit(new DeMux16Analog());
        MFRRegistry.registerRedNetLogicCircuit(new DeMux4());
        MFRRegistry.registerRedNetLogicCircuit(new Equal());
        MFRRegistry.registerRedNetLogicCircuit(new Fanout());
        MFRRegistry.registerRedNetLogicCircuit(new FlipFlopJK());
        MFRRegistry.registerRedNetLogicCircuit(new FlipFlopT());
        MFRRegistry.registerRedNetLogicCircuit(new Greater());
        MFRRegistry.registerRedNetLogicCircuit(new GreaterOrEqual());
        MFRRegistry.registerRedNetLogicCircuit(new Inverter());
        MFRRegistry.registerRedNetLogicCircuit(new LatchDGated());
        MFRRegistry.registerRedNetLogicCircuit(new LatchSR());
        MFRRegistry.registerRedNetLogicCircuit(new LatchSRGated());
        MFRRegistry.registerRedNetLogicCircuit(new Less());
        MFRRegistry.registerRedNetLogicCircuit(new LessOrEqual());
        MFRRegistry.registerRedNetLogicCircuit(new Max2());
        MFRRegistry.registerRedNetLogicCircuit(new Max3());
        MFRRegistry.registerRedNetLogicCircuit(new Max4());
        MFRRegistry.registerRedNetLogicCircuit(new Min2());
        MFRRegistry.registerRedNetLogicCircuit(new Min3());
        MFRRegistry.registerRedNetLogicCircuit(new Min4());
        MFRRegistry.registerRedNetLogicCircuit(new Multipulse());
        MFRRegistry.registerRedNetLogicCircuit(new Mux4());
        MFRRegistry.registerRedNetLogicCircuit(new Nand2());
        MFRRegistry.registerRedNetLogicCircuit(new Nand3());
        MFRRegistry.registerRedNetLogicCircuit(new Nand4());
        MFRRegistry.registerRedNetLogicCircuit(new Negator());
        MFRRegistry.registerRedNetLogicCircuit(new Noop());
        MFRRegistry.registerRedNetLogicCircuit(new Nor2());
        MFRRegistry.registerRedNetLogicCircuit(new Nor3());
        MFRRegistry.registerRedNetLogicCircuit(new Nor4());
        MFRRegistry.registerRedNetLogicCircuit(new NotEqual());
        MFRRegistry.registerRedNetLogicCircuit(new OneShot());
        MFRRegistry.registerRedNetLogicCircuit(new Or2());
        MFRRegistry.registerRedNetLogicCircuit(new Or3());
        MFRRegistry.registerRedNetLogicCircuit(new Or4());
        MFRRegistry.registerRedNetLogicCircuit(new Passthrough());
        MFRRegistry.registerRedNetLogicCircuit(new PassthroughGated());
        MFRRegistry.registerRedNetLogicCircuit(new PassthroughRoundRobin());
        MFRRegistry.registerRedNetLogicCircuit(new PulseLengthener());
        MFRRegistry.registerRedNetLogicCircuit(new RandomizerAnalog());
        MFRRegistry.registerRedNetLogicCircuit(new RandomizerDigital());
        MFRRegistry.registerRedNetLogicCircuit(new SevenSegmentEncoder());
        MFRRegistry.registerRedNetLogicCircuit(new SawtoothFalling());
        MFRRegistry.registerRedNetLogicCircuit(new SawtoothRising());
        MFRRegistry.registerRedNetLogicCircuit(new Scaler());
        MFRRegistry.registerRedNetLogicCircuit(new SchmittTrigger());
        MFRRegistry.registerRedNetLogicCircuit(new Sine());
        MFRRegistry.registerRedNetLogicCircuit(new Square());
        MFRRegistry.registerRedNetLogicCircuit(new Subtractor());
        MFRRegistry.registerRedNetLogicCircuit(new Triangle());
        MFRRegistry.registerRedNetLogicCircuit(new Xnor2());
        MFRRegistry.registerRedNetLogicCircuit(new Xnor3());
        MFRRegistry.registerRedNetLogicCircuit(new Xnor4());
        MFRRegistry.registerRedNetLogicCircuit(new Xor2());
        MFRRegistry.registerRedNetLogicCircuit(new Xor3());
        MFRRegistry.registerRedNetLogicCircuit(new Xor4());

        MFRRegistry.registerLaserOre(175, new ItemStack(Block.oreCoal));
        MFRRegistry.registerLaserOre(50, new ItemStack(Block.oreDiamond));
        MFRRegistry.registerLaserOre(50, new ItemStack(Block.oreEmerald));
        MFRRegistry.registerLaserOre(70, new ItemStack(Block.oreGold));
        MFRRegistry.registerLaserOre(150, new ItemStack(Block.oreIron));
        MFRRegistry.registerLaserOre(80, new ItemStack(Block.oreLapis));
        MFRRegistry.registerLaserOre(100, new ItemStack(Block.oreRedstone));
        MFRRegistry.registerLaserOre(50, new ItemStack(Block.oreNetherQuartz));
        MFRRegistry.registerLaserOre(80, new ItemStack(Block.glowStone));

        MFRRegistry.registerFruitLogBlockId(Block.wood.blockID);
        MFRRegistry.registerFruit(new FruitCocoa());

        MFRRegistry.registerAutoSpawnerBlacklist("VillagerGolem");

        MFRRegistry.addLaserPreferredOre(15, new ItemStack(Block.oreCoal));
        MFRRegistry.addLaserPreferredOre(3, new ItemStack(Block.oreDiamond));
        MFRRegistry.addLaserPreferredOre(5, new ItemStack(Block.oreEmerald));
        MFRRegistry.addLaserPreferredOre(4, new ItemStack(Block.oreGold));
        MFRRegistry.addLaserPreferredOre(12, new ItemStack(Block.oreIron));
        MFRRegistry.addLaserPreferredOre(11, new ItemStack(Block.oreLapis));
        MFRRegistry.addLaserPreferredOre(14, new ItemStack(Block.oreRedstone));
        MFRRegistry.addLaserPreferredOre(0, new ItemStack(Block.oreNetherQuartz));

        MFRRegistry.registerNeedleAmmoType(MineFactoryReloadedCore.needlegunAmmoStandardItem.itemID, (INeedleAmmo) MineFactoryReloadedCore.needlegunAmmoStandardItem);
        MFRRegistry.registerNeedleAmmoType(MineFactoryReloadedCore.needlegunAmmoLavaItem.itemID, (INeedleAmmo) MineFactoryReloadedCore.needlegunAmmoLavaItem);
        MFRRegistry.registerNeedleAmmoType(MineFactoryReloadedCore.needlegunAmmoSludgeItem.itemID, (INeedleAmmo) MineFactoryReloadedCore.needlegunAmmoSludgeItem);
        MFRRegistry.registerNeedleAmmoType(MineFactoryReloadedCore.needlegunAmmoSewageItem.itemID, (INeedleAmmo) MineFactoryReloadedCore.needlegunAmmoSewageItem);
        MFRRegistry.registerNeedleAmmoType(MineFactoryReloadedCore.needlegunAmmoFireItem.itemID, (INeedleAmmo) MineFactoryReloadedCore.needlegunAmmoFireItem);
        MFRRegistry.registerNeedleAmmoType(MineFactoryReloadedCore.needlegunAmmoAnvilItem.itemID, (INeedleAmmo) MineFactoryReloadedCore.needlegunAmmoAnvilItem);
    }
}
