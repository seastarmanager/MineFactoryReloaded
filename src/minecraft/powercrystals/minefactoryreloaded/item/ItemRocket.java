package powercrystals.minefactoryreloaded.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class ItemRocket extends ItemFactory {
    public ItemRocket(int id) {
        super(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
    }
}
