package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project DovecotRailwayDeco-1.19.2
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings({"unused", "deprecation"})
public class BlockCable extends HorizontalDirectionalBlock {
    public final boolean isInnerCorner;
    public final boolean isOuterCorner;
    // False: left, True: right
    public final boolean cornerDirection;

    protected BlockCable(Properties settings, boolean isInnerCorner, boolean isOuterCorner, boolean cornerDirection) {
        super(settings);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
        this.isInnerCorner = isInnerCorner;
        this.isOuterCorner = isOuterCorner;
        this.cornerDirection = cornerDirection;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(MetroItems.ITEM_CABLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (isInnerCorner) {
            if (cornerDirection) {
                return Shapes.or(
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.getValue(FACING)),
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.getValue(FACING).getCounterClockWise())
                );
            } else {
                return Shapes.or(
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.getValue(FACING)),
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.getValue(FACING).getClockWise())
                );
            }
        } else if (isOuterCorner) {
            if (cornerDirection) {
                return MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 8, 16, 16, state.getValue(FACING));
            } else {
                return MetroBlockUtil.getVoxelShapeByDirection(8, 0, 8, 16, 16, 16, state.getValue(FACING));
            }
        }
        return MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.getValue(FACING));
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getStateForUpdate(ctx.getLevel(), ctx.getClickedPos(), this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()), ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return getStateForUpdate(world, pos, state, state.getValue(FACING));
    }

    public BlockState getStateForUpdate(LevelAccessor world, BlockPos pos, BlockState state, Direction facing) {
        Block finalBlock = MetroBlocks.BLOCK_CABLE;

        BlockState up = world.getBlockState(pos.above());
        BlockState down = world.getBlockState(pos.below());
        BlockState right = world.getBlockState(pos.relative(facing.getCounterClockWise()));
        BlockState left = world.getBlockState(pos.relative(facing.getClockWise()));
        BlockState front = world.getBlockState(pos.relative(facing.getCounterClockWise().getCounterClockWise()));
        BlockState end = world.getBlockState(pos.relative(facing.getCounterClockWise().getCounterClockWise().getOpposite()));

        int connectedCount = 0;
        if (up.getBlock() instanceof BlockCable && up.getValue(FACING) == facing) {
            connectedCount++;
        }
        if (down.getBlock() instanceof BlockCable && down.getValue(FACING) == facing) {
            connectedCount++;
        }
        if (right.getBlock() instanceof BlockCable && right.getValue(FACING) == facing) {
            connectedCount++;
        }
        if (left.getBlock() instanceof BlockCable && left.getValue(FACING) == facing) {
            connectedCount++;
        }

        if (front.getBlock() instanceof BlockCable && front.getValue(FACING) == facing.getCounterClockWise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_OUTER_CORNER_LEFT;
        } else if (front.getBlock() instanceof BlockCable && front.getValue(FACING) == facing.getClockWise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_OUTER_CORNER_RIGHT;
        } else if (end.getBlock() instanceof BlockCable && end.getValue(FACING) == facing.getCounterClockWise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_INNER_CORNER_RIGHT;
        } else if (end.getBlock() instanceof BlockCable && end.getValue(FACING) == facing.getClockWise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_INNER_CORNER_LEFT;
        } else if (connectedCount == 1) {
             if (
                    (up.getBlock() instanceof BlockCable && up.getValue(FACING) == facing) ||
                            (down.getBlock() instanceof BlockCable && down.getValue(FACING) == facing)
            ) {
                finalBlock = MetroBlocks.BLOCK_CABLE_HORIZONTAL;
            } else if (
                    (left.getBlock() instanceof BlockCable && left.getValue(FACING) == facing) ||
                            (right.getBlock() instanceof BlockCable && right.getValue(FACING) == facing)) {
                finalBlock = MetroBlocks.BLOCK_CABLE;
            }
        } else if (connectedCount == 2) {
            if (up.getBlock() instanceof BlockCable && down.getBlock() instanceof BlockCable && up.getValue(FACING) == facing && down.getValue(FACING) == facing) {
                finalBlock = MetroBlocks.BLOCK_CABLE_HORIZONTAL;
            } else if (right.getBlock() instanceof BlockCable && left.getBlock() instanceof BlockCable && right.getValue(FACING) == facing && left.getValue(FACING) == facing) {
                finalBlock = MetroBlocks.BLOCK_CABLE;
            } else if (up.getBlock() instanceof BlockCable && up.getValue(FACING) == facing) {
                if (right.getBlock() instanceof BlockCable) {
                    finalBlock = MetroBlocks.BLOCK_CABLE_UP_RIGHT;
                } else if (left.getBlock() instanceof BlockCable) {
                    finalBlock = MetroBlocks.BLOCK_CABLE_UP_LEFT;
                }
            } else if (down.getBlock() instanceof BlockCable) {
                if (world.getBlockState(pos.relative(facing.getCounterClockWise())).getBlock() instanceof BlockCable) {
                    finalBlock = MetroBlocks.BLOCK_CABLE_DOWN_RIGHT;
                } else if (left.getBlock() instanceof BlockCable) {
                    finalBlock = MetroBlocks.BLOCK_CABLE_DOWN_LEFT;
                }
            }
        }

//        if (up.getBlock() instanceof BlockCable && down.getBlock() instanceof BlockCable) {
//            finalBlock = MetroBlocks.BLOCK_CABLE_HORIZONTAL;
//        } else if (up.getBlock() instanceof BlockCable) {
//            if (right.getBlock() instanceof BlockCable) {
//                finalBlock = MetroBlocks.BLOCK_CABLE_UP_RIGHT;
//            } else if (left.getBlock() instanceof BlockCable) {
//                finalBlock = MetroBlocks.BLOCK_CABLE_UP_LEFT;
//            }
//        } else if (down.getBlock() instanceof BlockCable) {
//            if (world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).getBlock() instanceof BlockCable) {
//                finalBlock = MetroBlocks.BLOCK_CABLE_DOWN_RIGHT;
//            } else if (left.getBlock() instanceof BlockCable) {
//                finalBlock = MetroBlocks.BLOCK_CABLE_DOWN_LEFT;
//            }
//        }

        return finalBlock.withPropertiesOf(state);
    }
}
