package einstein.jmc.blocks;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreeTieredCandleCakeBlock extends AbstractCandleBlock {

	public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
	protected static final VoxelShape SHAPE = Shapes.or(
			Block.box(3, 15, 3, 13, 21, 13),
			Block.box(1, 0, 1, 15, 8, 15),
			Block.box(2, 8, 2, 14, 15, 14),
			Block.box(7, 21, 7, 9, 27, 9));
	private ThreeTieredCakeBlock originalCake;

	public ThreeTieredCandleCakeBlock(Block candle, ThreeTieredCakeBlock originalCake, BlockBehaviour.Properties properties) {
		super(properties);
		this.originalCake = originalCake;
		this.registerDefaultState(stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
	}
	
	protected Iterable<Vec3> getParticleOffsets(BlockState state) {
		return ImmutableList.of(new Vec3(0.5D, 1.8125D, 0.5D));
	}
	
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
			if (candleHit(hitResult) && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
				extinguish(player, state, level, pos);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				InteractionResult interactionresult = originalCake.eat(level, pos, originalCake.defaultBlockState(), player);
				if (interactionresult.consumesAction()) {
					dropResources(state, level, pos);
				}
				return interactionresult;
			}
		}
		else {
			if (state.getValue(LIT) == false) {
				level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1, level.getRandom().nextFloat() * 0.4F + 0.8F);
				level.setBlock(pos, defaultBlockState().setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
				level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
				itemstack.hurtAndBreak(1, player, (player1) -> {
					player1.broadcastBreakEvent(player.getUsedItemHand());
				});

				return InteractionResult.sidedSuccess(level.isClientSide());
			}
			else {
				Item heldItem = player.getMainHandItem().getItem(); 
				if (heldItem instanceof FlintAndSteelItem || heldItem instanceof FireChargeItem) {
					return heldItem.useOn(new UseOnContext(player, hand, hitResult));
				}
				else {
					return InteractionResult.PASS;
				}
			}
		}
	}
	
	private static boolean candleHit(BlockHitResult hitResult) {
		return hitResult.getLocation().y - hitResult.getBlockPos().getY() > 1.3125D;
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}
	
	public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
		return new ItemStack(originalCake.asItem());
	}
	
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction direction, BlockState oldState, LevelAccessor accessor, BlockPos pos, BlockPos oldPos) {
		return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, oldState, accessor, pos, oldPos);
	}
	
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).getMaterial().isSolid();
	}
	
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return 30;
	}
	
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computation) {
		return false;
	}
}