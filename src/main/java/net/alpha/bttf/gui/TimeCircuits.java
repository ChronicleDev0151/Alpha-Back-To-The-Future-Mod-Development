package net.alpha.bttf.gui;

import net.alpha.bttf.init.ModSounds;
import net.alpha.bttf.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class TimeCircuits extends GuiScreen
{

    public static final int ID = 1;
    private Minecraft mc;

    // Destination
    private GuiTextField day;
    private GuiTextField month;
    private GuiTextField year;
    private GuiTextField hour;
    private GuiTextField minute;

    // present time
    private GuiLabel day2;
    private GuiLabel month2;
    private GuiLabel year2;
    private GuiLabel hour2;
    private GuiLabel minute2;

    // Last Time Departed
    private GuiLabel day3;
    private GuiLabel month3;
    private GuiLabel year3;
    private GuiLabel hour3;
    private GuiLabel minute3;

    private long tick;

    @Override
    public void initGui()
    {
        day = new GuiTextField(1, this.fontRenderer, 10, 10, 20, 10);
        day.setMaxStringLength(2);

        month = new GuiTextField(1, this.fontRenderer, 50, 10, 20, 10);
        month.setMaxStringLength(2);

        year = new GuiTextField(1, this.fontRenderer, 90, 10, 40, 10);
        year.setMaxStringLength(4);

        day2 = new GuiLabel(this.fontRenderer, 0, this.width, this.height, 0, 10, Color.white.getRGB());
        month2 = new GuiLabel(this.fontRenderer, 0, this.width, this.height / 5, 0, 10, Color.white.getRGB());



        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        day.textboxKeyTyped(typedChar, keyCode);
        month.textboxKeyTyped(typedChar, keyCode);
        year.textboxKeyTyped(typedChar, keyCode);

        day2.addLine(this.updateDate(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        day.mouseClicked(mouseX, mouseY, mouseButton);
        month.mouseClicked(mouseX, mouseY, mouseButton);
        year.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {

    }

    @Override
    public void updateScreen() {

        day.updateCursorCounter();
        month.updateCursorCounter();
        year.updateCursorCounter();

        super.updateScreen();
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        day.drawTextBox();
        month.drawTextBox();
        year.drawTextBox();


        day2.drawLabel(mc, mouseX, mouseY);
        day2.addLine("28");

        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public String updateDate(char typedChar, int keyCode)
    {
        String dd = String.valueOf(day.textboxKeyTyped(typedChar, keyCode));
        String mm = String.valueOf(month.textboxKeyTyped(typedChar, keyCode));
        String yyyy = String.valueOf(year.textboxKeyTyped(typedChar, keyCode));

       if(day.getText() == dd)
       {
           day2.addLine(dd);
       }

       if(month.getText() == mm)
       {
           month2.addLine(mm);
       }
       if(year.getText() == yyyy)
       {
           year2.addLine(yyyy);
       }
       return dd + mm + yyyy;
    }


}
