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
    public static final Item BODY;
    public static final Item WHEEL;
    public static final Item BODY_TWO;
    public static final Item CAR_ONE_SPAWN;
    public static final Item CONTROLLER;
    public static final Item DOOR_LEFT;
    public static final Item DOOR_RIGHT;
    public static final Item WHEEL_HOVERING;

    public static final Item ICON;

    public static final Item PLUTONIUM_PLUG;

    static
    {
        ICON = new ItemIcon();
        BODY = new ItemBody();
        WHEEL = new ItemWheel();
        BODY_TWO = new ItemBodyTwo();
        CONTROLLER = new ItemController();
        CAR_ONE_SPAWN = new ItemCarOne();
        DOOR_LEFT = new ItemDoor();
        DOOR_RIGHT = new ItemDoor2();
        WHEEL_HOVERING = new ItemWHover();
        PLUTONIUM_PLUG = new ItemPlug("plutonium_plug");
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
    }

    private static void register(Item item) {
        RegistryHandler.Items.add(item);
    }
}
