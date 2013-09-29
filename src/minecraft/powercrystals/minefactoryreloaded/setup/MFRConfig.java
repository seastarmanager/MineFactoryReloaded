package powercrystals.minefactoryreloaded.setup;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import java.io.File;

public final class MFRConfig {

    private static MFRConfig instance;

    // client config
    public final int spyglassRange;

    // common config
    public final int machineBlock0Id;
    public final int machineBlock1Id;
    public final int machineBlock2Id;

    public final int conveyorBlockId;

    public final int factoryGlassBlockId;
    public final int factoryGlassPaneBlockId;
    public final int factoryRoadBlockId;
    public final int factoryDecorativeBrickBlockId;
    public final int factoryDecorativeStoneBlockId;

    public final int rubberWoodBlockId;
    public final int rubberLeavesBlockId;
    public final int rubberSaplingBlockId;

    public final int railPickupCargoBlockId;
    public final int railDropoffCargoBlockId;
    public final int railPickupPassengerBlockId;
    public final int railDropoffPassengerBlockId;

    public final int rednetCableBlockId;
    public final int rednetLogicBlockId;
    public final int rednetPanelBlockId;

    public final int fakeLaserBlockId;

    public final int vineScaffoldBlockId;

    public final int milkStillBlockId;
    public final int sludgeStillBlockId;
    public final int sewageStillBlockId;
    public final int essenceStillBlockId;
    public final int biofuelStillBlockId;
    public final int meatStillBlockId;
    public final int pinkslimeStillBlockId;
    public final int chocolateMilkStillBlockId;
    public final int mushroomSoupStillBlockId;

    public final int hammerItemId;
    public final int milkItemId;
    public final int sludgeItemId;
    public final int sewageItemId;
    public final int mobEssenceItemId;
    public final int fertilizerItemId;
    public final int plasticSheetItemId;
    public final int rawPlasticItemId;
    public final int rubberBarItemId;
    public final int sewageBucketItemId;
    public final int sludgeBucketItemId;
    public final int mobEssenceBucketItemId;
    public final int syringeEmptyItemId;
    public final int syringeHealthItemId;
    public final int syringeGrowthItemId;
    public final int rawRubberItemId;
    public final int machineBaseItemId;
    public final int safariNetItemId;
    public final int ceramicDyeItemId;
    public final int blankRecordId;
    public final int syringeZombieId;
    public final int safariNetSingleItemId;
    public final int bioFuelItemId;
    public final int bioFuelBucketItemId;
    public final int upgradeItemId;
    public final int safariNetLauncherItemId;
    public final int sugarCharcoalItemId;
    public final int milkBottleItemId;
    public final int spyglassItemId;
    public final int portaSpawnerItemId;
    public final int strawItemId;
    public final int xpExtractorItemId;
    public final int syringeSlimeItemId;
    public final int syringeCureItemId;
    public final int logicCardItemId;
    public final int rednetMeterItemId;
    public final int rednetMemoryCardItemId;
    public final int rulerItemId;
    public final int meatIngotRawItemId;
    public final int meatIngotCookedItemId;
    public final int meatNuggetRawItemId;
    public final int meatNuggetCookedItemId;
    public final int meatBucketItemId;
    public final int pinkSlimeBucketItemId;
    public final int pinkSlimeballItemId;
    public final int safariNetJailerItemId;
    public final int laserFocusItemId;
    public final int chocolateMilkBucketItemId;
    public final int mushroomSoupBucketItemId;
    public final int needlegunItemId;
    public final int needlegunAmmoEmptyItemId;
    public final int needlegunAmmoStandardItemId;
    public final int needlegunAmmoLavaItemId;
    public final int needlegunAmmoSludgeItemId;
    public final int needlegunAmmoSewageItemId;
    public final int needlegunAmmoFireItemId;
    public final int needlegunAmmoAnvilItemId;
    public final int plasticCupItemId;
    public final int rocketLauncherItemId;
    public final int rocketItemId;

    public final int zoolologistEntityId;

