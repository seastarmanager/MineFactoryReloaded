package powercrystals.minefactoryreloaded.setup.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.block.itemblock.ItemBlockRedNetLogic;
import powercrystals.minefactoryreloaded.setup.MFRConfig;
import powercrystals.minefactoryreloaded.setup.Machine;

public class Vanilla {
    // prevent derived recipe sets from double-registering this one if multiple sets are enabled
    private static boolean _registeredMachines;
    private static boolean _registeredMachineUpgrades;
    private static boolean _registeredConveyors;
    private static boolean _registeredDecorative;
    private static boolean _registeredSyringes;
    private static boolean _registeredMiscItems;
    private static boolean _registeredVanillaImprovements;
    private static boolean _registeredRails;
    private static boolean _registeredGuns;
    private static boolean _registeredRedNet;
    private static boolean _registeredRedNetManual;

    public final void registerRecipes() {
        registerMachines();
        registerMachineUpgrades();
        registerConveyors();
        registerDecorative();
        registerSyringes();
        registerMiscItems();
        registerVanillaImprovements();
        registerRails();
        registerGuns();
        registerRedNet();
        registerRedNetManual();
    }

    protected void registerMachines() {
        if (_registeredMachines) {
            return;
        }
        _registeredMachines = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBaseItem, 3), "PPP",
                "SSS",
                'P', "sheetPlastic",
                'S', Block.stone));

        if (Machine.Planter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 0), "GGG",
                    "CPC",
                    " M ",
                    'G', "sheetPlastic",
                    'P', Block.pistonBase,
                    'C', Item.flowerPot,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Fisher.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 1), "GGG",
                    "RRR",
                    "BMB",
                    'G', "sheetPlastic",
                    'R', Item.fishingRod,
                    'B', Item.bucketEmpty,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Harvester.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 2), "GGG",
                    "SXS",
                    " M ",
                    'G', "sheetPlastic",
                    'X', Item.axeGold,
                    'S', Item.shears,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Rancher.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 3), "GGG",
                    "SBS",
                    " M ",
                    'G', "sheetPlastic",
                    'B', Item.bucketEmpty,
                    'S', Item.shears,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Fertilizer.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 4), "GGG",
                    "LBL",
                    " M ",
                    'G', "sheetPlastic",
                    'L', Item.leather,
                    'B', Item.glassBottle,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Vet.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 5), "GGG",
                    "SSS",
                    "EME",
                    'G', "sheetPlastic",
                    'E', Item.spiderEye,
                    'S', MineFactoryReloadedCore.syringeEmptyItem,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.ItemCollector.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 8, 6), "GGG",
                    " C ",
                    " M ",
                    'G', "sheetPlastic",
                    'C', Block.chest,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.BlockBreaker.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 7), "GGG",
                    "PHS",
                    " M ",
                    'G', "sheetPlastic",
                    'P', Item.pickaxeGold,
                    'H', MineFactoryReloadedCore.factoryHammerItem,
                    'S', Item.shovelGold,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.WeatherCollector.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 8), "GGG",
                    "BBB",
                    "UMU",
                    'G', "sheetPlastic",
                    'B', Block.fenceIron,
                    'U', Item.bucketEmpty,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.SludgeBoiler.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 9), "GGG",
                    "FFF",
                    " M ",
                    'G', "sheetPlastic",
                    'F', Block.furnaceIdle,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Sewer.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 4, 10), "GGG",
                    "BUB",
                    "BMB",
                    'G', "sheetPlastic",
                    'B', Item.brick,
                    'U', Item.bucketEmpty,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Composter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 11), "GGG",
                    "PFP",
                    " M ",
                    'G', "sheetPlastic",
                    'P', Block.pistonBase,
                    'F', Block.furnaceIdle,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Breeder.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 12), "GGG",
                    "CAC",
                    "PMP",
                    'G', "sheetPlastic",
                    'P', new ItemStack(Item.dyePowder, 1, 5),
                    'C', Item.goldenCarrot,
                    'A', Item.appleGold,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Grinder.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 13), "GGG",
                    "BSP",
                    " M ",
                    'G', "sheetPlastic",
                    'P', Block.pistonBase,
                    'B', Item.book,
                    'S', Item.swordGold,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.AutoEnchanter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 14), "GGG",
                    "BBB",
                    "DMD",
                    'G', "sheetPlastic",
                    'B', Item.book,
                    'D', Item.diamond,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Chronotyper.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(0), 1, 15), "GGG",
                    "EEE",
                    "PMP",
                    'G', "sheetPlastic",
                    'E', Item.emerald,
                    'P', new ItemStack(Item.dyePowder, 1, 5),
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Ejector.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 8, 0), "GGG",
                    " D ",
                    "RMR",
                    'G', "sheetPlastic",
                    'D', Block.dispenser,
                    'R', Item.redstone,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.ItemRouter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 8, 1), "GGG",
                    "RCR",
                    " M ",
                    'G', "sheetPlastic",
                    'C', Block.chest,
                    'R', Item.redstoneRepeater,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.LiquidRouter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 8, 2), "GGG",
                    "RBR",
                    "BMB",
                    'G', "sheetPlastic",
                    'B', Item.bucketEmpty,
                    'R', Item.redstoneRepeater,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.DeepStorageUnit.getIsRecipeEnabled()) {
            int dsuCount = MFRConfig.getInstance().craftSingleDSU ? 1 : 4;
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), dsuCount, 3), "GGG",
                    "PPP",
                    "EME",
                    'G', "sheetPlastic",
                    'P', Item.enderPearl,
                    'E', Item.eyeOfEnder,
                    'M', MineFactoryReloadedCore.machineBaseItem));

            if (MFRConfig.getInstance().enableCheapDSU) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 3), "GGG",
                        "CCC",
                        "CMC",
                        'G', "sheetPlastic",
                        'C', Block.chest,
                        'M', MineFactoryReloadedCore.machineBaseItem));
            }
        }

        if (Machine.LiquiCrafter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 4), "GGG",
                    "BWB",
                    "FMF",
                    'G', "sheetPlastic",
                    'B', Item.bucketEmpty,
                    'W', Block.workbench,
                    'F', Item.itemFrame,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.LavaFabricator.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 5), "GGG",
                    "OBO",
                    "CMC",
                    'G', "sheetPlastic",
                    'O', Block.obsidian,
                    'B', Item.blazeRod,
                    'C', Item.magmaCream,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.OilFabricator.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 6), "GGG",
                    "OTO",
                    "OMO",
                    'G', "sheetPlastic",
                    'O', Block.obsidian,
                    'T', Block.tnt,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.AutoJukebox.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 7), "GGG",
                    " J ",
                    " M ",
                    'G', "sheetPlastic",
                    'J', Block.jukebox,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Unifier.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 8), "GGG",
                    "CBC",
                    " M ",
                    'G', "sheetPlastic",
                    'B', Item.book,
                    'C', Item.comparator,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.AutoSpawner.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 9), "GGG",
                    "ECE",
                    "NMS",
                    'G', "sheetPlastic",
                    'E', Item.emerald,
                    'C', Item.magmaCream,
                    'N', Item.netherStalkSeeds,
                    'S', Item.sugar,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.BioReactor.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 10), "GGG",
                    "UEU",
                    "SMS",
                    'G', "sheetPlastic",
                    'U', Item.sugar,
                    'E', Item.fermentedSpiderEye,
                    'S', Item.slimeBall,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.BioFuelGenerator.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 11), "GGG",
                    "PFP",
                    "RMR",
                    'G', "sheetPlastic",
                    'F', Block.furnaceIdle,
                    'P', Block.pistonBase,
                    'R', Item.blazeRod,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.AutoDisenchanter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 12), "GGG",
                    "RDR",
                    "BMB",
                    'G', "sheetPlastic",
                    'B', Item.book,
                    'D', Item.diamond,
                    'R', Block.netherBrick,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.Slaughterhouse.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 13), "GGG",
                    "SSS",
                    "XMX",
                    'G', "sheetPlastic",
                    'S', Item.swordGold,
                    'X', Item.axeGold,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.MeatPacker.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 14), "GGG",
                    "BFB",
                    "BMB",
                    'G', "sheetPlastic",
                    'B', Block.brick,
                    'F', Item.flintAndSteel,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.EnchantmentRouter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(1), 1, 15), "GGG",
                    "RBR",
                    " M ",
                    'G', "sheetPlastic",
                    'B', Item.book,
                    'R', Item.redstoneRepeater,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.LaserDrill.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 0), "GGG",
                    "LLL",
                    "DMD",
                    'G', "sheetPlastic",
                    'L', Block.glowStone,
                    'D', Item.diamond,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.LaserDrillPrecharger.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 1), "GGG",
                    "LSL",
                    "DMD",
                    'G', "sheetPlastic",
                    'L', Block.glowStone,
                    'D', Item.diamond,
                    'S', MineFactoryReloadedCore.pinkSlimeballItem,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.AutoAnvil.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 2), "GGG",
                    "AAA",
                    " M ",
                    'G', "sheetPlastic",
                    'A', Block.anvil,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.BlockSmasher.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 3), "GGG",
                    "HHH",
                    "BMB",
                    'G', "sheetPlastic",
                    'H', MineFactoryReloadedCore.factoryHammerItem,
                    'B', Item.book,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.RedNote.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 4), "GGG",
                    "CNC",
                    " M ",
                    'G', "sheetPlastic",
                    'C', MineFactoryReloadedCore.rednetCableBlock,
                    'N', Block.music,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.AutoBrewer.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 5), "GGG",
                    "CBC",
                    "RMR",
                    'G', "sheetPlastic",
                    'C', Block.chest,
                    'B', Item.brewingStand,
                    'R', Item.redstoneRepeater,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.FruitPicker.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 6), "GGG",
                    "SXS",
                    "SMS",
                    'G', "sheetPlastic",
                    'X', Item.axeGold,
                    'S', Item.shears,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.BlockPlacer.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 6), "GGG",
                    "DDD",
                    " M ",
                    'G', "sheetPlastic",
                    'D', Block.dropper,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }

        if (Machine.MobCounter.getIsRecipeEnabled()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.machineBlocks.get(2), 1, 6), "GGG",
                    "RCR",
                    "SMS",
                    'G', "sheetPlastic",
                    'R', Item.redstoneRepeater,
                    'C', Item.comparator,
                    'S', MineFactoryReloadedCore.spyglassItem,
                    'M', MineFactoryReloadedCore.machineBaseItem));
        }
    }

    protected void registerMachineUpgrades() {
        if (_registeredMachineUpgrades) {
            return;
        }
        _registeredMachineUpgrades = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 0), "III",
                "PPP",
                "RGR",
                'I', "dyeBlue",
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 1), "III",
                "PPP",
                "RGR",
                'I', Item.ingotIron,
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 2), "III",
                "PPP",
                "RGR",
                'I', "ingotTin",
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 3), "III",
                "PPP",
                "RGR",
                'I', "ingotCopper",
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 4), "III",
                "PPP",
                "RGR",
                'I', "ingotBronze",
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 5), "III",
                "PPP",
                "RGR",
                'I', "ingotSilver",
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 6), "III",
                "PPP",
                "RGR",
                'I', Item.ingotGold,
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 7), "III",
                "PPP",
                "RGR",
                'I', Item.netherQuartz,
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 8), "III",
                "PPP",
                "RGR",
                'I', Item.diamond,
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 9), "III",
                "PPP",
                "RGR",
                'I', "ingotPlatinum",
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.upgradeItem, 1, 10), "III",
                "PPP",
                "RGR",
                'I', Item.emerald,
                'P', "dustPlastic",
                'R', Item.redstone,
                'G', Item.goldNugget));

        for (int i = 0; i < 16; i++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.laserFocusItem, 1, i), "ENE",
                    "NGN",
                    "ENE",
                    'E', Item.emerald,
                    'N', Item.goldNugget,
                    'G', new ItemStack(MineFactoryReloadedCore.factoryGlassPaneBlock, 1, i)));
        }
    }

    protected void registerConveyors() {
        if (_registeredConveyors) {
            return;
        }
        _registeredConveyors = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.conveyorBlock, 16, 16), "UUU",
                "RIR",
                'U', "itemRubber",
                'R', Item.redstone,
                'I', Item.ingotIron));

        for (int i = 0; i < 16; i++) {
            GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.conveyorBlock, 1, i), new ItemStack(MineFactoryReloadedCore.conveyorBlock, 1, 16), new ItemStack(MineFactoryReloadedCore.ceramicDyeItem, 1, i));
        }
    }

    protected void registerDecorative() {
        if (_registeredDecorative) {
            return;
        }
        _registeredDecorative = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 16), "BBB",
                "BPB",
                "BBB",
                'P', "sheetPlastic",
                'B', new ItemStack(Block.stoneBrick, 1, 0)));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 4, 1), "R R",
                " G ",
                "R R",
                'R', new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 1, 0),
                'G', Block.redstoneLampIdle);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 1, 4), new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 1, 1), new ItemStack(MineFactoryReloadedCore.factoryRoadBlock, 1, 4));

        for (int i = 0; i < 16; i++) {
            GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.ceramicDyeItem, 4, i), new ItemStack(Item.clay), new ItemStack(Item.dyePowder, 1, 15 - i));
            GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryGlassBlock, 1, i), new ItemStack(Block.glass), new ItemStack(MineFactoryReloadedCore.ceramicDyeItem, 1, i));
            GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryGlassPaneBlock, 1, i), new ItemStack(Block.thinGlass), new ItemStack(MineFactoryReloadedCore.ceramicDyeItem, 1, i));

            GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryGlassPaneBlock, 16, i), "GGG",
                    "GGG",
                    'G', new ItemStack(MineFactoryReloadedCore.factoryGlassBlock, 1, i));
        }


        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 0), "M M",
                " B ",
                "M M",
                'B', Block.brick,
                'M', Block.ice);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 1), "M M",
                " B ",
                "M M",
                'B', Block.brick,
                'M', Block.glowStone);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 2), "M M",
                " B ",
                "M M",
                'B', Block.brick,
                'M', Block.blockLapis);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 3), "M M",
                " B ",
                "M M",
                'B', Block.brick,
                'M', Block.obsidian);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 4), "M M",
                " B ",
                "M M",
                'B', Block.brick,
                'M', new ItemStack(Block.stoneSingleSlab, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 5), "M M",
                " B ",
                "M M",
                'B', Block.brick,
                'M', Block.blockSnow);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 6), "M M",
                " B ",
                "M M",
                'B', Block.stoneBrick,
                'M', Block.glowStone);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 7), "M M",
                " B ",
                "M M",
                'B', Block.stoneBrick,
                'M', Block.ice);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 8), "M M",
                " B ",
                "M M",
                'B', Block.stoneBrick,
                'M', Block.blockLapis);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 9), "M M",
                " B ",
                "M M",
                'B', Block.stoneBrick,
                'M', Block.obsidian);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 8, 10), "M M",
                " B ",
                "M M",
                'B', Block.stoneBrick,
                'M', Block.blockSnow);

        /**
         * Smooth:
         **/
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 0), "SSS",
                "SDS",
                "SSS",
                'S', Block.stone,
                'D', new ItemStack(Item.dyePowder, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 1), "SSS",
                "SDS",
                "SSS",
                'S', Block.stone,
                'D', Item.sugar);

        // cobble->smooth
        int stoneID = MineFactoryReloadedCore.factoryDecorativeStoneBlock.blockID;
        FurnaceRecipes.smelting().addSmelting(stoneID, 2, new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 0), 0.0001F);
        FurnaceRecipes.smelting().addSmelting(stoneID, 3, new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 1), 0.0001F);

        /**
         * Cobble:
         **/
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 2), "SSS",
                "SDS",
                "SSS",
                'S', Block.cobblestone,
                'D', new ItemStack(Item.dyePowder, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 3), "SSS",
                "SDS",
                "SSS",
                'S', Block.cobblestone,
                'D', Item.sugar);

        // meta-sensitive optional override in block code?

        /**
         * Large brick:
         **/
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 4), "SSS",
                "SDS",
                "SSS",
                'S', Block.stoneBrick,
                'D', new ItemStack(Item.dyePowder, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 5), "SSS",
                "SDS",
                "SSS",
                'S', Block.stoneBrick,
                'D', Item.sugar);

        // smooth->large brick
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 4, 4), "SS",
                "SS",
                'S', new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 4, 5), "SS",
                "SS",
                'S', new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 1));

        /**
         * Small brick:
         **/
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 6), "SSS",
                "SDS",
                "SSS",
                'S', Block.brick,
                'D', new ItemStack(Item.dyePowder, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 7), "SSS",
                "SDS",
                "SSS",
                'S', Block.brick,
                'D', Item.sugar);

        // large brick->small brick
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 4, 6), "SS",
                "SS",
                'S', new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 4));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 4, 7), "SS",
                "SS",
                'S', new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 5));

        /**
         * Gravel:
         **/
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 8), "SSS",
                "SDS",
                "SSS",
                'S', Block.gravel,
                'D', new ItemStack(Item.dyePowder, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 8, 9), "SSS",
                "SDS",
                "SSS",
                'S', Block.gravel,
                'D', Item.sugar);

        // FZ grinder?

        /**
         * Paved:
         **/
        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 4, 10), "SSS",
                "SDS",
                "SSS",
                'S', new ItemStack(Block.stoneSingleSlab, 1, 0),
                'D', new ItemStack(Item.dyePowder, 1, 0));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 4, 11), "SSS",
                "SDS",
                "SSS",
                'S', new ItemStack(Block.stoneSingleSlab, 1, 0),
                'D', Item.sugar);

        // smooth->paved
        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 10),
                new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 11),
                new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 1));

        // paved->smooth
        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 0),
                new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 10));
        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 1),
                new ItemStack(MineFactoryReloadedCore.factoryDecorativeStoneBlock, 1, 11));

        // TODO: add white/black sand?

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 1, 12), "MMM",
                "MMM",
                "MMM",
                'M', MineFactoryReloadedCore.meatIngotRawItem);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 1, 13), "MMM",
                "MMM",
                "MMM",
                'M', MineFactoryReloadedCore.meatIngotCookedItem);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.meatIngotRawItem, 9), new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 1, 12));

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.meatIngotCookedItem, 9), new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 1, 13));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.meatIngotRawItem), "MMM",
                "MMM",
                "MMM",
                'M', MineFactoryReloadedCore.meatNuggetRawItem);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.meatIngotCookedItem), "MMM",
                "MMM",
                "MMM",
                'M', MineFactoryReloadedCore.meatNuggetCookedItem);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.meatNuggetRawItem, 9), new ItemStack(MineFactoryReloadedCore.meatIngotRawItem));

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.meatNuggetCookedItem, 9), new ItemStack(MineFactoryReloadedCore.meatIngotCookedItem));
    }

    protected void registerSyringes() {
        if (_registeredSyringes) {
            return;
        }
        _registeredSyringes = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.syringeEmptyItem, 1), "PRP",
                "P P",
                " I ",
                'P', "sheetPlastic",
                'R', "itemRubber",
                'I', Item.ingotIron));

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.syringeHealthItem), MineFactoryReloadedCore.syringeEmptyItem, Item.appleRed);
        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.syringeGrowthItem), MineFactoryReloadedCore.syringeEmptyItem, Item.goldenCarrot);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.syringeZombieItem, 1), "FFF",
                "FSF",
                "FFF",
                'F', Item.rottenFlesh,
                'S', MineFactoryReloadedCore.syringeEmptyItem);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.syringeSlimeItem, 1), "   ",
                " S ",
                "BLB",
                'B', Item.slimeBall,
                'L', new ItemStack(Item.dyePowder, 1, 4),
                'S', MineFactoryReloadedCore.syringeEmptyItem);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.syringeCureItem), MineFactoryReloadedCore.syringeEmptyItem, Item.appleGold);
    }

    protected void registerMiscItems() {
        if (_registeredMiscItems) {
            return;
        }
        _registeredMiscItems = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.plasticSheetItem, 4), "##",
                "##",
                '#', "dustPlastic"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.fertilizerItem, 16), "WBW",
                "STS",
                "WBW",
                'W', Item.wheat,
                'B', new ItemStack(Item.dyePowder, 1, 15),
                'S', Item.silk,
                'T', "stickWood"));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.safariNetItem, 1), " E ",
                "EGE",
                " E ",
                'E', Item.enderPearl,
                'G', Item.ghastTear);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.safariNetSingleItem, 1), "SLS",
                " B ",
                "S S",
                'S', Item.silk,
                'L', Item.leather,
                'B', Item.slimeBall);

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.safariNetJailerItem, 1), " I ",
                "ISI",
                " I ",
                'S', MineFactoryReloadedCore.safariNetSingleItem,
                'I', Block.fenceIron);
        /*
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.safariNetLauncherItem, 1), new Object[]
				{
			"PGP",
			"LGL",
			"IRI",
			'P', "sheetPlastic",
			'L', Item.lightStoneDust,
			'G', Item.gunpowder,
			'I', Item.ingotIron,
			'R', Item.redstone,
				} ));
		*/
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.factoryHammerItem, 1), "PPP",
                " S ",
                " S ",
                'P', "sheetPlastic",
                'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.blankRecordItem, 1), "RRR",
                "RPR",
                "RRR",
                'R', "dustPlastic",
                'P', Item.paper));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.spyglassItem), "GLG",
                "PLP",
                " S ",
                'G', Item.ingotGold,
                'L', Block.glass,
                'P', "sheetPlastic",
                'S', "stickWood"));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.portaSpawnerItem), "GLG",
                "DND",
                "GLG",
                'G', Item.ingotGold,
                'L', Block.glass,
                'D', Item.diamond,
                'N', Item.netherStar);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.strawItem), "PP",
                "P ",
                "P ",
                'P', "sheetPlastic"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.xpExtractorItem), "PLP",
                "PLP",
                "RPR",
                'R', "itemRubber",
                'L', Block.glass,
                'P', "sheetPlastic"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.rulerItem), "P",
                "A",
                "P",
                'P', "sheetPlastic",
                'A', Item.paper));

        GameRegistry.addRecipe(new ItemStack(MineFactoryReloadedCore.vineScaffoldBlock, 8), "VV",
                "VV",
                "VV",
                'V', Block.vine);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.chocolateMilkBucketItem), Item.bucketMilk, Item.bucketEmpty, new ItemStack(Item.dyePowder, 1, 3));
		
		/*GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.emptyPlasticCup, 16), new Object[]
				{
			" P ",
			"P P",
			'P', "sheetPlastic",
				} ));//*/
    }

    protected void registerVanillaImprovements() {
        if (_registeredVanillaImprovements) {
            return;
        }
        _registeredVanillaImprovements = true;

        FurnaceRecipes.smelting().addSmelting(MineFactoryReloadedCore.rawRubberItem.itemID, 0, new ItemStack(MineFactoryReloadedCore.rubberBarItem), 0.1F);
        FurnaceRecipes.smelting().addSmelting(MineFactoryReloadedCore.rubberWoodBlock.blockID, 0, new ItemStack(Item.coal, 1, 1), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(Block.planks, 3, 3), new ItemStack(MineFactoryReloadedCore.rubberWoodBlock));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Block.torchWood, 4), "R",
                "S",
                'R', MineFactoryReloadedCore.rawRubberItem,
                'S', "stickWood"));

        GameRegistry.addRecipe(new ItemStack(Block.pistonStickyBase), "R",
                "P",
                'R', MineFactoryReloadedCore.rawRubberItem,
                'P', Block.pistonBase);

        if (MFRConfig.getInstance().vanillaOverrideIce) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Block.ice, 1, 1), new ItemStack(Block.ice, 1, 0), "dustPlastic"));
        }

        if (MFRConfig.getInstance().enableMossyCobbleRecipe) {
            GameRegistry.addShapelessRecipe(new ItemStack(Block.cobblestoneMossy), Block.cobblestone,
                    Item.bucketWater,
                    Item.wheat);
        }

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.milkBottleItem), Item.bucketMilk,
                Item.glassBottle);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Block.torchWood, 1), "C",
                "S",
                'C', MineFactoryReloadedCore.sugarCharcoalItem,
                'S', "stickWood"));
    }

    protected void registerRails() {
        if (_registeredRails) {
            return;
        }
        _registeredRails = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.railPickupCargoBlock, 2), " C ",
                "SDS",
                "SSS",
                'C', Block.chest,
                'S', "sheetPlastic",
                'D', Block.railDetector));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.railDropoffCargoBlock, 2), "SSS",
                "SDS",
                " C ",
                'C', Block.chest,
                'S', "sheetPlastic",
                'D', Block.railDetector));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.railPickupPassengerBlock, 3), " L ",
                "SDS",
                "SSS",
                'L', Block.blockLapis,
                'S', "sheetPlastic",
                'D', Block.railDetector));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.railDropoffPassengerBlock, 3), "SSS",
                "SDS",
                " L ",
                'L', Block.blockLapis,
                'S', "sheetPlastic",
                'D', Block.railDetector));
    }

    protected void registerGuns() {
        if (_registeredGuns) {
            return;
        }
        _registeredGuns = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.needlegunItem), "PIP",
                "PIP",
                "SLS",
                'P', "sheetPlastic",
                'I', Item.ingotIron,
                'S', Item.slimeBall,
                'L', MineFactoryReloadedCore.safariNetLauncherItem));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoEmptyItem, 4), "P P",
                "PIP",
                "PPP",
                'P', "sheetPlastic",
                'I', Item.ingotIron));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoStandardItem), "AAA",
                "AMA",
                "AAA",
                'A', Item.arrow,
                'M', MineFactoryReloadedCore.needlegunAmmoEmptyItem));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoAnvilItem), "SMS",
                "SAS",
                "SSS",
                'A', new ItemStack(Block.anvil, 1, 0),
                'M', MineFactoryReloadedCore.needlegunAmmoEmptyItem,
                'S', Item.silk));

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoFireItem, 4), MineFactoryReloadedCore.needlegunAmmoEmptyItem,
                Item.flintAndSteel);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoLavaItem), MineFactoryReloadedCore.needlegunAmmoEmptyItem,
                Item.bucketLava);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoSludgeItem), MineFactoryReloadedCore.needlegunAmmoEmptyItem,
                MineFactoryReloadedCore.sludgeBucketItem);

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.needlegunAmmoSewageItem), MineFactoryReloadedCore.needlegunAmmoEmptyItem,
                MineFactoryReloadedCore.sewageBucketItem);
    }

    protected void registerRedNet() {
        if (_registeredRedNet) {
            return;
        }
        _registeredRedNet = true;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.rednetCableBlock, 8), "PPP",
                "RRR",
                "PPP",
                'R', Item.redstone,
                'P', "sheetPlastic"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 1, 11), "PRP",
                "RGR",
                "PIP",
                'R', Item.redstone,
                'P', "sheetPlastic",
                'G', Block.glass,
                'I', Item.ingotIron));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.rednetLogicBlock), "RDR",
                "LGL",
                "PHP",
                'H', new ItemStack(MineFactoryReloadedCore.factoryDecorativeBrickBlock, 1, 11),
                'P', "sheetPlastic",
                'G', Item.ingotGold,
                'L', new ItemStack(Item.dyePowder, 1, 4),
                'D', Item.diamond,
                'R', Item.redstone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.logicCardItem, 1, 0), "RPR",
                "PGP",
                "RPR",
                'P', "sheetPlastic",
                'G', Item.ingotGold,
                'R', Item.redstone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.logicCardItem, 1, 1), "GPG",
                "PCP",
                "RGR",
                'C', new ItemStack(MineFactoryReloadedCore.logicCardItem, 1, 0),
                'P', "sheetPlastic",
                'G', Item.ingotGold,
                'R', Item.redstone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.logicCardItem, 1, 2), "DPD",
                "RCR",
                "GDG",
                'C', new ItemStack(MineFactoryReloadedCore.logicCardItem, 1, 1),
                'P', "sheetPlastic",
                'G', Item.ingotGold,
                'D', Item.diamond,
                'R', Item.redstone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.rednetMeterItem, 1, 0), " G",
                "PR",
                "PP",
                'P', "sheetPlastic",
                'G', Item.goldNugget,
                'R', Item.redstone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.rednetMemoryCardItem, 1, 0), "GGG",
                "PRP",
                "PPP",
                'P', "sheetPlastic",
                'G', Item.goldNugget,
                'R', Item.redstone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MineFactoryReloadedCore.rednetPanelBlock, 1, 0), "PCP",
                "PBP",
                "KPK",
                'P', "sheetPlastic",
                'C', MineFactoryReloadedCore.rednetCableBlock,
                'B', Block.bookShelf,
                'K', new ItemStack(Item.dyePowder, 1, 0)));

        GameRegistry.addShapelessRecipe(new ItemStack(MineFactoryReloadedCore.rednetMemoryCardItem, 1, 0), new ItemStack(MineFactoryReloadedCore.rednetMemoryCardItem, 1, 0));
    }

    private final void registerRedNetManual() {
        if (_registeredRedNetManual) {
            return;
        }
        _registeredRedNetManual = true;

        GameRegistry.addShapelessRecipe(ItemBlockRedNetLogic.manual, MineFactoryReloadedCore.plasticSheetItem, Item.redstone, Item.book);
    }
}
