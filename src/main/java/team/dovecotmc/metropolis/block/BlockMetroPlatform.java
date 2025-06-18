package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockMetroPlatform extends HorizontalDirectionalBlock implements IBlockPlatform {
    public static final EnumProperty<EnumPlatformType> TYPE = EnumProperty.create("type", EnumPlatformType.class);

    public BlockMetroPlatform(Properties settings) {
        super(settings.noOcclusion());
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getUpdatedState(this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(TYPE, EnumPlatformType.NORMAL), ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
        world.setBlockAndUpdate(pos, getUpdatedState(state, world, pos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (state.getValue(TYPE).equals(EnumPlatformType.INNER_CORNER_LEFT)) {
            return Shapes.or(
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing.getClockWise()
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 13, 0,
                            16, 16, 16,
                            facing
                    )
            );
        } else if (state.getValue(TYPE).equals(EnumPlatformType.INNER_CORNER_RIGHT)) {
            return Shapes.or(
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing.getCounterClockWise()
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 13, 0,
                            16, 16, 16,
                            facing
                    )
            );
        } else if (state.getValue(TYPE).equals(EnumPlatformType.OUTER_CORNER_LEFT)) {
            return Shapes.or(
                    MetroBlockUtil.getVoxelShapeByDirection(
                            6, 0, 0,
                            16, 16, 10,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 13, 0,
                            16, 16, 16,
                            facing
                    )
            );
        } else if (state.getValue(TYPE).equals(EnumPlatformType.OUTER_CORNER_RIGHT)) {
            return Shapes.or(
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            10, 16, 10,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 13, 0,
                            16, 16, 16,
                            facing
                    )
            );
        }
        return Shapes.or(
                MetroBlockUtil.getVoxelShapeByDirection(
                        0, 0, 0,
                        16, 16, 10,
                        facing
                ),
                MetroBlockUtil.getVoxelShapeByDirection(
                        0, 13, 0,
                        16, 16, 16,
                        facing
                )
        );
    }

    protected BlockState getUpdatedState(BlockState last, LevelAccessor world, BlockPos pos) {
        Direction facing = last.getValue(FACING);

        BlockState right = world.getBlockState(pos.relative(facing.getCounterClockWise()));
        BlockState left = world.getBlockState(pos.relative(facing.getClockWise()));
        BlockState front = world.getBlockState(pos.relative(facing.getOpposite()));
        BlockState back = world.getBlockState(pos.relative(facing));

        if (left.getBlock() instanceof BlockMetroPlatform && left.getValue(FACING).equals(facing) &&
                right.getBlock() instanceof BlockMetroPlatform && right.getValue(FACING).equals(facing)) {
            return last.setValue(TYPE, EnumPlatformType.NORMAL);
        } else if (back.getBlock() instanceof BlockMetroPlatform && back.getValue(FACING).equals(facing.getClockWise())) {
            return last.setValue(TYPE, EnumPlatformType.OUTER_CORNER_LEFT);
        } else if (back.getBlock() instanceof BlockMetroPlatform && back.getValue(FACING).equals(facing.getCounterClockWise())) {
            return last.setValue(TYPE, EnumPlatformType.OUTER_CORNER_RIGHT);
        } else if (front.getBlock() instanceof BlockMetroPlatform && front.getValue(FACING).equals(facing.getClockWise())) {
            return last.setValue(TYPE, EnumPlatformType.INNER_CORNER_LEFT);
        } else if (front.getBlock() instanceof BlockMetroPlatform && front.getValue(FACING).equals(facing.getCounterClockWise())) {
            return last.setValue(TYPE, EnumPlatformType.INNER_CORNER_RIGHT);
        } else if (left.getBlock() instanceof BlockMetroPlatform && left.getValue(FACING).equals(facing) &&
                !(right.getBlock() instanceof BlockMetroPlatform)) {
            return last.setValue(TYPE, EnumPlatformType.NORMAL);
        } else if (!(left.getBlock() instanceof BlockMetroPlatform) &&
                right.getBlock() instanceof BlockMetroPlatform && right.getValue(FACING).equals(facing)) {
            return last.setValue(TYPE, EnumPlatformType.NORMAL);
        } else {
            return last.setValue(TYPE, EnumPlatformType.NORMAL);
        }
    }

    public enum EnumPlatformType implements StringRepresentable {
        NORMAL,
        OUTER_CORNER_LEFT,
        OUTER_CORNER_RIGHT,
        INNER_CORNER_LEFT,
        INNER_CORNER_RIGHT;

        @Override
        public String getSerializedName() {
            return this.toString().toLowerCase();
        }
    }
}
