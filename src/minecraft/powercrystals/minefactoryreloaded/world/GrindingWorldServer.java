package powercrystals.minefactoryreloaded.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

import java.util.ArrayList;

public class GrindingWorldServer implements IGrindingWorld {
    protected TileEntityFactoryPowered grinder;
    protected ArrayList<Entity> entitiesToGrind = new ArrayList<Entity>();

    public GrindingWorldServer(TileEntityFactoryPowered grinder) {
        this.grinder = grinder;
    }

    @Override
    public void setAllowSpawns(boolean allow) {
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity) {
        if (grinder != null && entity instanceof EntityItem) {
            if (grinder.manageSolids()) {
                ItemStack drop = ((EntityItem) entity).getEntityItem();
                if (drop != null) grinder.doDrop(drop);
            }
        }
        /*
        else if(allowSpawns)
		{
			//entity.worldObj = this.proxiedWorld;
			//return super.spawnEntityInWorld(entity);
		}
		*/
        entity.setDead();
        return true;
    }

    @Override
    public boolean addEntityForGrinding(Entity entity) {
        /*
		if(entity.worldObj == this) return true;
		if(entity.worldObj == this.proxiedWorld)
		{
			entity.worldObj = this;
			entitiesToGrind.add(entity);
			return true;
		}
		return false;
		*/
        if (!entitiesToGrind.contains(entity))
            entitiesToGrind.add(entity);
        return true;
    }

    @Override
    public void clearReferences() {
        /*
		for(Entity ent : entitiesToGrind)
		{
			if(ent.worldObj == this) ent.worldObj = this.proxiedWorld;
		}
		*/
        entitiesToGrind.clear();
    }

    @Override
    public void cleanReferences() {
        for (int i = entitiesToGrind.size(); i-- > 0; ) {
            Entity ent = entitiesToGrind.get(i);
            if (ent.isDead) entitiesToGrind.remove(ent);
        }
    }

}
