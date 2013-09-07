package powercrystals.minefactoryreloaded.core;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityGrinder;

public class GrindingDamage extends DamageSource
{
    public final TileEntityGrinder grinder;
	protected int _msgCount;
	protected Random _rand;
	public GrindingDamage(TileEntityGrinder grinder)
	{
		this(grinder, null, 1);
	}
	
	public GrindingDamage(TileEntityGrinder grinder, String type)
	{
		this(grinder, type, 1);
	}

	public GrindingDamage(TileEntityGrinder grinder, String type, int deathMessages)
	{
		super(type == null ? "mfr.grinder" : type);
		setDamageBypassesArmor();
        this.grinder = grinder;
		setDamageAllowedInCreativeMode();
		_msgCount = Math.max(deathMessages, 1);
		_rand = new Random();
	}
	
	@Override
    public ChatMessageComponent getDeathMessage(EntityLivingBase par1EntityLiving)
    {
        EntityLivingBase entityliving1 = par1EntityLiving.func_94060_bK();
        String s = "death.attack." + this.damageType;
        if (_msgCount > 0)
        {
        	int msg = _rand.nextInt(_msgCount);
        	if (msg != 0)
        	{
        		s += "." + msg;
        	}
        }
        String s1 = s + ".player";
        return entityliving1 != null && StatCollector.func_94522_b(s1) ? new ChatMessageComponent().func_111072_b(StatCollector.translateToLocalFormatted(s1, par1EntityLiving.getTranslatedEntityName(), entityliving1.getTranslatedEntityName())) : new ChatMessageComponent().func_111072_b(StatCollector.translateToLocalFormatted(s, par1EntityLiving.getTranslatedEntityName()));
    }
}