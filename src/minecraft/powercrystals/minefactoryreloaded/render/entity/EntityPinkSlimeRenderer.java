package powercrystals.minefactoryreloaded.render.entity;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;

public class EntityPinkSlimeRenderer extends RenderSlime {

    private final static ResourceLocation slimeTextures = new ResourceLocation(MineFactoryReloadedCore.mobTextureFolder + "pinkslime.png");

    public EntityPinkSlimeRenderer() {
        super(new ModelSlime(16), new ModelSlime(0), 0.25f);
    }

    @Override
    protected ResourceLocation getSlimeTextures(EntitySlime par1EntitySlime) {
        return slimeTextures;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getSlimeTextures((EntitySlime) entity);
    }
}
