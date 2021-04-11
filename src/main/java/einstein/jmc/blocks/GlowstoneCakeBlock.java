package einstein.jmc.blocks;

import einstein.jmc.tileentity.GlowstoneCakeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GlowstoneCakeBlock extends ContainerCakeBlock
{
    public GlowstoneCakeBlock(final Block.Properties properties) {
        super(properties);
        
    }
    
    @Override
    public ActionResultType eatSlice(final IWorld worldIn, final BlockPos pos, final BlockState state, final PlayerEntity playerIn) {
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        }
        playerIn.addStat(Stats.EAT_CAKE_SLICE);
        playerIn.getFoodStats().addStats(2, 0.1F);
        World world = playerIn.getEntityWorld();
        TileEntity tileentity = world.getTileEntity(pos);
        ((GlowstoneCakeTileEntity)tileentity).setGlowing();
        final int i = state.get(GlowstoneCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(GlowstoneCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
    
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new GlowstoneCakeTileEntity();
	}
}
