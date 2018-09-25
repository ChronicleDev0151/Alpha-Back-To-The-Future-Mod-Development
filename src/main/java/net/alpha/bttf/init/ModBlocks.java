package net.alpha.bttf.init;

import net.alpha.bttf.blocks.BlockCarOne;
import net.alpha.bttf.blocks.BlockPlutoniumCase;
import net.alpha.bttf.blocks.BlockRailRoad;
import net.alpha.bttf.blocks.BlockScaledBTTF3Delorean;
import net.alpha.bttf.item.ItemBlockVehicle;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ModBlocks
{

    public static final Block car_one_model;
    public static final Block plutonium_case;
    public static final Block car_two_model;

    public static final Block RAIL_ROAD;
    public static final Block CAR_THREE_SCALED;

    static
    {
        car_one_model = new BlockCarOne();
        plutonium_case = new BlockPlutoniumCase("plutonium_case");
        car_two_model = new BlockPlutoniumCase("car_two_model");
        RAIL_ROAD = new BlockRailRoad();
        CAR_THREE_SCALED = new BlockScaledBTTF3Delorean();
    }

    public static void register()
    {
        registerBlock(car_one_model);
        registerBlock(car_two_model, new ItemBlockVehicle(car_two_model));
        registerBlock(plutonium_case, new ItemBlockVehicle(plutonium_case));
        registerBlock(RAIL_ROAD, new ItemBlockVehicle(RAIL_ROAD));
        registerBlock(CAR_THREE_SCALED, new ItemBlockVehicle(CAR_THREE_SCALED));
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
