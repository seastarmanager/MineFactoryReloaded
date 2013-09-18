package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.minefactoryreloaded.core.MFRLiquidMover;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

public class TileEntityWeather extends TileEntityFactoryPowered implements IFluidHandler {
    private FluidTank _tank;

    public TileEntityWeather() {
        super(Machine.WeatherCollector);
        _tank = new FluidTank(4 * FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public String getGuiBackground() {
        return "weathercollector.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {
        return new GuiFactoryPowered(getContainer(inventoryPlayer), this);
    }

    @Override
    public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer) {
        return new ContainerFactoryPowered(this, inventoryPlayer);
    }

    @Override
    public FluidTank getTank() {
        return _tank;
    }

    @Override
    public int getEnergyStoredMax() {
        return 16000;
    }

    @Override
    public int getWorkMax() {
        return 50;
    }

    @Override
    public int getIdleTicksMax() {
        return 600;
    }

    @Override
    public boolean activateMachine() {
        MFRLiquidMover.pumpFluid(this);

        if (worldObj.getWorldInfo().isRaining() && canSeeSky()) {
            BiomeGenBase bgb = worldObj.getBiomeGenForCoords(this.xCoord, this.zCoord);

            if (!bgb.canSpawnLightningBolt() && !bgb.getEnableSnow()) {
                setIdleTicks(getIdleTicksMax());
                return false;
            }
            setWorkDone(getWorkDone() + 1);
            if (getWorkDone() >= getWorkMax()) {
                if (bgb.getFloatTemperature() >= 0.15F) {
                    //if(_tank.fill(new LiquidStack(Block.waterStill.blockID, LiquidContainerRegistry.BUCKET_VOLUME), true) > 0)
                    if (_tank.fill(FluidContainerRegistry.getFluidForFilledItem(new ItemStack(Item.bucketWater)), true) > 0) {
                        setWorkDone(0);
                        return true;
                    } else {
                        setWorkDone(getWorkMax());
                        return false;
                    }
                } else {
                    doDrop(new ItemStack(Item.snowball));
                    setWorkDone(0);
                }
            }
            return true;
        }
        setIdleTicks(getIdleTicksMax());
        return false;
    }

    @Override
    public ForgeDirection getDropDirection() {
        return ForgeDirection.DOWN;
    }

    private boolean canSeeSky() {
        for (int y = yCoord + 1; y <= 256; y++) {
            int blockId = worldObj.getBlockId(xCoord, y, zCoord);
            if (Block.blocksList[blockId] != null && !Block.blocksList[blockId].isAirBlock(worldObj, xCoord, y, zCoord)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean allowBucketDrain() {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean manageSolids() {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { _tank.getInfo() };
    }
}
