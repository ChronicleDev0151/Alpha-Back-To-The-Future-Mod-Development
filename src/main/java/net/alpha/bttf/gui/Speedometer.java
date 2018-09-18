package net.alpha.bttf.gui;

import ibxm.Player;
import net.alpha.bttf.Reference;
import net.alpha.bttf.entity.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;

public class Speedometer extends Gui
{
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    private ResourceLocation SPEEDOMETER = new ResourceLocation(Reference.MOD_ID , "textures/timetravel/speedometer.png");

    private static Constructor<ScaledResolution> scaledResolution188Constructor = null;
    static
    {
        try
        {
            scaledResolution188Constructor = ScaledResolution.class.getConstructor(Minecraft.class);
        }
        catch(Exception e)
        {
        }
    }

    private void draw()
    {
        ScaledResolution scaledResolution;
        scaledResolution = new ScaledResolution(mc);

        if(scaledResolution != null)
        {
            try
            {
                scaledResolution = scaledResolution188Constructor.newInstance(mc);
            } catch(Exception e)
            {
               return;
            }
        }
        else
            {


                EntityPlayer player = mc.player;
                Entity entity = player.getRidingEntity();

                if(entity instanceof EntityVehicle)
                {

                    EntityVehicle vehicle = (EntityVehicle) entity;

                    String speed = new DecimalFormat("0.0").format(vehicle.getKilometersPreHour());
                    mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "BPS: " + TextFormatting.YELLOW + speed, 10, 10, Color.WHITE.getRGB());

                    this.mc.renderEngine.bindTexture(SPEEDOMETER);
                }

            }
    }

}
