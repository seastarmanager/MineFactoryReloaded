package powercrystals.minefactoryreloaded.block;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercrystals.core.net.PacketHandler;
import powercrystals.core.util.Util;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.gui.MFRCreativeTab;
import powercrystals.minefactoryreloaded.net.NetworkHandler;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

public class BlockFactoryRoad extends Block {
    private Icon _iconRoad;
    private Icon _iconRoadOff;
    private Icon _iconRoadOn;

    public BlockFactoryRoad(int id) {
        super(id, Material.rock);
        if (MFRConfig.getInstance().enableSlipperyRoads) {
            slipperiness = 0.98F;
        }
        setHardness(2.0F);
        setUnlocalizedName("mfr.road");
        setResistance(25.0F);
        setStepSound(soundStoneFootstep);
        setCreativeTab(MFRCreativeTab.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        _iconRoad = par1IconRegister.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName());
        _iconRoadOff = par1IconRegister.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName() + ".light.off");
        _iconRoadOn = par1IconRegister.registerIcon(MineFactoryReloadedCore.modId + ":" + getUnlocalizedName() + ".light.on");
    }

    @Override
    public Icon getIcon(int side, int meta) {
        if (meta == 1 || meta == 3) return _iconRoadOff;
        if (meta == 2 || meta == 4) return _iconRoadOn;
        return _iconRoad;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborId) {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);
            boolean isPowered = Util.isRedstonePowered(world, x, y, z);
            int newMeta = -1;

            if (meta == 1 && isPowered) {
                newMeta = 2;
            } else if (meta == 2 && !isPowered) {
                newMeta = 1;
            } else if (meta == 3 && !isPowered) {
                newMeta = 4;
            } else if (meta == 4 && isPowered) {
                newMeta = 3;
            }

            if (newMeta >= 0) {
                world.setBlockMetadataWithNotify(x, y, z, newMeta, 7);
                PacketDispatcher.sendPacketToAllAround(x, y, z, 50, world.provider.dimensionId,
                        NetworkHandler.getBuilder().startBuild(PacketHandler.PacketType.CUSTOM).append(x).append(y).append(z).append(newMeta).build());
                        //PacketWrapper.createPacket(MineFactoryReloadedCore.modNetworkChannel, Packets.RoadBlockUpdate, new Object[]{x, y, z, newMeta}));
            }
        }
    }

    @Override
    public int damageDropped(int meta) {
        if (meta == 1 || meta == 2) return 1;
        if (meta == 3 || meta == 4) return 4;
        return 0;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta == 2 || meta == 4 ? 15 : 0;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        onNeighborBlockChange(world, x, y, z, 0);
    }
}
