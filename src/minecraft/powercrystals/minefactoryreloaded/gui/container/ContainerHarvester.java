package powercrystals.minefactoryreloaded.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.gui.slot.SlotAcceptUpgrade;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityHarvester;

import java.util.List;

public class ContainerHarvester extends ContainerUpgradable {

    private int fluidID;
    private int fluidAmount;

    public ContainerHarvester(TileEntityHarvester te, InventoryPlayer inv) {
        super(te, inv);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotAcceptUpgrade(_te, 0, 152, 79));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (ICrafting crafter : (List<ICrafting>) crafters) {
            crafter.sendProgressBarUpdate(this, 100, getSetting("silkTouch"));
            crafter.sendProgressBarUpdate(this, 101, getSetting("harvestSmallMushrooms"));
            crafter.sendProgressBarUpdate(this, 102, getSetting("harvestJungleWood"));
            if (_te.getTank() != null && _te.getTank().getFluidAmount() > 0) {
                crafter.sendProgressBarUpdate(this, 103, _te.getTank().getFluidAmount());
                crafter.sendProgressBarUpdate(this, 104, _te.getTank().getFluid().fluidID);
                crafter.sendProgressBarUpdate(this, 105, 0);
            }
        }
    }

    @Override
    public void updateProgressBar(int var, int value) {
        super.updateProgressBar(var, value);

        if (var == 100) setSetting("silkTouch", value);
        if (var == 101) setSetting("harvestSmallMushrooms", value);
        if (var == 102) setSetting("harvestJungleWood", value);
        if (var == 103) fluidAmount = value;
        if (var == 104) fluidID = value;
        if (var == 105) _te.getTank().setFluid(new FluidStack(fluidID, fluidAmount));
    }

    private int getSetting(String setting) {
        TileEntityHarvester h = (TileEntityHarvester) _te;
        if (h.getSettings().get(setting) == null) {
            return 0;
        }
        return h.getSettings().get(setting) ? 1 : 0;
    }

    private void setSetting(String setting, int value) {
        ((TileEntityHarvester) _te).getSettings().put(setting, value != 0);
    }
}
