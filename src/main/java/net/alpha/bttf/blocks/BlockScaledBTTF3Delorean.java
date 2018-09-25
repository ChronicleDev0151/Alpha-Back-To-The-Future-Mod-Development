package net.alpha.bttf.blocks;

import net.alpha.bttf.init.ModBlocks;
import net.alpha.bttf.tileentity.TileEntityScaledBTTF3Delorean;
import net.alpha.bttf.tileentity.TileEntityScaledBTTF3DeloreanRailed;
import net.alpha.bttf.vehicles.EntityCarOne;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class BlockScaledBTTF3Delorean extends Block implements ITileEntityProvider
{
    public BlockScaledBTTF3Delorean()
    {
        super(Material.IRON);
        this.setRegistryName("scaled_bttf3_delorean_normal");
        this.setUnlocalizedName("scaled_bttf3_norma");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        tooltip.add("Shift For Info");

        if(GuiScreen.isShiftKeyDown())
        {
            tooltip.add("Back To The Future 3 Delorean, This Is A Scaled Model For The Mod, Place It Anywhere You Like, Its A Museum Item");
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {

        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("railed"))
            {
                worldIn.setTileEntity(pos, new TileEntityScaledBTTF3DeloreanRailed());
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityScaledBTTF3Delorean();
    }
}
