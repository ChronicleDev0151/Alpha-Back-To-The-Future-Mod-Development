package net.alpha.bttf.timetravel;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public enum TimeTravelTypes
{
    TIMETRAVELONE(5F, TextFormatting.BLUE),
    TIMETRAVELTWO(3F, TextFormatting.BLUE),
    TIMETRAVELTHREE(7F, TextFormatting.GOLD);

    float timeSpeed;
    TextFormatting color;


    TimeTravelTypes(float timeSpeed, TextFormatting color)
    {
        this.timeSpeed = timeSpeed;
        this.color = color;
    }

    public float getTimeSpeed() {
        return timeSpeed;
    }

    public TextFormatting getColor() {
        return color;
    }

    public static TimeTravelTypes getType(ItemStack stack)
    {
        return getType(stack.getItemDamage());
    }

    public static TimeTravelTypes getType(int index)
    {
        return TimeTravelTypes.values()[index];
    }


}