    public final boolean colorblindMode;
    public final int treeSearchMaxVertical;
    public final int treeSearchMaxHorizontal;
    public final int verticalHarvestSearchMaxVertical;
    public final boolean enableBonemealFertilizing;
    public final boolean enableCheapDSU;
    public final boolean craftSingleDSU;
    public final boolean enableMossyCobbleRecipe;
    public final boolean conveyorCaptureNonItems;
    public final boolean conveyorNeverCapturesPlayers;
    public final boolean conveyorNeverCapturesTCGolems;
    public final boolean playSounds;
    public final int fruitTreeSearchMaxVertical;
    public final int fruitTreeSearchMaxHorizontal;
    public final int breederShutdownThreshold;
    public final int autospawnerCostStandard;
    public final int autospawnerCostExact;
    public final int laserdrillCost;
    public final boolean meatSaturation;

    public final boolean vanillaOverrideGlassPane;
    public final boolean vanillaOverrideIce;
    public final boolean vanillaOverrideMilkBucket;

    public final boolean enableSlipperyRoads;

    public final String redNetConnectionBlacklist;

    public final boolean redNetDebug;

    public final boolean rubberTreeWorldGen;

    public final boolean mfrLakeWorldGen;
    public final int mfrLakeSewageRarity;
    public final int mfrLakeSludgeRarity;
    public final String rubberTreeBiomeWhitelist;
    public final String rubberTreeBiomeBlacklist;
    public final String worldGenDimensionBlacklist;

    public final int passengerRailSearchMaxHorizontal;
    public final int passengerRailSearchMaxVertical;

    // recipes config
    public final boolean vanillaRecipes;
    public final boolean thermalExpansionRecipes;
    public final boolean gregTechRecipes;

    public static void init(final File clientConfig, final File commonConfig) {
        if (instance != null)
            return;
        instance = new MFRConfig(clientConfig, commonConfig);
    }

    public static MFRConfig getInstance() {
        return instance;
    }

