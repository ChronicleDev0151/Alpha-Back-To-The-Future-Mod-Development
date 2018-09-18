package net.alpha.bttf.timetravel;

import net.alpha.bttf.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Date;

public abstract class TimeTravel extends TimeEntity
{
    public static TimeTravel INSTANCE;

    public TimeTravel(World worldin)
    {

        super(worldin);
    }


    protected TimeTravel(World worldIn, long ticks)
    {
        super(worldIn);
        this.updateAnimation();
        this.updateSound();
     //   this.updateTime(ticks);
        this.updateTimeTravel(ticks);
    }

    public void TimeTravel(long ticks)
    {

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer playerIn = minecraft.player;
        World worldIn = minecraft.world;
        EntityLivingBase base = minecraft.player;
        Entity entity = minecraft.pointedEntity;


        Date date = new Date();

        this.updateSound();
        this.Animation();
        this.updateTime(ticks);


    }

    public void Animation()
    {

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer playerIn = minecraft.player;
        World worldIn = minecraft.world;
        EntityLivingBase base = minecraft.player;
        Entity entity = minecraft.pointedEntity;

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        if(TimeTravelEnum.FINISHED) {
          minecraft.getTextureManager().bindTexture(Textures.ICE);
        }

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();



    }

    public void updateTimeTravel(long ticks)
    {
        this.TimeTravel(ticks);

    }

    public void updateTime(long ticks)
    {
     this.TimeUtil(ticks);
    }


    public void updateAnimation()
    {
     this.Animation();
    }

    public void updateSound()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
         EntityPlayer player = minecraft.player;

         player.playSound(ModSounds.TRAVEL_GATE_ONE, 1, 1);
    }

    public String TimeUtil(long ticks)
    {

        int years = (int) ((Math.floor(ticks / 100000)) % 365);
        int days = (int) ((Math.floor(ticks / 10000) / 100000 * 30));
        int hours = (int) ((Math.floor(ticks / 1000.0) + 7) % 24);
        int minutes = (int) Math.floor((ticks % 1000) / 1000.0 * 60);

        return String.format("%02y:%02d:02h:02m", years, days, hours, minutes);
    }


    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
