package powercrystals.minefactoryreloaded.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import powercrystals.minefactoryreloaded.gui.slot.SlotFake;
import powercrystals.minefactoryreloaded.gui.slot.SlotRemoveOnly;
import powercrystals.minefactoryreloaded.gui.slot.SlotViewOnly;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLiquiCrafter;

public class ContainerLiquiCrafter extends ContainerFactoryInventory
{
	private TileEntityLiquiCrafter _crafter;
	
	private int _tempTankIndex;
	private int _tempLiquidId;
	
	public ContainerLiquiCrafter(TileEntityLiquiCrafter crafter, InventoryPlayer inventoryPlayer)
	{
		super(crafter, inventoryPlayer);
		_crafter = crafter;
	}
	
	@Override
	protected void addSlots()
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				addSlotToContainer(new SlotFake(_te, j + i * 3, 8 + j * 18, 20 + i * 18));
			}
		}
		
		addSlotToContainer(new SlotViewOnly(_te, 9, 80, 38));
		addSlotToContainer(new SlotRemoveOnly(_te, 10, 134, 38));
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(_te, 11 + j + i * 9, 8 + j * 18, 79 + i * 18));
			}
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		int tankIndex = (int)(_crafter.worldObj.getWorldTime() % 9);
		FluidTank tank = _crafter.getTanks()[tankIndex];
		FluidStack l = tank.getFluid();

		for(int i = 0; i < crafters.size(); i++)
		{
            ICrafting crafter = (ICrafting) crafters.get(i);
			crafter.sendProgressBarUpdate(this, 0, tankIndex);
			if(l != null)
			{
				crafter.sendProgressBarUpdate(this, 1, l.fluidID);
				crafter.sendProgressBarUpdate(this, 2, 0);
				crafter.sendProgressBarUpdate(this, 3, l.amount);
			}
			else
			{
				crafter.sendProgressBarUpdate(this, 1, 0);
				crafter.sendProgressBarUpdate(this, 2, 0);
				crafter.sendProgressBarUpdate(this, 3, 0);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int var, int value)
	{
		super.updateProgressBar(var, value);
		if(var == 0) _tempTankIndex = value;
		else if(var == 1) _tempLiquidId = value;
		//else if(var == 2) _tempLiquidMeta = value;
		else if(var == 3)
		{
			_crafter.getTanks()[_tempTankIndex].setFluid(new FluidStack(_tempLiquidId, value));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		
		if(slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			
			if(slot < 9 || (slot > 9 && slot < 29))
			{
				if(!mergeItemStack(stackInSlot, 29, inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if(slot != 9 && !mergeItemStack(stackInSlot, 11, 18, false))
			{
				return null;
			}
			
			if(stackInSlot.stackSize == 0)
			{
				slotObject.putStack(null);
			}
			else
			{
				slotObject.onSlotChanged();
			}
			
			if(stackInSlot.stackSize == stack.stackSize)
			{
				return null;
			}
			
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		
		return stack;
	}
	
	@Override
	protected int getPlayerInventoryVerticalOffset()
	{
		return 133;
	}
}
