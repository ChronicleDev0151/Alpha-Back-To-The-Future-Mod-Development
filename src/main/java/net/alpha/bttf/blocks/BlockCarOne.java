package net.alpha.bttf.blocks;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.Main;
import net.alpha.bttf.tileentity.TileEntityCarOne;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public class BlockCarOne extends BlockObject {




    public BlockCarOne() {
        super(Material.ANVIL, "CAR_ONE_MODEL");
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCarOne();
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
