package team.dovecotmc.metropolis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import team.dovecotmc.metropolis.IBlockPlatform;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockMetroPlatform extends HorizontalFacingBlock implements IBlockPlatform {
    public static final EnumProperty<EnumPlatformType> TYPE = EnumProperty.of("type", EnumPlatformType.class);

    public BlockMetroPlatform(Settings settings) {
        super(settings.nonOpaque());
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getUpdatedState(this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(TYPE, EnumPlatformType.NORMAL), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        world.setBlockState(pos, getUpdatedState(state, world, pos));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TYPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        if (state.get(TYPE).equals(EnumPlatformType.INNER_CORNER_LEFT)) {
            return VoxelShapes.union(
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing.rotateYClockwise()
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 13, 0,
                            16, 16, 16,
                            facing
                    )
            );
        } else if (state.get(TYPE).equals(EnumPlatformType.INNER_CORNER_RIGHT)) {
            return VoxelShapes.union(
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 0,
                            16, 16, 10,
                            facing.rotateYCounterclockwise()
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 13, 0,
                            16, 16, 16,
                            facing
                    )
            );
        } else if (state.get(TYPE).equals(EnumPlatformType.OUTER_CORNER_LEFT)) {
            return VoxelShapes.union(
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
        } else if (state.get(TYPE).equals(EnumPlatformType.OUTER_CORNER_RIGHT)) {
            return VoxelShapes.union(
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
        return VoxelShapes.union(
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

    protected BlockState getUpdatedState(BlockState last, WorldAccess world, BlockPos pos) {
        Direction facing = last.get(FACING);

        BlockState right = world.getBlockState(pos.offset(facing.rotateYCounterclockwise()));
        BlockState left = world.getBlockState(pos.offset(facing.rotateYClockwise()));
        BlockState front = world.getBlockState(pos.offset(facing.getOpposite()));
        BlockState back = world.getBlockState(pos.offset(facing));

        if (left.getBlock() instanceof BlockMetroPlatform && left.get(FACING).equals(facing) &&
                right.getBlock() instanceof BlockMetroPlatform && right.get(FACING).equals(facing)) {
            return last.with(TYPE, EnumPlatformType.NORMAL);
        } else if (back.getBlock() instanceof BlockMetroPlatform && back.get(FACING).equals(facing.rotateYClockwise())) {
            return last.with(TYPE, EnumPlatformType.OUTER_CORNER_LEFT);
        } else if (back.getBlock() instanceof BlockMetroPlatform && back.get(FACING).equals(facing.rotateYCounterclockwise())) {
            return last.with(TYPE, EnumPlatformType.OUTER_CORNER_RIGHT);
        } else if (front.getBlock() instanceof BlockMetroPlatform && front.get(FACING).equals(facing.rotateYClockwise())) {
            return last.with(TYPE, EnumPlatformType.INNER_CORNER_LEFT);
        } else if (front.getBlock() instanceof BlockMetroPlatform && front.get(FACING).equals(facing.rotateYCounterclockwise())) {
            return last.with(TYPE, EnumPlatformType.INNER_CORNER_RIGHT);
        } else if (left.getBlock() instanceof BlockMetroPlatform && left.get(FACING).equals(facing) &&
                !(right.getBlock() instanceof BlockMetroPlatform)) {
            return last.with(TYPE, EnumPlatformType.NORMAL);
        } else if (!(left.getBlock() instanceof BlockMetroPlatform) &&
                right.getBlock() instanceof BlockMetroPlatform && right.get(FACING).equals(facing)) {
            return last.with(TYPE, EnumPlatformType.NORMAL);
        } else {
            return last.with(TYPE, EnumPlatformType.NORMAL);
        }
    }

    public enum EnumPlatformType implements StringIdentifiable {
        NORMAL,
        OUTER_CORNER_LEFT,
        OUTER_CORNER_RIGHT,
        INNER_CORNER_LEFT,
        INNER_CORNER_RIGHT;

        @Override
        public String asString() {
            return this.toString().toLowerCase();
        }
    }
}
