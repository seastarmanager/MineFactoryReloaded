package powercrystals.minefactoryreloaded.modhelpers;

/**
 * Only provided when multple mods provide the same ore
 * @author samrg472
 */
public enum Weights {

    COPPER(100),
    TIN(130),
    LEAD(80);

    public final int weight;

    private Weights(int weight) {
        this.weight = weight;
    }

}
