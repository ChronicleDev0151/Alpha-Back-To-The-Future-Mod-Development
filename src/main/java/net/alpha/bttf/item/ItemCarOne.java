package net.alpha.bttf.item;

import net.alpha.bttf.Main;
import net.alpha.bttf.gui.TimeCircuits;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCarOne extends Item
{
    public ItemCarOne() {
        this.setRegistryName("car_one");
        this.setUnlocalizedName("car_one");
        this.setCreativeTab(Main.TAB);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            if(facing == EnumFacing.UP)
            {
                BlockPos up = pos.up();
               // EntityCarOne car = new EntityVehicle(worldIn, pos.getX(), pos.getY(), pos.getZ());
               // worldIn.spawnEntity(car);
            }
            player.openGui(Main.instance, TimeCircuits.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
