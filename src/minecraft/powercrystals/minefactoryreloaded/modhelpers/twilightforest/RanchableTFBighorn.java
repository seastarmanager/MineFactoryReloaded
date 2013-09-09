package powercrystals.minefactoryreloaded.modhelpers.twilightforest;

import net.minecraft.entity.EntityLiving;
import powercrystals.minefactoryreloaded.farmables.ranchables.RanchableSheep;

public class RanchableTFBighorn extends RanchableSheep {
    private Class<? extends EntityLiving> _tfBighornClass;

    public RanchableTFBighorn(Class<? extends EntityLiving> tfBighornClass) {
        _tfBighornClass = tfBighornClass;
    }

    @Override
    public Class<? extends EntityLiving> getRanchableEntity() {
        return _tfBighornClass;
    }
}