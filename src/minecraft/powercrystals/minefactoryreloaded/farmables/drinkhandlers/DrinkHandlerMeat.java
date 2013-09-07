package powercrystals.minefactoryreloaded.farmables.drinkhandlers;

import net.minecraft.entity.player.EntityPlayer;
import powercrystals.minefactoryreloaded.api.IFluidDrinkHandler;

public class DrinkHandlerMeat implements IFluidDrinkHandler
{
	@Override
	public void onDrink(EntityPlayer player)
	{
		player.heal(4);
		player.getFoodStats().addStats(4, 1.0F);
	}
}
