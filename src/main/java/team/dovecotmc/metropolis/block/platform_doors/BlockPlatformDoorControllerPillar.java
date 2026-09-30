package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

public class BlockPlatformDoorControllerPillar extends AbstractPlatformDoorController {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public BlockPlatformDoorControllerPillar(Properties properties) {
        super(properties);
}

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction facing = blockState.getValue(FACING);
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    5, 0, 5,
                    11, 23, 11,
                    facing
            );
        } else {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    5, -16, 5,
                    11, 7, 11,
                    facing
            );
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        boolean upper = blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER);

        if (upper && !(level.getBlockState(blockPos.below()).getBlock() instanceof BlockPlatformDoorControllerPillar))
            return InteractionResult.SUCCESS;

        return super.use(blockState, level, upper ? blockPos.below() : blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        level.setBlockAndUpdate(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);

        if (state == null)
            return null;

        BlockPos blockPos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
            return state.setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            preventCreativeDropFromBottomPart(level, blockPos, blockState, player);
        }
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
            if (level.getBlockState(blockPos.above()).getBlock() instanceof BlockPlatformDoorControllerPillar && level.getBlockState(blockPos.above()).getValue(HALF).equals(DoubleBlockHalf.UPPER))
                level.destroyBlock(blockPos.above(), false);
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER))
            return null;

        return super.newBlockEntity(blockPos, blockState);
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
