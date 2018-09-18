package net.alpha.bttf.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ItemBlockVehicle extends ItemBlock
{

    public ItemBlockVehicle(Block block) {
        super(block);
        this.setMaxStackSize(1);
    }
}
