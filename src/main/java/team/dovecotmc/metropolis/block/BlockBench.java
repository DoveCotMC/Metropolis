package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockBenchEntity;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockBench extends HorizontalFacingBlock {
    public BlockBench(Settings settings) {
        super(settings.nonOpaque());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        return VoxelShapes.union(
                MetroBlockUtil.getVoxelShapeByDirection(
                        0, 0, 0,
                        16, 8, 16,
                        facing
                ),
                MetroBlockUtil.getVoxelShapeByDirection(
                        0, 0, 8,
                        16, 16, 16,
                        facing
                )
        );
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

//    @Nullable
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new BlockBenchEntity(pos, state);
//    }
}
