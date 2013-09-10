package powercrystals.minefactoryreloaded.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLaserDrill;

@SideOnly(Side.CLIENT)
public class LaserDrillRenderer extends TileEntitySpecialRenderer {
    static final ResourceLocation loc = new ResourceLocation("textures/entity/beacon_beam.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileEntityLaserDrill laserDrill = (TileEntityLaserDrill) tileEntity;
        if (laserDrill.shouldDrawBeam()) {
            this.bindTexture(loc);
            LaserRendererBase.renderLaser(laserDrill, x, y, z, laserDrill.getBeamHeight(), ForgeDirection.DOWN, partialTicks);
        }
    }
}