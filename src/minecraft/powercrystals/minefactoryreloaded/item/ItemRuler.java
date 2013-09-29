package powercrystals.minefactoryreloaded.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

public class ItemRuler extends ItemFactory {
    public ItemRuler(int id) {
        super(id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (world.isRemote) {
            MovingObjectPosition mop = player.rayTrace(MFRConfig.getInstance().spyglassRange, 1.0F);
            if (mop == null || (mop.typeOfHit == EnumMovingObjectType.ENTITY && mop.entityHit == null)) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Nothing in sight"));
            } else if (mop.typeOfHit == EnumMovingObjectType.ENTITY) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Hit entity - measurement failed"));
            } else {
                if (stack.getTagCompound() == null) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setInteger("x", mop.blockX);
                    tag.setInteger("y", mop.blockY);
                    tag.setInteger("z", mop.blockZ);
                    stack.setTagCompound(tag);
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Recorded position 1"));
                } else {
                    int x = stack.getTagCompound().getInteger("x");
                    int y = stack.getTagCompound().getInteger("y");
                    int z = stack.getTagCompound().getInteger("z");

                    int distX = Math.abs(mop.blockX - x);
                    int distY = Math.abs(mop.blockY - y);
                    int distZ = Math.abs(mop.blockZ - z);

                    double distAll = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2) + Math.pow(distZ, 2));

                    player.sendChatToPlayer(ChatMessageComponent.createFromText("X: distance: " + distX + ", count: " + (distX + 1)));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Y: distance: " + distY + ", count: " + (distY + 1)));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Z: distance: " + distZ + ", count: " + (distZ + 1)));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Total distance: %.1f", distAll)));
                }
            }
        }

        return super.onItemRightClick(stack, world, player);
    }
}
