package net.alpha.bttf.item;

import net.alpha.bttf.Main;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemController extends Item
{
    public ItemController()
    {
        this.setRegistryName("controller");
        this.setUnlocalizedName("controller");
        this.setCreativeTab(Main.TAB);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("linked_car"))
            {
                tooltip.add(TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + "Linked");
                tooltip.add(stack.getTagCompound().getString("linked_car"));
                return;
            }
        }
        tooltip.add(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Unlinked");
        tooltip.add("Right click a car with this controller to link them together");
    }
}
