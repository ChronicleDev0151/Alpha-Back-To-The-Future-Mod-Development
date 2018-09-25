package net.alpha.bttf.init;

import net.alpha.bttf.Reference;
import net.alpha.bttf.item.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.b3d.B3DModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;


public class ModItems
{

    // BTTF 1 DELOREAN
    public static final Item BODY;
    public static final Item WHEEL;

    // BTTF 2 DELOREAN
    public static final Item BODY_TWO;
    public static final Item CAR_ONE_SPAWN;

    // MISC
    public static final Item CONTROLLER;
    public static final Item DOOR_LEFT;
    public static final Item DOOR_RIGHT;
    public static final Item WHEEL_HOVERING;


    // TAB STUFF
    public static final Item ICON;

    // DEFAULT DELOREAN
    public static final Item DEFAULT_BODY;


    // POWER
    public static final Item PLUTONIUM_PLUG;

    static
    {
        ICON = new ItemIcon();
        BODY = new ItemBody("body_one");
        WHEEL = new ItemWheel("wheel");
        BODY_TWO = new ItemBody("body_two");
        CONTROLLER = new ItemController();
        CAR_ONE_SPAWN = new ItemCarOne();
        DOOR_LEFT = new ItemDoor("door");
        DOOR_RIGHT = new ItemDoor("door_2");
        WHEEL_HOVERING = new ItemWheel("hover_wheel");
        PLUTONIUM_PLUG = new ItemPlug("plutonium_plug");
        DEFAULT_BODY = new ItemBody("default_body");
    }

    public static void register() {
        register(BODY);
        register(ICON);
        register(WHEEL);
        register(BODY_TWO);
        register(CONTROLLER);
        register(CAR_ONE_SPAWN);
        register(DOOR_LEFT);
        register(DOOR_RIGHT);
        register(WHEEL_HOVERING);
        register(PLUTONIUM_PLUG);
        register(DEFAULT_BODY);
    }

    private static void register(Item item) {
        RegistryHandler.Items.add(item);
    }
}
