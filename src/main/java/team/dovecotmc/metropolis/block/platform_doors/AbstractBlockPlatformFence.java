package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBlockPlatformFence extends HorizontalDirectionalBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private final DyeColor color;

    public AbstractBlockPlatformFence(Properties properties, DyeColor color) {
        super(properties.mapColor(color.getMapColor()));
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    public @NotNull BlockState getTintedBlockState(BlockState oldState, DyeColor dyeColor) {
        return oldState;
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER) && level.getBlockState(blockPos.below()).getBlock().equals(blockState.getBlock()))
            return use(level.getBlockState(blockPos.below()), level, blockPos.below(), player, interactionHand, new BlockHitResult(blockHitResult.getLocation().add(0, -1, 0), blockHitResult.getDirection(), blockHitResult.getBlockPos().below(), blockHitResult.isInside()));

        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemStack.getItem() instanceof DyeItem dyeItem) {
            BlockState newState = getTintedBlockState(blockState, dyeItem.getDyeColor());

            if (newState.getBlock().equals(blockState.getBlock()))
                return InteractionResult.FAIL;

            level.setBlockAndUpdate(blockPos, newState.setValue(HALF, DoubleBlockHalf.LOWER));
            level.setBlockAndUpdate(blockPos.above(), newState.setValue(HALF, DoubleBlockHalf.UPPER));

            if (!level.isClientSide()) {
                level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0f, 1.0f);

                if (!player.isCreative()) {
                    itemStack.setCount(itemStack.getCount() - 1);
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        level.setBlockAndUpdate(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            preventCreativeDropFromBottomPart(level, blockPos, blockState, player);
        }
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
            if (level.getBlockState(blockPos.above()).getBlock() instanceof AbstractBlockPlatformFence && level.getBlockState(blockPos.above()).getValue(HALF).equals(DoubleBlockHalf.UPPER))
                level.destroyBlock(blockPos.above(), false);
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
            return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    protected static void preventCreativeDropFromBottomPart(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        BlockPos blockPos2;
        BlockState blockState2;
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER && (blockState2 = level.getBlockState(blockPos2 = blockPos.below())).is(blockState.getBlock()) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER) {
            BlockState blockState3 = blockState2.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            level.setBlock(blockPos2, blockState3, 35);
            level.levelEvent(player, 2001, blockPos2, Block.getId(blockState2));
        }
    }
}
