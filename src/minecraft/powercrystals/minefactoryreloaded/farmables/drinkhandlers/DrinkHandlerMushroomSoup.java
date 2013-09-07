package powercrystals.minefactoryreloaded.farmables.drinkhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import powercrystals.minefactoryreloaded.api.IFluidDrinkHandler;

public class DrinkHandlerMushroomSoup implements IFluidDrinkHandler
{
	@Override
	public void onDrink(EntityPlayer player)
	{
		player.heal(4);
		player.getFoodStats().addStats(4, 1.0F);
		player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 15 * 20, 2));
	}
}
