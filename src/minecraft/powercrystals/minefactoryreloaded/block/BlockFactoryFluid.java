package powercrystals.minefactoryreloaded.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.core.block.BlockFluidClassic;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.rednet.IConnectableRedNet;
import powercrystals.minefactoryreloaded.api.rednet.RedNetConnectionType;

public class BlockFactoryFluid extends BlockFluidClassic implements IConnectableRedNet {
    private Icon _iconFlowing;
    private Icon _iconStill;

    public BlockFactoryFluid(int id, String fluidName) {
        super(id, Material.water);
        setUnlocalizedName("mfr.fluid." + fluidName + ".still");
        setHardness(100.0f);
        setLightOpacity(3);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (world.isRemote) {
            return;
        }

        if (entity instanceof EntityLivingBase && !((EntityLivingBase) entity).isEntityUndead()) {
            EntityLivingBase ent = (EntityLivingBase) entity;
            if (blockID == MineFactoryReloadedCore.sludgeLiquid.blockID) {
                ent.addPotionEffect(new PotionEffect(Potion.poison.id, 12 * 20, 0));
                ent.addPotionEffect(new PotionEffect(Potion.weakness.id, 12 * 20, 0));
                ent.addPotionEffect(new PotionEffect(Potion.confusion.id, 12 * 20, 0));
            } else if (blockID == MineFactoryReloadedCore.sewageLiquid.blockID) {
                ent.addPotionEffect(new PotionEffect(Potion.hunger.id, 12 * 20, 0));
                ent.addPotionEffect(new PotionEffect(Potion.poison.id, 12 * 20, 0));
                ent.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 12 * 20, 0));
            } else if (blockID == MineFactoryReloadedCore.essenceLiquid.blockID) {
                ent.addPotionEffect(new PotionEffect(Potion.nightVision.id, 60 * 20, 0));
            } else if (blockID == MineFactoryReloadedCore.milkLiquid.blockID) {
                ent.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 6 * 20, 0));
            } else if (blockID == MineFactoryReloadedCore.biofuelLiquid.blockID) {
                ent.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 12 * 20, 0));
            }
        }
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        if (blockID == MineFactoryReloadedCore.essenceLiquid.blockID) {
            return 7;
        } else {
            return 0;
        }
    }

    @Override
    public int getRenderType() {
        return MineFactoryReloadedCore.renderIdFluidClassic;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister ir) {
        _iconStill = ir.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName());
        _iconFlowing = ir.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName().replace(".still", ".flowing"));
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return side <= 1 ? _iconStill : _iconFlowing;
    }

    @Override
    public RedNetConnectionType getConnectionType(World world, int x, int y, int z, ForgeDirection side) {
        return RedNetConnectionType.None;
    }

    @Override
    public int[] getOutputValues(World world, int x, int y, int z, ForgeDirection side) {
        return null;
    }

    @Override
    public int getOutputValue(World world, int x, int y, int z, ForgeDirection side, int subnet) {
        return 0;
    }

    @Override
    public void onInputsChanged(World world, int x, int y, int z, ForgeDirection side, int[] inputValues) {
    }

    @Override
    public void onInputChanged(World world, int x, int y, int z, ForgeDirection side, int inputValue) {
    }
}
