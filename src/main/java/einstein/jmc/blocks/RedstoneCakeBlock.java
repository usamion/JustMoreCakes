package einstein.jmc.blocks;

import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RedstoneCakeBlock extends BaseCakeBlock {

	public RedstoneCakeBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return 7 - blockState.getValue(BITES);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (ModClientConfigs.REDSTONE_CAKE_PARTICLES.get()) {
			for (int i = 0; i < 2; ++i) {
				double d0 = pos.getX() + rand.nextDouble();
				double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.25D;
				double d2 = pos.getZ() + rand.nextDouble();
				level.addParticle(DustParticleOptions.REDSTONE, d0, d1, d2, 0, 0, 0);
			}
		}
	}
}
