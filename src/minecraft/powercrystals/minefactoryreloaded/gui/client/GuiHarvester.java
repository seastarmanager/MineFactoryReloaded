package powercrystals.minefactoryreloaded.gui.client;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import powercrystals.minefactoryreloaded.gui.container.ContainerHarvester;
import powercrystals.minefactoryreloaded.net.NetworkHandler;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityHarvester;

public class GuiHarvester extends GuiFactoryPowered {
    private TileEntityHarvester _harvester;

    private GuiButton _settingSilkTouch;
    private GuiButton _settingSmallShrooms;
    private GuiButton _settingJungleWood;

    private static final String _silkTouchText = "Shear Leaves: ";
    private static final String _smallShroomsText = "Small Shrooms: ";
    private static final String _jungleWoodText = "Jungle Wood: ";

    public GuiHarvester(ContainerHarvester container, TileEntityHarvester te) {
        super(container, te);
        _harvester = te;
        ySize = 180;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();

        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;

        _settingSilkTouch = new GuiButton(1, xOffset + 7, yOffset + 14, 110, 20, _silkTouchText);
        _settingSmallShrooms = new GuiButton(2, xOffset + 7, yOffset + 34, 110, 20, _smallShroomsText);
        _settingJungleWood = new GuiButton(3, xOffset + 7, yOffset + 54, 110, 20, _jungleWoodText);

        buttonList.add(_settingSilkTouch);
        buttonList.add(_settingSmallShrooms);
        buttonList.add(_settingJungleWood);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        _settingSilkTouch.displayString = _silkTouchText + getSettingText("silkTouch");
        _settingSmallShrooms.displayString = _smallShroomsText + getSettingText("harvestSmallMushrooms");
        _settingJungleWood.displayString = _jungleWoodText + getSettingText("harvestJungleWood");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            PacketDispatcher.sendPacketToServer(NetworkHandler.getBuilder().startBuild(_harvester.xCoord, _harvester.yCoord, _harvester.zCoord)
                                                .append("silkTouch").append(getNewSettingValue("silkTouch")).build());
        } else if (button.id == 2) {
            PacketDispatcher.sendPacketToServer(NetworkHandler.getBuilder().startBuild(_harvester.xCoord, _harvester.yCoord, _harvester.zCoord)
                                                .append("harvestSmallMushrooms").append(getNewSettingValue("harvestSmallMushrooms")).build());
        } else if (button.id == 3) {
            PacketDispatcher.sendPacketToServer(NetworkHandler.getBuilder().startBuild(_harvester.xCoord, _harvester.yCoord, _harvester.zCoord)
                                                .append("harvestJungleWood").append(getNewSettingValue("harvestJungleWood")).build());
        }
    }

    private String getSettingText(String setting) {
        if (_harvester.getSettings().get(setting) == null) return "No";
        return _harvester.getSettings().get(setting) ? "Yes" : "No";
    }

    private Boolean getNewSettingValue(String setting) {
        return _harvester.getSettings().get(setting) == null || !_harvester.getSettings().get(setting);
    }
}
