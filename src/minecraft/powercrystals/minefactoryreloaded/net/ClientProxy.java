package powercrystals.minefactoryreloaded.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import powercrystals.minefactoryreloaded.MineFactoryReloadedClient;

public class ClientProxy implements IMFRProxy {
    @Override
    public void init() {
        MineFactoryReloadedClient.init();
    }

    @Override
    public void movePlayerToCoordinates(EntityPlayer e, double x, double y, double z) {
        e.setPosition(x, y, z);
    }

    @Override
    @ForgeSubscribe
    public void onPostTextureStitch(TextureStitchEvent.Post e) {
    }
}
