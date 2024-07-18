package team.dovecotmc.metropolis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project DovecotRailwayDeco-1.19.2
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings({"unused", "deprecation"})
public class BlockCable extends HorizontalFacingBlock {
    public final boolean isInnerCorner;
    public final boolean isOuterCorner;
    // False: left, True: right
    public final boolean cornerDirection;

    protected BlockCable(Settings settings, boolean isInnerCorner, boolean isOuterCorner, boolean cornerDirection) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
        this.isInnerCorner = isInnerCorner;
        this.isOuterCorner = isOuterCorner;
        this.cornerDirection = cornerDirection;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(MetroItems.ITEM_CABLE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (isInnerCorner) {
            if (cornerDirection) {
                return VoxelShapes.union(
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.get(FACING)),
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.get(FACING).rotateYCounterclockwise())
                );
            } else {
                return VoxelShapes.union(
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.get(FACING)),
                        MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.get(FACING).rotateYClockwise())
                );
            }
        } else if (isOuterCorner) {
            if (cornerDirection) {
                return MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 8, 16, 16, state.get(FACING));
            } else {
                return MetroBlockUtil.getVoxelShapeByDirection(8, 0, 8, 16, 16, 16, state.get(FACING));
            }
        }
        return MetroBlockUtil.getVoxelShapeByDirection(0, 0, 8, 16, 16, 16, state.get(FACING));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getStateForUpdate(ctx.getWorld(), ctx.getBlockPos(), this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()), ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getStateForUpdate(world, pos, state, state.get(FACING));
    }

    public BlockState getStateForUpdate(WorldAccess world, BlockPos pos, BlockState state, Direction facing) {
        Block finalBlock = MetroBlocks.BLOCK_CABLE;

        BlockState up = world.getBlockState(pos.up());
        BlockState down = world.getBlockState(pos.down());
        BlockState right = world.getBlockState(pos.offset(facing.rotateYCounterclockwise()));
        BlockState left = world.getBlockState(pos.offset(facing.rotateYClockwise()));
        BlockState front = world.getBlockState(pos.offset(facing.rotateYCounterclockwise().rotateYCounterclockwise()));
        BlockState end = world.getBlockState(pos.offset(facing.rotateYCounterclockwise().rotateYCounterclockwise().getOpposite()));

        int connectedCount = 0;
        if (up.getBlock() instanceof BlockCable && up.get(FACING) == facing) {
            connectedCount++;
        }
        if (down.getBlock() instanceof BlockCable && down.get(FACING) == facing) {
            connectedCount++;
        }
        if (right.getBlock() instanceof BlockCable && right.get(FACING) == facing) {
            connectedCount++;
        }
        if (left.getBlock() instanceof BlockCable && left.get(FACING) == facing) {
            connectedCount++;
        }

        if (front.getBlock() instanceof BlockCable && front.get(FACING) == facing.rotateYCounterclockwise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_OUTER_CORNER_LEFT;
        } else if (front.getBlock() instanceof BlockCable && front.get(FACING) == facing.rotateYClockwise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_OUTER_CORNER_RIGHT;
        } else if (end.getBlock() instanceof BlockCable && end.get(FACING) == facing.rotateYCounterclockwise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_INNER_CORNER_RIGHT;
        } else if (end.getBlock() instanceof BlockCable && end.get(FACING) == facing.rotateYClockwise()) {
            finalBlock = MetroBlocks.BLOCK_CABLE_INNER_CORNER_LEFT;
        } else if (connectedCount == 1) {
             if (
                    (up.getBlock() instanceof BlockCable && up.get(FACING) == facing) ||
                            (down.getBlock() instanceof BlockCable && down.get(FACING) == facing)
            ) {
                finalBlock = MetroBlocks.BLOCK_CABLE_HORIZONTAL;
            } else if (
                    (left.getBlock() instanceof BlockCable && left.get(FACING) == facing) ||
                            (right.getBlock() instanceof BlockCable && right.get(FACING) == facing)) {
                finalBlock = MetroBlocks.BLOCK_CABLE;
            }
        } else if (connectedCount == 2) {
            if (up.getBlock() instanceof BlockCable && down.getBlock() instanceof BlockCable && up.get(FACING) == facing && down.get(FACING) == facing) {
                finalBlock = MetroBlocks.BLOCK_CABLE_HORIZONTAL;
            } else if (right.getBlock() instanceof BlockCable && left.getBlock() instanceof BlockCable && right.get(FACING) == facing && left.get(FACING) == facing) {
                finalBlock = MetroBlocks.BLOCK_CABLE;
            } else if (up.getBlock() instanceof BlockCable && up.get(FACING) == facing) {
                if (right.getBlock() instanceof BlockCable) {
                    finalBlock = MetroBlocks.BLOCK_CABLE_UP_RIGHT;
                } else if (left.getBlock() instanceof BlockCable) {
                    finalBlock = MetroBlocks.BLOCK_CABLE_UP_LEFT;
                }
            } else if (down.getBlock() instanceof BlockCable) {
                if (world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).getBlock() instanceof BlockCable) {
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

        return finalBlock.getStateWithProperties(state);
    }
}
