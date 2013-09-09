package powercrystals.minefactoryreloaded.farmables.drinkhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import powercrystals.minefactoryreloaded.api.IFluidDrinkHandler;

public class DrinkHandlerLava implements IFluidDrinkHandler {
    @Override
    public void onDrink(EntityPlayer player) {
        player.attackEntityFrom(new InternalLavaDamage(), 7);
        player.setFire(30);
        NBTTagCompound tag = player.getEntityData();
        tag.setLong("drankLavaTime", player.worldObj.getTotalWorldTime());
    }

    protected class InternalLavaDamage extends DamageSource {
        public InternalLavaDamage() {
            super(DamageSource.lava.damageType);
            this.setDamageBypassesArmor();
            this.setFireDamage();
            this.setDifficultyScaled();
        }
    }
}
