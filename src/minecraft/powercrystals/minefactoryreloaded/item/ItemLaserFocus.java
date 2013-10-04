package powercrystals.minefactoryreloaded.item;

public class ItemLaserFocus extends ItemMulti {

    public enum Type {
        WHITE("white"),
        ORANGE("orange"),
        MAGENTA("magenta"),
        LIGHTBLUE("lightblue"),
        YELLOW("yellow"),
        LIME("lime"),
        PINK("pink"),
        GRAY("gray"),
        LIGHTGRAY("lightgray"),
        CYAN("cyan"),
        PURPLE("purple"),
        BLUE("blue"),
        BROWN("brown"),
        GREEN("green"),
        RED("red"),
        BLACK("black");

        public final String name;

        private Type(String name) {
            this.name = name;
        }
    }

    public ItemLaserFocus(int itemId) {
        super(itemId);
        String[] names = new String[Type.values().length];
        for (int i = 0; i < names.length; i++)
            names[i] = Type.values()[i].name;
        setNames(names);
    }
}
