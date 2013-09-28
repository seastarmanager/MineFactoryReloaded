package powercrystals.minefactoryreloaded.modhelpers.forestry.trees;

import forestry.api.arboriculture.ITree;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;

import java.lang.reflect.Method;
import java.util.Random;

public class FertilizableForestryTree implements IFactoryFertilizable {

    private static Block sapling;
    private static Class<?> tileTreeContainer;
    private static Method getTreeInvokable;

    @Override
    public int getFertilizableBlockId() {
        return sapling.blockID;
    }

    @Override
    public boolean canFertilizeBlock(World world, int x, int y, int z, FertilizerType fertilizerType) {
        return world.getBlockId(x, y, z) == this.getFertilizableBlockId();
    }

    @Override
    public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType) {
        if (world.getBlockId(x, y, z) == this.getFertilizableBlockId()) {
            TileEntity t = world.getBlockTileEntity(x, y, z);
            if (tileTreeContainer.isInstance(t)) {
                ITree tree = getTree(t);
                return tree.getTreeGenerator(world, x, y, z, true).generate(world, rand, x, y, z);
            }
        }
        return false;
    }

    public static ITree getTree(TileEntity t) {
        try {
            return (ITree) getTreeInvokable.invoke(t);
        } catch (Throwable ex) {
        }
        return null;
    }

    static {
        try {
            sapling = (Block) Class.forName("forestry.core.config.ForestryBlock").getField("saplingGE").get(null);
            tileTreeContainer = Class.forName("forestry.arboriculture.gadgets.TileTreeContainer");
            getTreeInvokable = tileTreeContainer.getDeclaredMethod("getTree", new Class[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
