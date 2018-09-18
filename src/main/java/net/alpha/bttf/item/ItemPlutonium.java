package net.alpha.bttf.item;

import net.alpha.bttf.Main;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.xml.soap.SOAPConstants;

public class ItemPlutonium extends Item
{
    public int capacity;
    public int power;


    public ItemPlutonium()
    {
        this.setRegistryName("plutonium");
        this.setUnlocalizedName("plutonium");
        this.setCreativeTab(Main.TAB);
    }

    public int getCurrentPower(ItemStack stack)
    {
        if(!stack.isEmpty() && stack.getItem() == this)
        {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if(tagCompound != null)
            {
                return tagCompound.getInteger("plutonium_level");
            }
        }
        return 0;
    }

    public int getCapacity(ItemStack stack)
    {
        if(!stack.isEmpty() && stack.getItem() == this)
        {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if(tagCompound != null && tagCompound.hasKey("plutonium_level", Constants.NBT.TAG_INT));
            {
                int capacity = tagCompound.getInteger("capacity");
                return capacity > 0 ? capacity : this.capacity;
            }
        }
        return capacity;
    }

    public int fill(ItemStack stack, int fuel)
    {
        int capacity = getCapacity(stack);
        int currentFuel = getCurrentPower(stack);
        int newFuel = Math.min(currentFuel + fuel, capacity);
        NBTTagCompound tagCompound = createTagCompound(stack);
        tagCompound.setInteger("fuel", newFuel);
        return Math.max(0, currentFuel + fuel - capacity);
    }

    public int drain(ItemStack stack, int maxAmount)
    {
        int currentFuel = getCurrentPower(stack);
        int remainingFuel = Math.max(0, currentFuel - maxAmount);
        NBTTagCompound tagCompound = createTagCompound(stack);
        tagCompound.setInteger("fuel", remainingFuel);
        return currentFuel - remainingFuel;
    }

    public int power(ItemStack stack, int fuel)
    {
        int capacity = getCapacity(stack);
        int currentPower = getCurrentPower(stack);
        int newPower = Math.min(currentPower + fuel, capacity);
        return Math.max(0, currentPower + fuel - capacity);
    }

    public static NBTTagCompound createTagCompound(ItemStack stack)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }


}
