package powercrystals.minefactoryreloaded.modhelpers;

import powercrystals.minefactoryreloaded.item.ItemLaserFocus;

/**
 * Only provided when multple mods provide the same ore
 * @author samrg472
 */
public enum Weights {

    COPPER(100, ItemLaserFocus.Type.BROWN),
    TIN(130, ItemLaserFocus.Type.LIGHTGRAY),
    LEAD(80, ItemLaserFocus.Type.BLACK);

    public final int weight;
    public final ItemLaserFocus.Type colorFocus;

    private Weights(int weight, ItemLaserFocus.Type colorFocus) {
        this.weight = weight;
        this.colorFocus = colorFocus;
    }

}
