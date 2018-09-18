package net.alpha.bttf.init;

import net.alpha.bttf.Reference;
import net.alpha.bttf.tileentity.TileEntityCarOne;
import net.alpha.bttf.tileentity.TileEntityCarTwo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityCarOne.class, new ResourceLocation(Reference.MOD_ID, "car_one_model"));
        GameRegistry.registerTileEntity(TileEntityCarTwo.class, new ResourceLocation(Reference.MOD_ID, "car_two_model"));
    }
}
