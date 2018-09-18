package net.alpha.bttf.init;

import net.alpha.bttf.blocks.BlockCarOne;
import net.alpha.bttf.blocks.BlockPlutoniumCase;
import net.alpha.bttf.item.ItemBlockVehicle;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ModBlocks
{

    public static final Block car_one_model;
    public static final Block plutonium_case;
    public static final Block car_two_model;

    static
    {
        car_one_model = new BlockCarOne();
        plutonium_case = new BlockPlutoniumCase("plutonium_case");
        car_two_model = new BlockPlutoniumCase("car_two_model");
    }

    public static void register()
    {
        registerBlock(car_one_model);
        registerBlock(car_two_model, new ItemBlockVehicle(car_two_model));
        registerBlock(plutonium_case, new ItemBlockVehicle(plutonium_case));
    }

    private static void registerBlock(Block block)
    {
        registerBlock(block, new ItemBlock(block));
    }

    private static void registerBlock(Block block, ItemBlock item)
    {
        if(block.getRegistryName() == null)
            throw new IllegalArgumentException("A block being registered does not have a registry name and could be successfully registered.");

        RegistryHandler.Blocks.add(block);
        item.setRegistryName(block.getRegistryName());
        RegistryHandler.Items.add(item);
    }


}
