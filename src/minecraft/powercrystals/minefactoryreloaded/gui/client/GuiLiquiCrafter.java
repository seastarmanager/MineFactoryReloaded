package powercrystals.minefactoryreloaded.gui.client;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.gui.container.ContainerLiquiCrafter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLiquiCrafter;

public class GuiLiquiCrafter extends GuiFactoryInventory {
    private static ResourceLocation loc = new ResourceLocation(MineFactoryReloadedCore.guiFolder + "liquicrafter.png");

    private ResourceLocation tile_loc;
    private TileEntityLiquiCrafter _crafter;

    public GuiLiquiCrafter(ContainerLiquiCrafter container, TileEntityLiquiCrafter router) {
        super(container, router);
        _crafter = router;
        xSize = 231;
        ySize = 214;
        tile_loc = new ResourceLocation(MineFactoryReloadedCore.guiFolder + _tileEntity.getGuiBackground());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString("Template", 67, 27, 4210752);
        fontRenderer.drawString("Output", 128, 26, 4210752);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FluidTank tank = _crafter.getTank();
        if (tank != null) {
            FluidStack l = tank.getFluid();
            if (l != null) {
                for (int i = 0; i < 9; i++)
                    drawTank(-50 + (i % 3 * 18), 43 + (i / 3 * 35), l.amount * 33 / _crafter.getTanks()[i].getCapacity(), _crafter.getTank().getFluid());
            }
        }
        this.mc.renderEngine.bindTexture(loc);
        for (int i = 0; i < 8; i++) {
            this.drawTexturedModalRect(-50 + (i % 3 * 18), 10 + (i / 3 * 35), 232, 0, 16, 33);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float gameTicks, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(MineFactoryReloadedCore.guiFolder + _tileEntity.getGuiBackground()));
        int x = (width - xSize) / 2 - 56;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawTank(int xOffset, int yOffset, int level, FluidStack stack) {
        if (stack == null) {
            return;
        }
        Icon icon = stack.getFluid().getStillIcon();
        if (icon == null)
            icon = Block.lavaMoving.getIcon(0, 0);

        int vertOffset = 0;

        if (fluid == null) {
            String name = icon.getIconName() + ".png";
            if (name.contains(":")) {
                String[] split = name.split(":");
                fluid = new ResourceLocation(split[0] + ":textures/blocks/" + split[1]);
            } else
                fluid = new ResourceLocation(name);
        }

        while (level > 0) {
            int texHeight;

            if (level > 16) {
                texHeight = 16;
                level -= 16;
            } else {
                texHeight = level;
                level = 0;
            }

            drawTexturedModelRectFromIcon(xOffset, yOffset - texHeight - vertOffset, icon, 16, texHeight);
            vertOffset = vertOffset + 16;
        }

        this.mc.renderEngine.bindTexture(tile_loc);
        this.drawTexturedModalRect(xOffset, yOffset - 33, 232, 0, 16, 33);
    }
}
