package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTurnstile extends HorizontalFacingBlock {
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public BlockTurnstile() {
        super(Settings.of(Material.METAL));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        return VoxelShapes.union(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, -4, 3, 17, 20, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, -4, 16, 17, 20, facing)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        VoxelShape shape = VoxelShapes.union(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, -4, 2, 24, 20, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, -4, 16, 24, 20, facing)
        );
        return state.get(OPEN) ? shape : VoxelShapes.union(shape, MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, facing));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(OPEN, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPEN);
    }
}