    private MFRConfig(final File clientConfig, final File commonConfig) {
        Configuration c = new Configuration(clientConfig);

        Property _spyglassRange = c.get(Configuration.CATEGORY_GENERAL, "SpyglassRange", 200);
        _spyglassRange.comment = "The maximum number of blocks the spyglass and ruler can look to find something. This calculation is performed only on the client side.";
        spyglassRange = _spyglassRange.getInt();

        c.save();


        c = new Configuration(commonConfig);
        c.load();
        machineBlock0Id = c.getBlock("ID.MachineBlock", 3120).getInt();
        conveyorBlockId = c.getBlock("ID.ConveyorBlock", 3121).getInt();
        rubberWoodBlockId = c.getBlock("ID.RubberWood", 3122).getInt();
        rubberLeavesBlockId = c.getBlock("ID.RubberLeaves", 3123).getInt();
        rubberSaplingBlockId = c.getBlock("ID.RubberSapling", 3124).getInt();
        railDropoffCargoBlockId = c.getBlock("ID.CargoRailDropoffBlock", 3125).getInt();
        railPickupCargoBlockId = c.getBlock("ID.CargoRailPickupBlock", 3126).getInt();
        railDropoffPassengerBlockId = c.getBlock("ID.PassengerRailDropoffBlock", 3127).getInt();
        railPickupPassengerBlockId = c.getBlock("ID.PassengerRailPickupBlock", 3128).getInt();
        factoryGlassBlockId = c.getBlock("ID.StainedGlass", 3129).getInt();
        factoryGlassPaneBlockId = c.getBlock("ID.StainedGlassPane", 3130).getInt();
        machineBlock1Id = c.getBlock("ID.MachineBlock1", 3131).getInt();
        factoryRoadBlockId = c.getBlock("ID.Road", 3132).getInt();
        factoryDecorativeBrickBlockId = c.getBlock("ID.Bricks", 3133).getInt();
        factoryDecorativeStoneBlockId = c.getBlock("ID.Stone", 3134).getInt();
        milkStillBlockId = c.getBlock("ID.Milk.Still", 3135).getInt();
        meatStillBlockId = c.getBlock("ID.Meat.Still", 3136).getInt();
        sludgeStillBlockId = c.getBlock("ID.Sludge.Still", 3137).getInt();
        pinkslimeStillBlockId = c.getBlock("ID.PinkSlime.Still", 3138).getInt();
        sewageStillBlockId = c.getBlock("ID.Sewage.Still", 3139).getInt();
        chocolateMilkStillBlockId = c.getBlock("ID.ChocolateMilk.Still", 3140).getInt();
        essenceStillBlockId = c.getBlock("ID.MobEssence.Still", 3141).getInt();
        mushroomSoupStillBlockId = c.getBlock("ID.MushroomSoup.Still", 3142).getInt();
        biofuelStillBlockId = c.getBlock("ID.BioFuel.Still", 3143).getInt();
        rednetCableBlockId = c.getBlock("ID.RedNet.Cable", 3144).getInt();
        rednetLogicBlockId = c.getBlock("ID.RedNet.Logic", 3145).getInt();
        machineBlock2Id = c.getBlock("ID.MachineBlock2", 3146).getInt();
        fakeLaserBlockId = c.getBlock("ID.FakeLaser", 3147).getInt();
        vineScaffoldBlockId = c.getBlock("ID.VineScaffold", 3148).getInt();
        rednetPanelBlockId = c.getBlock("ID.RedNet.Panel", 3149).getInt();

        hammerItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Hammer", 11987).getInt();
        milkItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Milk", 11988).getInt();
        sludgeItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Sludge", 11989).getInt();
        sewageItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Sewage", 11990).getInt();
        mobEssenceItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MobEssence", 11991).getInt();
        fertilizerItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.FertilizerItem", 11992).getInt();
        plasticSheetItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.PlasticSheet", 11993).getInt();
        rawPlasticItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.RawPlastic", 11994).getInt();
        rubberBarItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.RubberBar", 11995).getInt();
        sewageBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SewageBucket", 11996).getInt();
        sludgeBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SludgeBucket", 11997).getInt();
        mobEssenceBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MobEssenceBucket", 11998).getInt();
        syringeEmptyItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SyringeEmpty", 11999).getInt();
        syringeHealthItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SyringeHealth", 12000).getInt();
        syringeGrowthItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SyringeGrowth", 12001).getInt();
        rawRubberItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.RawRubber", 12002).getInt();
        machineBaseItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MachineBlock", 12003).getInt();
        safariNetItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SafariNet", 12004).getInt();
        ceramicDyeItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.CeramicDye", 12005).getInt();
        blankRecordId = c.getItem(Configuration.CATEGORY_ITEM, "ID.BlankRecord", 12006).getInt();
        syringeZombieId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SyringeZombie", 12007).getInt();
        safariNetSingleItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SafariNetSingleUse", 12008).getInt();
        bioFuelItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.BioFuel", 12009).getInt();
        bioFuelBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.BioFuelBucket", 12010).getInt();
        upgradeItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Upgrade", 12011).getInt();
        safariNetLauncherItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SafariNetLauncher", 12012).getInt();
        sugarCharcoalItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SugarCharcoal", 12013).getInt();
        milkBottleItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MilkBottle", 12014).getInt();
        spyglassItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Spyglass", 12015).getInt();
        portaSpawnerItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.PortaSpawner", 12016).getInt();
        strawItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Straw", 12017).getInt();
        xpExtractorItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.XPExtractor", 12018).getInt();
        syringeSlimeItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SyringeSlime", 12019).getInt();
        syringeCureItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SyringeCure", 12020).getInt();
        logicCardItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Upgrade.PRC", 12021).getInt();
        rednetMeterItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.RedNet.Meter", 12022).getInt();
        rednetMemoryCardItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.RedNet.MemoryCard", 12023).getInt();
        rulerItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Ruler", 12024).getInt();
        meatIngotRawItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MeatIngotRaw", 12025).getInt();
        meatIngotCookedItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MeatIngotCooked", 12026).getInt();
        meatNuggetRawItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MeatNuggetRaw", 12027).getInt();
        meatNuggetCookedItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MeatNuggetCooked", 12028).getInt();
        meatBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MeatBucket", 12029).getInt();
        pinkSlimeBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.PinkSlimeBucket", 12030).getInt();
        pinkSlimeballItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.PinkSlimeball", 12031).getInt();
        safariNetJailerItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.SafariNetJailer", 12032).getInt();
        laserFocusItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.LaserFocus", 12033).getInt();
        chocolateMilkBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.ChocolateMilkBucket", 12034).getInt();
        mushroomSoupBucketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MushroomSoupBucket", 12035).getInt();
        needlegunItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun", 12036).getInt();
        needlegunAmmoEmptyItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Empty", 12037).getInt();
        needlegunAmmoStandardItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Standard", 12038).getInt();
        needlegunAmmoLavaItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Lava", 12039).getInt();
        needlegunAmmoSludgeItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Sludge", 12040).getInt();
        needlegunAmmoSewageItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Sewage", 12041).getInt();
        needlegunAmmoFireItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Fire", 12042).getInt();
        needlegunAmmoAnvilItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.NeedleGun.Ammo.Anvil", 12043).getInt();
        plasticCupItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.PlasticCup", 12044).getInt();
        rocketLauncherItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.RocketLauncher", 12045).getInt();
        rocketItemId = c.getItem(Configuration.CATEGORY_ITEM, "ID.Rocket", 12046).getInt();

        zoolologistEntityId = c.get("Entity", "ID.Zoologist", 330).getInt();

        colorblindMode = c.get(Configuration.CATEGORY_GENERAL, "RedNet.EnableColorblindMode", false, "Set to true to enable the RedNet GUI's colorblind mode.").getBoolean(false);
        treeSearchMaxHorizontal = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.TreeMaxHoriztonal", 8, "When searching for parts of a tree, how far out to the sides (radius) to search").getInt();
        treeSearchMaxVertical = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.TreeMaxVertical", 40, "When searching for parts of a tree, how far up to search").getInt();
        verticalHarvestSearchMaxVertical = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.StackingBlockMaxVertical", 3, "How far upward to search for members of \"stacking\" blocks, like cactus and sugarcane").getInt();
        passengerRailSearchMaxVertical = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.PassengerRailMaxVertical", 2, "When searching for players or dropoff locations, how far up to search").getInt();
        passengerRailSearchMaxHorizontal = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.PassengerRailMaxHorizontal", 3, "When searching for players or dropoff locations, how far out to the sides (radius) to search").getInt();
        rubberTreeWorldGen = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.RubberTree", true, "Whether or not to generate rubber trees during map generation").getBoolean(true);
        mfrLakeWorldGen = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.MFRLakes", true, "Whether or not to generate MFR lakes during map generation").getBoolean(true);
        enableBonemealFertilizing = c.get(Configuration.CATEGORY_GENERAL, "Fertilizer.EnableBonemeal", false, "If true, the fertilizer will use bonemeal as well as MFR fertilizer. Provided for those who want a less work-intensive farm.").getBoolean(false);
        enableCheapDSU = c.get(Configuration.CATEGORY_GENERAL, "DSU.EnableCheaperRecipe", false, "If true, DSU can be built out of chests instead of ender pearls. Does nothing if the DSU recipe is disabled.").getBoolean(false);
        craftSingleDSU = c.get(Configuration.CATEGORY_GENERAL, "DSU.CraftSingle", false, "DSU recipes will always craft one DSU. Does nothing for recipes that already only craft one DSU (cheap mode, GT recipes, etc).").getBoolean(false);
        enableMossyCobbleRecipe = c.get(Configuration.CATEGORY_GENERAL, "EnableMossyCobbleRecipe", true, "If true, mossy cobble can be crafted.").getBoolean(true);
        conveyorCaptureNonItems = c.get(Configuration.CATEGORY_GENERAL, "Conveyor.CaptureNonItems", true, "If false, conveyors will not grab non-item entities. Breaks conveyor mob grinders but makes them safe for golems, etc.").getBoolean(true);
        conveyorNeverCapturesPlayers = c.get(Configuration.CATEGORY_GENERAL, "Conveyor.NeverCapturePlayers", false, "If true, conveyors will NEVER capture players regardless of other settings.").getBoolean(false);
        conveyorNeverCapturesTCGolems = c.get(Configuration.CATEGORY_GENERAL, "Conveyor.NeverCaptureTCGolems", false, "If true, conveyors will NEVER capture Thaumcraft golems regardless of other settings.").getBoolean(false);
        playSounds = c.get(Configuration.CATEGORY_GENERAL, "PlaySounds", true, "Set to false to disable the harvester's sound when a block is harvested.").getBoolean(true);
        enableSlipperyRoads = c.get(Configuration.CATEGORY_GENERAL, "Road.Slippery", true, "If true, roads will be slippery like ice.").getBoolean(true);
        fruitTreeSearchMaxHorizontal = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.FruitTreeMaxHoriztonal", 5, "When searching for parts of a fruit tree, how far out to the sides (radius) to search").getInt();
        fruitTreeSearchMaxVertical = c.get(Configuration.CATEGORY_GENERAL, "SearchDistance.FruitTreeMaxVertical", 20, "When searching for parts of a fruit tree, how far up to search").getInt();
        breederShutdownThreshold = c.get(Configuration.CATEGORY_GENERAL, "Breeder.ShutdownThreshold", 50, "If the number of entities in the breeder's target area exceeds this value, the breeder will cease operating. This is provided to control server lag.").getInt();
        autospawnerCostExact = c.get(Configuration.CATEGORY_GENERAL, "AutoSpawner.Cost.Exact", 50, "The work required to generate a mob in exact mode.").getInt();
        autospawnerCostStandard = c.get(Configuration.CATEGORY_GENERAL, "AutoSpawner.Cost.Standard", 15, "The work required to generate a mob in standard (non-exact) mode.").getInt();
        laserdrillCost = c.get(Configuration.CATEGORY_GENERAL, "LaserDrill.Cost", 300, "The work required by the drill to generate a single ore.").getInt();
        meatSaturation = c.get(Configuration.CATEGORY_GENERAL, "Meat.IncreasedSaturation", false, "If true, meat will be worth steak saturation instead of cookie saturation.").getBoolean(false);

