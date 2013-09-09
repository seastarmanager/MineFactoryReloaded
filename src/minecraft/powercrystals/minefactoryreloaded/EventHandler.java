package powercrystals.minefactoryreloaded;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import powercrystals.minefactoryreloaded.block.BlockRubberSapling;
import powercrystals.minefactoryreloaded.core.GrindingDamage;

import java.util.List;

public class EventHandler {

    @ForgeSubscribe
    public void handleDrops(LivingDropsEvent e) {
        if (e.source instanceof GrindingDamage) {
            GrindingDamage damage = (GrindingDamage) e.source;
            List<EntityItem> drops = e.drops;

            for (EntityItem item : drops)
                damage.grinder.doDrop(item.getEntityItem());
            e.setCanceled(true);
        }
    }

    @ForgeSubscribe
    public void onBonemeal(BonemealEvent e) {
        if (!e.world.isRemote && e.world.getBlockId(e.X, e.Y, e.Z) == MineFactoryReloadedCore.rubberSaplingBlock.blockID) {
            ((BlockRubberSapling) MineFactoryReloadedCore.rubberSaplingBlock).growTree(e.world, e.X, e.Y, e.Z, e.world.rand);
            e.setResult(Event.Result.ALLOW);
        }
    }

    @ForgeSubscribe
    public void onBucketFill(FillBucketEvent e) {
        if (e.current.itemID != Item.bucketEmpty.itemID) {
            return;
        }
        ItemStack filledBucket = fillBucket(e.world, e.target);
        if (filledBucket != null) {
            e.world.setBlockToAir(e.target.blockX, e.target.blockY, e.target.blockZ);
            e.result = filledBucket;
            e.setResult(Event.Result.ALLOW);
        }
    }

    private ItemStack fillBucket(World world, MovingObjectPosition block) {
        int blockId = world.getBlockId(block.blockX, block.blockY, block.blockZ);
        if (blockId == MineFactoryReloadedCore.milkLiquid.blockID) return new ItemStack(Item.bucketMilk);
        else if (blockId == MineFactoryReloadedCore.sludgeLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.sludgeBucketItem);
        else if (blockId == MineFactoryReloadedCore.sewageLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.sewageBucketItem);
        else if (blockId == MineFactoryReloadedCore.essenceLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.mobEssenceBucketItem);
        else if (blockId == MineFactoryReloadedCore.biofuelLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.bioFuelBucketItem);
        else if (blockId == MineFactoryReloadedCore.meatLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.meatBucketItem);
        else if (blockId == MineFactoryReloadedCore.pinkSlimeLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.pinkSlimeBucketItem);
        else if (blockId == MineFactoryReloadedCore.chocolateMilkLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.chocolateMilkBucketItem);
        else if (blockId == MineFactoryReloadedCore.mushroomSoupLiquid.blockID) return new ItemStack(MineFactoryReloadedCore.mushroomSoupBucketItem);
        else return null;
    }
}
