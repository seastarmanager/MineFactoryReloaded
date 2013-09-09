package powercrystals.minefactoryreloaded.modhelpers.ic2;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.core.asm.relauncher.Implementable;

import java.util.Arrays;
import java.util.List;

@Implementable("ic2.api.recipe.IRecipeInput")
public class RecipeInputItemStack {

    public final ItemStack input;
    public final int amount;

    public RecipeInputItemStack(ItemStack input) {
        this.input = input;
        this.amount = input.stackSize;
    }

    /**
     * Check if subject matches this recipe input, ignoring the amount.
     *
     * @param subject ItemStack to check
     * @return true if it matches the requirement
     */
    public boolean matches(ItemStack subject) {
        return subject.itemID == input.itemID &&
                (subject.getItemDamage() == input.getItemDamage() || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }

    /**
     * Determine the minimum input stack size.
     *
     * @return input amount required
     */
    public int getAmount() {
        return amount;
    }

    /**
     * List all possible inputs (best effort).
     * <p/>
     * The stack size is undefined, use getAmount to get the correct one.
     *
     * @return list of inputs, may be incomplete
     */
    public List<ItemStack> getInputs() {
        return Arrays.asList(input);
    }
}
