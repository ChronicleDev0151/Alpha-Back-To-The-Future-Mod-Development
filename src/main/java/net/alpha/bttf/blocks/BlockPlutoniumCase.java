package net.alpha.bttf.blocks;

import io.netty.buffer.ByteBuf;
import net.alpha.bttf.Main;
import net.alpha.bttf.tileentity.TileEntityCarOne;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public class BlockPlutoniumCase extends Block implements IEntityAdditionalSpawnData
{
    public BlockPlutoniumCase(String ID)
    {
        super(Material.ANVIL);
        this.setRegistryName(ID);
        this.setUnlocalizedName(ID);
        this.setCreativeTab(Main.MUSEUM);
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCarOne();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }
}