        vanillaOverrideGlassPane = c.get(Configuration.CATEGORY_GENERAL, "VanillaOverride.GlassPanes", true, "If true, allows vanilla glass panes to connect to MFR stained glass panes.").getBoolean(true);
        vanillaOverrideIce = c.get(Configuration.CATEGORY_GENERAL, "VanillaOverride.Ice", true, "If true, enables MFR unmelting ice as well as vanilla ice.").getBoolean(true);
        vanillaOverrideMilkBucket = c.get(Configuration.CATEGORY_GENERAL, "VanillaOverride.MilkBucket", true, "If true, replaces the vanilla milk bucket so milk can be placed in the world.").getBoolean(true);

        redNetDebug = c.get(Configuration.CATEGORY_GENERAL, "RedNet.Debug", false, "If true, RedNet cables will dump a massive amount of data to the log file. You should probably only use this if PC tells you to.").getBoolean(false);

        rubberTreeBiomeWhitelist = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.RubberTreeBiomeWhitelist", "", "A comma-separated list of biomes to allow rubber trees to spawn in. Does nothing if rubber tree worldgen is disabled.").getString();
        rubberTreeBiomeBlacklist = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.RubberTreeBiomeBlacklist", "", "A comma-separated list of biomes to disallow rubber trees to spawn in. Overrides any other biomes added.").getString();
        redNetConnectionBlacklist = c.get(Configuration.CATEGORY_GENERAL, "RedNet.ConnectionBlackList", "", "A comma-separated list of block IDs to prevent RedNet cables from connecting to.").getString();
        worldGenDimensionBlacklist = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.DimensionBlacklist", "", "A comma-separated list of dimension IDs to disable MFR worldgen in. By default, MFR will not attempt worldgen in dimensions where the player cannot respawn.").getString();

        mfrLakeSludgeRarity = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.LakeRarity.Sludge", 32, "Higher numbers make sludge lakes rarer. A value of one will be approximately one per chunk.").getInt();
        mfrLakeSewageRarity = c.get(Configuration.CATEGORY_GENERAL, "WorldGen.LakeRarity.Sewage", 32, "Higher numbers make sewage lakes rarer. A value of one will be approximately one per chunk.").getInt();

        vanillaRecipes = c.get("RecipeSets", "EnableVanillaRecipes", true, "If true, MFR will register its standard (vanilla-item-only) recipes.").getBoolean(true);
        thermalExpansionRecipes = c.get("RecipeSets", "EnableThermalExpansionRecipes", false, "If true, MFR will register its Thermal Expansion-based recipes.").getBoolean(false);
        gregTechRecipes = c.get("RecipeSets", "EnableGregTechRecipes", false, "If true, MFR will register its GregTech-based recipes.").getBoolean(false);

        for (Machine machine : Machine.values())
            machine.load(c);

        c.save();
    }

    public void loadCommonConfig(File configFile) {

    }

}