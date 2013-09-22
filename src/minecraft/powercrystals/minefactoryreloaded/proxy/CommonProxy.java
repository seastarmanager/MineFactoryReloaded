package powercrystals.minefactoryreloaded.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.TextureStitchEvent.Post;

@SuppressWarnings("UnusedDeclaration")
public class CommonProxy implements IMFRProxy {
    @Override
    public void init() {
    }

    @Override
    public void movePlayerToCoordinates(EntityPlayer e, double x, double y, double z) {
        if (e instanceof EntityPlayerMP) {
            ((EntityPlayerMP) e).playerNetServerHandler.setPlayerLocation(x, y, z, e.cameraYaw, e.cameraPitch);
        }
    }

    @Override
    public void onPostTextureStitch(Post e) {
    }
}
