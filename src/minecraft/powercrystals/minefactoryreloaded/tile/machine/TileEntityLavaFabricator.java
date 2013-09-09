package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidRegistry;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFluidFabricator;

public class TileEntityLavaFabricator extends TileEntityFluidFabricator {
    public TileEntityLavaFabricator() {
        super(FluidRegistry.LAVA.getID(), 20, Machine.LavaFabricator);
    }

    @Override
    public String getGuiBackground() {
        return "lavafab.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiFactoryPowered(getContainer(inventoryPlayer), this);
    }

    @Override
    public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerFactoryPowered(this, inventoryPlayer);
    }
}
