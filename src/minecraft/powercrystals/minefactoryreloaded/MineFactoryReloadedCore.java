package powercrystals.minefactoryreloaded;

import buildcraft.api.transport.FacadeManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.DispenserBehaviors;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.core.mod.BaseMod;
import powercrystals.minefactoryreloaded.block.*;
import powercrystals.minefactoryreloaded.block.itemblock.*;
import powercrystals.minefactoryreloaded.entity.EntityNeedle;
import powercrystals.minefactoryreloaded.entity.EntityPinkSlime;
import powercrystals.minefactoryreloaded.entity.EntityRocket;
import powercrystals.minefactoryreloaded.entity.EntitySafariNet;
import powercrystals.minefactoryreloaded.gui.MFRGUIHandler;
import powercrystals.minefactoryreloaded.item.*;
import powercrystals.minefactoryreloaded.net.ConnectionHandler;
import powercrystals.minefactoryreloaded.net.NetworkHandler;
import powercrystals.minefactoryreloaded.proxy.IMFRProxy;
import powercrystals.minefactoryreloaded.setup.BehaviorDispenseSafariNet;
import powercrystals.minefactoryreloaded.setup.MFRConfig;
import powercrystals.minefactoryreloaded.setup.MineFactoryReloadedFuelHandler;
import powercrystals.minefactoryreloaded.setup.MineFactoryReloadedWorldGen;
import powercrystals.minefactoryreloaded.setup.recipe.GregTech;
import powercrystals.minefactoryreloaded.setup.recipe.ThermalExpansion;
import powercrystals.minefactoryreloaded.setup.recipe.Vanilla;
import powercrystals.minefactoryreloaded.setup.village.VillageCreationHandler;
import powercrystals.minefactoryreloaded.setup.village.VillageTradeHandler;
import powercrystals.minefactoryreloaded.tile.conveyor.TileEntityConveyor;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityUnifier;
import powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetCable;
import powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetHistorian;
import powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Mod(modid = MineFactoryReloadedCore.modId, name = MineFactoryReloadedCore.modName, version = MineFactoryReloadedCore.version,
        dependencies = "required-after:PowerCrystalsCore@[1.1.7,);after:BuildCraft|Core;after:BuildCraft|Factory;after:BuildCraft|Energy;after:BuildCraft|Builders;after:BuildCraft|Transport;after:IC2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class)
public class MineFactoryReloadedCore extends BaseMod {
    @SidedProxy(clientSide = "powercrystals.minefactoryreloaded.proxy.ClientProxy", serverSide = "powercrystals.minefactoryreloaded.proxy.CommonProxy")
    public static IMFRProxy proxy;

    public static final String modId = "MineFactoryReloaded";
    public static final String modNetworkChannel = "MFReloaded";
    public static final String version = "1.6.4R2.7.0B8";
    public static final String modName = "Minefactory Reloaded";

    public static final String guiFolder = modId + ":" + "textures/gui/";
    public static final String villagerFolder = modId + ":" + "textures/villager/";
    public static final String tileEntityFolder = modId + ":" + "textures/tileentity/";
    public static final String mobTextureFolder = modId + ":" + "textures/mob/";

    public static int renderIdConveyor = 1000;
    public static int renderIdFactoryGlassPane = 1001;
    public static int renderIdRedstoneCable = 1002;
    public static int renderIdFluidClassic = 1003;
    public static int renderIdRedNetLogic = 1004;
    public static int renderIdVineScaffold = 1005;
    public static int renderIdRedNetPanel = 1006;
    public static int renderIdFactoryGlass = 1007;

    public static Map<Integer, Block> machineBlocks = new HashMap<Integer, Block>();

    public static Block conveyorBlock;

    public static Block factoryGlassBlock;
    public static Block factoryGlassPaneBlock;
    public static Block factoryRoadBlock;
    public static Block factoryDecorativeBrickBlock;
    public static Block factoryDecorativeStoneBlock;

    public static Block rubberWoodBlock;
    public static Block rubberLeavesBlock;
    public static Block rubberSaplingBlock;

    public static Block railPickupCargoBlock;
    public static Block railDropoffCargoBlock;
    public static Block railPickupPassengerBlock;
    public static Block railDropoffPassengerBlock;

    public static BlockRedNetCable rednetCableBlock;
    public static BlockRedNetLogic rednetLogicBlock;
    public static BlockRedNetPanel rednetPanelBlock;

    public static BlockFactoryFluid milkLiquid;
    public static BlockFactoryFluid sludgeLiquid;
    public static BlockFactoryFluid sewageLiquid;
    public static BlockFactoryFluid essenceLiquid;
    public static BlockFactoryFluid biofuelLiquid;
    public static BlockFactoryFluid meatLiquid;
    public static BlockFactoryFluid pinkSlimeLiquid;
    public static BlockFactoryFluid chocolateMilkLiquid;
    public static BlockFactoryFluid mushroomSoupLiquid;

    public static Block fakeLaserBlock;
    public static Block vineScaffoldBlock;

    public static Item factoryHammerItem;
    public static Item fertilizerItem;
    public static Item plasticSheetItem;
    public static Item rubberBarItem;
    public static Item rawPlasticItem;
    public static Item sewageBucketItem;
    public static Item sludgeBucketItem;
    public static Item mobEssenceBucketItem;
    public static Item syringeEmptyItem;
    public static Item syringeHealthItem;
    public static Item syringeGrowthItem;
    public static Item rawRubberItem;
    public static Item machineBaseItem;
    public static Item safariNetItem;
    public static Item ceramicDyeItem;
    public static Item blankRecordItem;
    public static Item syringeZombieItem;
    public static Item safariNetSingleItem;
    public static Item bioFuelBucketItem;
    public static Item upgradeItem;
    public static Item safariNetLauncherItem;
    public static Item sugarCharcoalItem;
    public static Item milkBottleItem;
    public static Item spyglassItem;
    public static Item portaSpawnerItem;
    public static Item strawItem;
    public static Item xpExtractorItem;
    public static Item syringeSlimeItem;
    public static Item syringeCureItem;
    public static Item logicCardItem;
    public static Item rednetMeterItem;
    public static Item rednetMemoryCardItem;
    public static Item rulerItem;
    public static Item meatIngotRawItem;
    public static Item meatIngotCookedItem;
    public static Item meatNuggetRawItem;
    public static Item meatNuggetCookedItem;
    public static Item meatBucketItem;
    public static Item pinkSlimeBucketItem;
    public static Item pinkSlimeballItem;
    public static Item safariNetJailerItem;
    public static Item laserFocusItem;
    public static Item chocolateMilkBucketItem;
    public static Item mushroomSoupBucketItem;
    public static Item needlegunItem;
    public static Item needlegunAmmoEmptyItem;
    public static Item needlegunAmmoStandardItem;
    public static Item needlegunAmmoLavaItem;
    public static Item needlegunAmmoSludgeItem;
    public static Item needlegunAmmoSewageItem;
    public static Item needlegunAmmoFireItem;
    public static Item needlegunAmmoAnvilItem;
    public static Item rocketLauncherItem;
    public static Item rocketItem;

    public static Fluid milkFluid;
    public static Fluid sludgeFluid;
    public static Fluid sewageFluid;
    public static Fluid essenceFluid;
    public static Fluid biofuelFluid;
    public static Fluid meatFluid;
    public static Fluid pinkSlimeFluid;
    public static Fluid chocolateMilkFluid;
    public static Fluid mushroomSoupFluid;

    public static ItemFactoryCup plasticCup;

    @Mod.Instance(modId)
    public static MineFactoryReloadedCore instance;

    public static MineFactoryReloadedCore instance() {
        return instance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        NetworkHandler.init();
        setConfigFolderBase(evt.getModConfigurationDirectory());

        MFRConfig.loadCommonConfig(getCommonConfig());
        MFRConfig.loadClientConfig(getClientConfig());

        extractLang(new String[]{"en_US", "es_AR", "es_ES", "es_MX", "es_UY", "es_VE", "zh_CN", "zh_TW", "ru_RU", "ko_KR", "de_DE", "sr_SP"});
        loadLang();

        milkLiquid = new BlockFactoryFluid(MFRConfig.milkStillBlockId.getInt(), "milk");
        sludgeLiquid = new BlockFactoryFluid(MFRConfig.sludgeStillBlockId.getInt(), "sludge");
        sewageLiquid = new BlockFactoryFluid(MFRConfig.sewageStillBlockId.getInt(), "sewage");
        essenceLiquid = new BlockFactoryFluid(MFRConfig.essenceStillBlockId.getInt(), "essence");
        biofuelLiquid = new BlockFactoryFluid(MFRConfig.biofuelStillBlockId.getInt(), "biofuel");
        meatLiquid = new BlockFactoryFluid(MFRConfig.meatStillBlockId.getInt(), "meat");
        pinkSlimeLiquid = new BlockPinkSlimeFluid(MFRConfig.pinkslimeStillBlockId.getInt(), "pinkslime");
        chocolateMilkLiquid = new BlockFactoryFluid(MFRConfig.chocolateMilkStillBlockId.getInt(), "chocolatemilk");
        mushroomSoupLiquid = new BlockFactoryFluid(MFRConfig.mushroomSoupStillBlockId.getInt(), "mushroomsoup");

        sewageBucketItem = (new ItemFactoryBucket(MFRConfig.sewageBucketItemId.getInt(), sewageLiquid.blockID)).setUnlocalizedName("mfr.bucket.sewage").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        sludgeBucketItem = (new ItemFactoryBucket(MFRConfig.sludgeBucketItemId.getInt(), sludgeLiquid.blockID)).setUnlocalizedName("mfr.bucket.sludge").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        mobEssenceBucketItem = (new ItemFactoryBucket(MFRConfig.mobEssenceBucketItemId.getInt(), essenceLiquid.blockID)).setUnlocalizedName("mfr.bucket.essence").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        bioFuelBucketItem = (new ItemFactoryBucket(MFRConfig.bioFuelBucketItemId.getInt(), biofuelLiquid.blockID)).setUnlocalizedName("mfr.bucket.biofuel").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        meatBucketItem = (new ItemFactoryBucket(MFRConfig.meatBucketItemId.getInt(), meatLiquid.blockID)).setUnlocalizedName("mfr.bucket.meat").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        pinkSlimeBucketItem = (new ItemFactoryBucket(MFRConfig.pinkSlimeBucketItemId.getInt(), pinkSlimeLiquid.blockID)).setUnlocalizedName("mfr.bucket.pinkslime").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        chocolateMilkBucketItem = (new ItemFactoryBucket(MFRConfig.chocolateMilkBucketItemId.getInt(), chocolateMilkLiquid.blockID)).setUnlocalizedName("mfr.bucket.chocolatemilk").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        mushroomSoupBucketItem = (new ItemFactoryBucket(MFRConfig.mushroomSoupBucketItemId.getInt(), mushroomSoupLiquid.blockID)).setUnlocalizedName("mfr.bucket.mushroomsoup").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);

        final int itemOffset = sewageBucketItem.itemID - MFRConfig.sewageBucketItemId.getInt();
        if (MFRConfig.vanillaOverrideMilkBucket.getBoolean(true)) {
            int milkBucketId = Item.bucketMilk.itemID;
            Item.itemsList[milkBucketId] = null;
            Item.bucketMilk = new ItemFactoryBucket(milkBucketId - itemOffset, milkLiquid.blockID).setUnlocalizedName("mfr.bucket.milk").setMaxStackSize(1).setContainerItem(Item.bucketEmpty);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        final float meatNuggetSaturation = MFRConfig.meatSaturation.getBoolean(false) ? 0.1F : 0.2F;
        final float meatIngotSaturation = MFRConfig.meatSaturation.getBoolean(false) ? 0.2F : 0.8F;

        conveyorBlock               = new BlockConveyor(MFRConfig.conveyorBlockId.getInt());
        factoryGlassBlock           = new BlockFactoryGlass(MFRConfig.factoryGlassBlockId.getInt());
        factoryGlassPaneBlock       = new BlockFactoryGlassPane(MFRConfig.factoryGlassPaneBlockId.getInt());
        factoryRoadBlock            = new BlockFactoryRoad(MFRConfig.factoryRoadBlockId.getInt());
        factoryDecorativeBrickBlock = new BlockFactoryDecorativeBricks(MFRConfig.factoryDecorativeBrickBlockId.getInt());
        factoryDecorativeStoneBlock = new BlockDecorativeStone(MFRConfig.factoryDecorativeStoneBlockId.getInt());
        rubberWoodBlock             = new BlockRubberWood(MFRConfig.rubberWoodBlockId.getInt());
        rubberLeavesBlock           = new BlockRubberLeaves(MFRConfig.rubberLeavesBlockId.getInt());
        rubberSaplingBlock          = new BlockRubberSapling(MFRConfig.rubberSaplingBlockId.getInt());
        railDropoffCargoBlock       = new BlockRailCargoDropoff(MFRConfig.railDropoffCargoBlockId.getInt());
        railPickupCargoBlock        = new BlockRailCargoPickup(MFRConfig.railPickupCargoBlockId.getInt());
        railDropoffPassengerBlock   = new BlockRailPassengerDropoff(MFRConfig.railDropoffPassengerBlockId.getInt());
        railPickupPassengerBlock    = new BlockRailPassengerPickup(MFRConfig.railPickupPassengerBlockId.getInt());
        rednetCableBlock            = new BlockRedNetCable(MFRConfig.rednetCableBlockId.getInt());
        rednetLogicBlock            = new BlockRedNetLogic(MFRConfig.rednetLogicBlockId.getInt());
        rednetPanelBlock            = new BlockRedNetPanel(MFRConfig.rednetPanelBlockId.getInt());
        fakeLaserBlock              = new BlockFakeLaser(MFRConfig.fakeLaserBlockId.getInt());
        vineScaffoldBlock           = new BlockVineScaffold(MFRConfig.vineScaffoldBlockId.getInt());
        machineBlocks.put(0, new BlockFactoryMachine(MFRConfig.machineBlock0Id.getInt(), 0));
        machineBlocks.put(1, new BlockFactoryMachine(MFRConfig.machineBlock1Id.getInt(), 1));
        machineBlocks.put(2, new BlockFactoryMachine(MFRConfig.machineBlock2Id.getInt(), 2));

        factoryHammerItem         = (new ItemFactoryHammer(MFRConfig.hammerItemId.getInt())).setUnlocalizedName("mfr.hammer").setMaxStackSize(1);
        fertilizerItem            = (new ItemFactory(MFRConfig.fertilizerItemId.getInt())).setUnlocalizedName("mfr.fertilizer");
        plasticSheetItem          = (new ItemFactory(MFRConfig.plasticSheetItemId.getInt())).setUnlocalizedName("mfr.plastic.sheet");
        rawPlasticItem            = (new ItemFactory(MFRConfig.rawPlasticItemId.getInt())).setUnlocalizedName("mfr.plastic.raw");
        rubberBarItem             = (new ItemFactory(MFRConfig.rubberBarItemId.getInt())).setUnlocalizedName("mfr.rubber.bar");
        syringeEmptyItem          = (new ItemFactory(MFRConfig.syringeEmptyItemId.getInt())).setUnlocalizedName("mfr.syringe.empty");
        rawRubberItem             = (new ItemFactory(MFRConfig.rawRubberItemId.getInt())).setUnlocalizedName("mfr.rubber.raw");
        machineBaseItem           = (new ItemFactory(MFRConfig.machineBaseItemId.getInt())).setUnlocalizedName("mfr.machineblock");
        blankRecordItem           = (new ItemFactory(MFRConfig.blankRecordId.getInt())).setUnlocalizedName("mfr.record.blank").setMaxStackSize(1);
        pinkSlimeballItem         = (new ItemFactory(MFRConfig.pinkSlimeballItemId.getInt())).setUnlocalizedName("mfr.pinkslimeball");
        sugarCharcoalItem         = (new ItemFactory(MFRConfig.sugarCharcoalItemId.getInt())).setUnlocalizedName("mfr.sugarcharcoal");

        meatIngotRawItem          = (new ItemFactoryFood(MFRConfig.meatIngotRawItemId.getInt(), 4, meatIngotSaturation)).setUnlocalizedName("mfr.meat.ingot.raw");
        meatIngotCookedItem       = (new ItemFactoryFood(MFRConfig.meatIngotCookedItemId.getInt(), 10, meatIngotSaturation)).setUnlocalizedName("mfr.meat.ingot.cooked");
        meatNuggetRawItem         = (new ItemFactoryFood(MFRConfig.meatNuggetRawItemId.getInt(), 1, meatNuggetSaturation)).setUnlocalizedName("mfr.meat.nugget.raw");
        meatNuggetCookedItem      = (new ItemFactoryFood(MFRConfig.meatNuggetCookedItemId.getInt(), 4, meatNuggetSaturation)).setUnlocalizedName("mfr.meat.nugget.cooked");

        syringeHealthItem         = (new ItemSyringeHealth()).setUnlocalizedName("mfr.syringe.health").setContainerItem(syringeEmptyItem);
        syringeGrowthItem         = (new ItemSyringeGrowth()).setUnlocalizedName("mfr.syringe.growth").setContainerItem(syringeEmptyItem);
        syringeSlimeItem          = (new ItemSyringeSlime(MFRConfig.syringeSlimeItemId.getInt())).setUnlocalizedName("mfr.syringe.slime").setContainerItem(syringeEmptyItem);
        syringeZombieItem         = (new ItemSyringeZombie()).setUnlocalizedName("mfr.syringe.zombie").setContainerItem(syringeEmptyItem);
        syringeCureItem           = (new ItemSyringeCure(MFRConfig.syringeCureItemId.getInt())).setUnlocalizedName("mfr.syringe.cure").setContainerItem(syringeEmptyItem);

        needlegunItem             = (new ItemNeedleGun(MFRConfig.needlegunItemId.getInt())).setUnlocalizedName("mfr.needlegun").setMaxStackSize(1);
        needlegunAmmoEmptyItem    = (new ItemFactory(MFRConfig.needlegunAmmoEmptyItemId.getInt())).setUnlocalizedName("mfr.needlegun.ammo.empty");
        needlegunAmmoStandardItem = (new ItemNeedlegunAmmoStandard(MFRConfig.needlegunAmmoStandardItemId.getInt())).setUnlocalizedName("mfr.needlegun.ammo.standard");
        needlegunAmmoLavaItem     = (new ItemNeedlegunAmmoBlock(MFRConfig.needlegunAmmoLavaItemId.getInt(), Block.lavaMoving.blockID, 3)).setUnlocalizedName("mfr.needlegun.ammo.lava");
        needlegunAmmoSludgeItem   = (new ItemNeedlegunAmmoBlock(MFRConfig.needlegunAmmoSludgeItemId.getInt(), sludgeLiquid.blockID, 6)).setUnlocalizedName("mfr.needlegun.ammo.sludge");
        needlegunAmmoSewageItem   = (new ItemNeedlegunAmmoBlock(MFRConfig.needlegunAmmoSewageItemId.getInt(), sewageLiquid.blockID, 6)).setUnlocalizedName("mfr.needlegun.ammo.sewage");
        needlegunAmmoFireItem     = (new ItemNeedlegunAmmoFire(MFRConfig.needlegunAmmoFireItemId.getInt())).setUnlocalizedName("mfr.needlegun.ammo.fire");
        needlegunAmmoAnvilItem    = (new ItemNeedlegunAmmoBlock(MFRConfig.needlegunAmmoAnvilItemId.getInt(), Block.anvil.blockID, 2)).setUnlocalizedName("mfr.needlegun.ammo.anvil").setMaxDamage(0);

        safariNetItem             = (new ItemSafariNet(MFRConfig.safariNetItemId.getInt())).setUnlocalizedName("mfr.safarinet.reusable");
        safariNetSingleItem       = (new ItemSafariNet(MFRConfig.safariNetSingleItemId.getInt())).setUnlocalizedName("mfr.safarinet.singleuse");
        safariNetLauncherItem     = (new ItemSafariNetLauncher(MFRConfig.safariNetLauncherItemId.getInt())).setUnlocalizedName("mfr.safarinet.launcher").setMaxStackSize(1);
        safariNetJailerItem       = (new ItemSafariNet(MFRConfig.safariNetJailerItemId.getInt())).setUnlocalizedName("mfr.safarinet.jailer");

        logicCardItem             = (new ItemLogicUpgradeCard(MFRConfig.logicCardItemId.getInt())).setUnlocalizedName("mfr.upgrade.logic").setMaxStackSize(1);
        rednetMeterItem           = (new ItemRedNetMeter(MFRConfig.rednetMeterItemId.getInt())).setUnlocalizedName("mfr.rednet.meter").setMaxStackSize(1);
        rednetMemoryCardItem      = (new ItemRedNetMemoryCard(MFRConfig.rednetMemoryCardItemId.getInt())).setUnlocalizedName("mfr.rednet.memorycard").setMaxStackSize(1);

        rocketLauncherItem        = (new ItemRocketLauncher(MFRConfig.rocketLauncherItemId.getInt())).setUnlocalizedName("mfr.rocketlauncher").setMaxStackSize(1);
        rocketItem                = (new ItemRocket(MFRConfig.rocketItemId.getInt())).setUnlocalizedName("mfr.rocket").setMaxStackSize(16);

        ceramicDyeItem            = (new ItemCeramicDye(MFRConfig.ceramicDyeItemId.getInt())).setUnlocalizedName("mfr.ceramicdye");
        upgradeItem               = (new ItemUpgrade(MFRConfig.upgradeItemId.getInt())).setUnlocalizedName("mfr.upgrade.radius").setMaxStackSize(1);
        milkBottleItem            = (new ItemMilkBottle(MFRConfig.milkBottleItemId.getInt())).setUnlocalizedName("mfr.milkbottle").setMaxStackSize(16);
        spyglassItem              = (new ItemSpyglass(MFRConfig.spyglassItemId.getInt())).setUnlocalizedName("mfr.spyglass").setMaxStackSize(1);
        portaSpawnerItem          = (new ItemPortaSpawner(MFRConfig.portaSpawnerItemId.getInt())).setUnlocalizedName("mfr.portaspawner").setMaxStackSize(1);
        strawItem                 = (new ItemStraw(MFRConfig.strawItemId.getInt())).setUnlocalizedName("mfr.straw").setMaxStackSize(1);
        xpExtractorItem           = (new ItemXpExtractor(MFRConfig.xpExtractorItemId.getInt())).setUnlocalizedName("mfr.xpextractor").setMaxStackSize(1);
        rulerItem                 = (new ItemRuler(MFRConfig.rulerItemId.getInt())).setUnlocalizedName("mfr.ruler").setMaxStackSize(1);
        laserFocusItem            = (new ItemLaserFocus(MFRConfig.laserFocusItemId.getInt())).setUnlocalizedName("mfr.laserfocus").setMaxStackSize(1);
        plasticCup                = (new ItemFactoryCup(MFRConfig.plasticCupItemId.getInt(), 64, 16).setUnlocalizedName("mfr.bucket.plasticcup"));

        for (Entry<Integer, Block> machine : machineBlocks.entrySet())
            GameRegistry.registerBlock(machine.getValue(), ItemBlockFactoryMachine.class, machine.getValue().getUnlocalizedName());

        GameRegistry.registerBlock(conveyorBlock, ItemBlockConveyor.class, conveyorBlock.getUnlocalizedName());
        GameRegistry.registerBlock(factoryGlassBlock, ItemBlockFactoryGlass.class, factoryGlassBlock.getUnlocalizedName());
        GameRegistry.registerBlock(factoryGlassPaneBlock, ItemBlockFactoryGlassPane.class, factoryGlassPaneBlock.getUnlocalizedName());
        GameRegistry.registerBlock(factoryRoadBlock, ItemBlockFactoryRoad.class, factoryRoadBlock.getUnlocalizedName());
        GameRegistry.registerBlock(factoryDecorativeBrickBlock, ItemBlockFactoryDecorativeBrick.class, factoryDecorativeBrickBlock.getUnlocalizedName());
        GameRegistry.registerBlock(factoryDecorativeStoneBlock, ItemBlockDecorativeStone.class, factoryDecorativeStoneBlock.getUnlocalizedName());
        GameRegistry.registerBlock(rubberWoodBlock, rubberWoodBlock.getUnlocalizedName());
        GameRegistry.registerBlock(rubberLeavesBlock, rubberLeavesBlock.getUnlocalizedName());
        GameRegistry.registerBlock(rubberSaplingBlock, rubberSaplingBlock.getUnlocalizedName());
        GameRegistry.registerBlock(railPickupCargoBlock, railPickupCargoBlock.getUnlocalizedName());
        GameRegistry.registerBlock(railDropoffCargoBlock, railDropoffCargoBlock.getUnlocalizedName());
        GameRegistry.registerBlock(railPickupPassengerBlock, railPickupPassengerBlock.getUnlocalizedName());
        GameRegistry.registerBlock(railDropoffPassengerBlock, railDropoffPassengerBlock.getUnlocalizedName());
        GameRegistry.registerBlock(rednetCableBlock, rednetCableBlock.getUnlocalizedName());
        GameRegistry.registerBlock(rednetLogicBlock, ItemBlockRedNetLogic.class, rednetLogicBlock.getUnlocalizedName());
        GameRegistry.registerBlock(rednetPanelBlock, ItemBlockRedNetPanel.class, rednetPanelBlock.getUnlocalizedName());
        GameRegistry.registerBlock(vineScaffoldBlock, ItemBlockVineScaffold.class, vineScaffoldBlock.getUnlocalizedName());

        GameRegistry.registerBlock(milkLiquid, milkLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(sludgeLiquid, sludgeLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(sewageLiquid, sewageLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(essenceLiquid, essenceLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(biofuelLiquid, biofuelLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(meatLiquid, meatLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(pinkSlimeLiquid, pinkSlimeLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(chocolateMilkLiquid, chocolateMilkLiquid.getUnlocalizedName());
        GameRegistry.registerBlock(mushroomSoupLiquid, mushroomSoupLiquid.getUnlocalizedName());

        Block.setBurnProperties(rubberWoodBlock.blockID, 4, 20);
        Block.setBurnProperties(rubberLeavesBlock.blockID, 30, 20);

        MinecraftForge.setBlockHarvestLevel(MineFactoryReloadedCore.rednetCableBlock, 0, "pickaxe", 0);

        if (MFRConfig.vanillaOverrideGlassPane.getBoolean(true)) {
            Block.blocksList[Block.thinGlass.blockID] = null;
            Item.itemsList[Block.thinGlass.blockID] = null;
            Block.thinGlass = new BlockVanillaGlassPane();
            GameRegistry.registerBlock(Block.thinGlass, Block.thinGlass.getUnlocalizedName());
        }
        if (MFRConfig.vanillaOverrideIce.getBoolean(true)) {
            Block.blocksList[Block.ice.blockID] = null;
            Item.itemsList[Block.ice.blockID] = null;
            Block.ice = new BlockVanillaIce();
            GameRegistry.registerBlock(Block.ice, ItemBlockVanillaIce.class, "blockVanillaIce");
        }

        GameRegistry.registerTileEntity(TileEntityConveyor.class, "factoryConveyor");
        GameRegistry.registerTileEntity(TileEntityRedNetCable.class, "factoryRedstoneCable");
        GameRegistry.registerTileEntity(TileEntityRedNetLogic.class, "factoryRednetLogic");
        GameRegistry.registerTileEntity(TileEntityRedNetHistorian.class, "factoryRednetHistorian");

        EntityRegistry.registerModEntity(EntitySafariNet.class, "entitySafariNet", 0, instance, 160, 5, true);
        EntityRegistry.registerModEntity(EntityPinkSlime.class, "mfrEntityPinkSlime", 1, instance, 160, 5, true);
        EntityRegistry.registerModEntity(EntityNeedle.class, "mfrEntityNeedle", 2, instance, 160, 5, true);
        EntityRegistry.registerModEntity(EntityRocket.class, "mfrEntityRocket", 3, instance, 160, 1, true);

        OreDictionary.registerOre("itemRubber", MineFactoryReloadedCore.rubberBarItem);
        OreDictionary.registerOre("woodRubber", MineFactoryReloadedCore.rubberWoodBlock);
        OreDictionary.registerOre("sheetPlastic", MineFactoryReloadedCore.plasticSheetItem);
        OreDictionary.registerOre("dustPlastic", MineFactoryReloadedCore.rawPlasticItem);
        OreDictionary.registerOre("ingotMeat", MineFactoryReloadedCore.meatIngotCookedItem);
        OreDictionary.registerOre("ingotMeatRaw", MineFactoryReloadedCore.meatIngotRawItem);
        OreDictionary.registerOre("nuggetMeat", MineFactoryReloadedCore.meatNuggetCookedItem);
        OreDictionary.registerOre("nuggetMeatRaw", MineFactoryReloadedCore.meatNuggetRawItem);
        OreDictionary.registerOre("itemCharcoalSugar", MineFactoryReloadedCore.sugarCharcoalItem);
        OreDictionary.registerOre("cableRedNet", MineFactoryReloadedCore.rednetCableBlock);

        proxy.init();
        GameRegistry.registerFuelHandler(new MineFactoryReloadedFuelHandler());
        NetworkRegistry.instance().registerGuiHandler(this, new MFRGUIHandler());

        IBehaviorDispenseItem behavior = new BehaviorDispenseSafariNet();
        BlockDispenser.dispenseBehaviorRegistry.putObject(safariNetItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(safariNetSingleItem, behavior);

        DispenserBehaviors.registerDispenserBehaviours(); // Work around to make the below behavior actually /exist/ before the server starts.
        behavior = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry.getObject(Item.bucketWater);
        BlockDispenser.dispenseBehaviorRegistry.putObject(sewageBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(sludgeBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(mobEssenceBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(bioFuelBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(meatBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(pinkSlimeBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(chocolateMilkBucketItem, behavior);
        BlockDispenser.dispenseBehaviorRegistry.putObject(mushroomSoupBucketItem, behavior);

        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(safariNetSingleItem), 1, 1, 25));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(safariNetSingleItem), 1, 1, 25));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(safariNetSingleItem), 1, 1, 25));

        VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreationHandler());

        VillagerRegistry.instance().registerVillageTradeHandler(MFRConfig.zoolologistEntityId.getInt(), new VillageTradeHandler());

        MinecraftForge.EVENT_BUS.register(new powercrystals.minefactoryreloaded.EventHandler());
        MinecraftForge.EVENT_BUS.register(instance);
        GameRegistry.registerWorldGenerator(new MineFactoryReloadedWorldGen());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        FluidRegistry.registerFluid(milkFluid = new Fluid("milk").setUnlocalizedName(milkLiquid.getUnlocalizedName()).setBlockID(milkLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(milkFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Item.bucketMilk), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(sludgeFluid = new Fluid("sludge").setUnlocalizedName(sludgeLiquid.getUnlocalizedName()).setBlockID(sludgeLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(sludgeFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(sludgeBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(sewageFluid = new Fluid("sewage").setUnlocalizedName(sewageLiquid.getUnlocalizedName()).setBlockID(sewageLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(sewageFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(sewageBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(essenceFluid = new Fluid("essence").setUnlocalizedName(essenceLiquid.getUnlocalizedName()).setBlockID(essenceLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(essenceFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(mobEssenceBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(biofuelFluid = new Fluid("biofuel").setUnlocalizedName(biofuelLiquid.getUnlocalizedName()).setBlockID(biofuelLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(biofuelFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bioFuelBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(meatFluid = new Fluid("meat").setUnlocalizedName(meatLiquid.getUnlocalizedName()).setBlockID(meatLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(meatFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(meatBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(pinkSlimeFluid = new Fluid("pinkslime").setUnlocalizedName(pinkSlimeLiquid.getUnlocalizedName()).setBlockID(pinkSlimeLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(pinkSlimeFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(pinkSlimeBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(chocolateMilkFluid = new Fluid("chocolatemilk").setUnlocalizedName(chocolateMilkLiquid.getUnlocalizedName()).setBlockID(chocolateMilkLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(chocolateMilkFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(chocolateMilkBucketItem), new ItemStack(Item.bucketEmpty)));

        FluidRegistry.registerFluid(mushroomSoupFluid = new Fluid("mushroomsoup").setUnlocalizedName(mushroomSoupLiquid.getUnlocalizedName()).setBlockID(mushroomSoupLiquid));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(mushroomSoupFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(mushroomSoupBucketItem), new ItemStack(Item.bucketEmpty)));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(mushroomSoupFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Item.bowlSoup), new ItemStack(Item.bowlEmpty)));

        TileEntityUnifier.updateUnifierFluids();

        for (ItemStack s : OreDictionary.getOres("itemRubber"))
            FurnaceRecipes.smelting().addSmelting(s.itemID, s.getItemDamage(), new ItemStack(rawPlasticItem), 0.3F);

        FurnaceRecipes.smelting().addSmelting(Item.sugar.itemID, new ItemStack(sugarCharcoalItem), 0.1F);
        FurnaceRecipes.smelting().addSmelting(meatIngotRawItem.itemID, new ItemStack(meatIngotCookedItem), 0.5F);
        FurnaceRecipes.smelting().addSmelting(meatNuggetRawItem.itemID, new ItemStack(meatNuggetCookedItem), 0.3F);

        String[] biomeWhitelist = MFRConfig.rubberTreeBiomeWhitelist.getString().split(",");
        for (String biome : biomeWhitelist) {
            MFRRegistry.registerRubberTreeBiome(biome);
        }

        String[] biomeBlacklist = MFRConfig.rubberTreeBiomeBlacklist.getString().split(",");
        for (String biome : biomeBlacklist)
            MFRRegistry.getRubberTreeBiomes().remove(biome);

        if (MFRConfig.vanillaRecipes.getBoolean(true))
            new Vanilla().registerRecipes();

        if (MFRConfig.thermalExpansionRecipes.getBoolean(false))
            new ThermalExpansion().registerRecipes();

        if (MFRConfig.gregTechRecipes.getBoolean(false))
            new GregTech().registerRecipes();

        for (int i = 0; i < 14; i++)
            FacadeManager.addFacade(new ItemStack(factoryDecorativeBrickBlock.blockID, 1, i));

        for (int i = 0; i < 12; i++)
            FacadeManager.addFacade(new ItemStack(factoryDecorativeStoneBlock.blockID, 1, i));

        FacadeManager.addFacade(new ItemStack(factoryRoadBlock.blockID, 1, 0));
        FacadeManager.addFacade(new ItemStack(factoryRoadBlock.blockID, 1, 1));
        FacadeManager.addFacade(new ItemStack(factoryRoadBlock.blockID, 1, 4));
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void textureHook(TextureStitchEvent.Post event) {
        if (event.map.textureType == 0) {
            milkFluid.setIcons(milkLiquid.getIcon(1, 0), milkLiquid.getIcon(2, 0));
            sludgeFluid.setIcons(sludgeLiquid.getIcon(1, 0), sludgeLiquid.getIcon(2, 0));
            sewageFluid.setIcons(sewageLiquid.getIcon(1, 0), sewageLiquid.getIcon(2, 0));
            essenceFluid.setIcons(essenceLiquid.getIcon(1, 0), essenceLiquid.getIcon(2, 0));
            biofuelFluid.setIcons(biofuelLiquid.getIcon(1, 0), biofuelLiquid.getIcon(2, 0));
            meatFluid.setIcons(meatLiquid.getIcon(1, 0), meatLiquid.getIcon(2, 0));
            pinkSlimeFluid.setIcons(pinkSlimeLiquid.getIcon(1, 0), pinkSlimeLiquid.getIcon(2, 0));
            chocolateMilkFluid.setIcons(chocolateMilkLiquid.getIcon(1, 0), chocolateMilkLiquid.getIcon(2, 0));
            mushroomSoupFluid.setIcons(mushroomSoupLiquid.getIcon(1, 0), mushroomSoupLiquid.getIcon(2, 0));
        }
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public String getModName() {
        return modName;
    }

    @Override
    public String getModVersion() {
        return version;
    }
}
