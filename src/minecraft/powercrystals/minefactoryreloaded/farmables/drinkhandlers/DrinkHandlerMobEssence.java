package powercrystals.minefactoryreloaded.farmables.drinkhandlers;

import net.minecraft.entity.player.EntityPlayer;
import powercrystals.minefactoryreloaded.api.IFluidDrinkHandler;

public class DrinkHandlerMobEssence implements IFluidDrinkHandler
{
	@Override
	public void onDrink(EntityPlayer player)
	{
		player.addExperience(player.worldObj.rand.nextInt(5) + 10);
	}
}
