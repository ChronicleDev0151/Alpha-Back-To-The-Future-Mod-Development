package net.alpha.bttf;

import net.alpha.bttf.client.ClientEvent;
import net.alpha.bttf.common.CommonEvents;
import net.alpha.bttf.entity.EntityVehicle;
import net.alpha.bttf.gui.Speedometer;
import net.alpha.bttf.init.ModBlocks;
import net.alpha.bttf.gui.GuiHandler;
import net.alpha.bttf.init.ModItems;
import net.alpha.bttf.init.RegistryHandler;
import net.alpha.bttf.init.TileEntityRegistry;
import net.alpha.bttf.network.PacketHandler;
import net.alpha.bttf.proxy.Proxy;
import net.alpha.bttf.vehicles.EntityCarOne;
import net.alpha.bttf.vehicles.EntityCarTwo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: Chronicle0151
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptedMinecraftVersions = Reference.MOD_COMPATIBILITY)
public class Main {



    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
    public static Proxy proxy;

    @Mod.Instance(Reference.MOD_ID)
    public static Main instance;

    public static final CreativeTabs TAB = new CreativeTabs("tabFuture")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModBlocks.car_one_model);
        }
    };

    public static final CreativeTabs MUSEUM = new CreativeTabs("tabMuseumFuture")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModBlocks.car_one_model);
        }
    };

    public int nextEntityId;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new CommonEvents());

        PacketHandler.init();

        TileEntityRegistry.init();

        RegistryHandler.init();

        proxy.preInit();
        registerVehicles();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        proxy.Init();
    }

    private void registerGates()
    {
    }

    public void registerVehicles()
    {
        registerVehicle("car_one", EntityCarOne.class);
        registerVehicle("car_two", EntityCarTwo.class);
    }

    private void registerVehicle(String id, Class<? extends EntityVehicle> clazz)
    {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, id), clazz, id, nextEntityId++, this, 64, 1, true);
    }


    private void registerGates(String id, Class<? extends Entity> clazz) {
        EntityRegistry.registerModEntity(new ResourceLocation("bttf", id), clazz, id, nextEntityId++, this, 64, 1, true);
    }


}
