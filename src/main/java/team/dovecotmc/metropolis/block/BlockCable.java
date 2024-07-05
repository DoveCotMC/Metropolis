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
    protected BlockCable(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
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
        // TODO: Get state
        Block finalBlock = MetroBlocks.BLOCK_CABLE;

        if (world.getBlockState(pos.up()).getBlock() instanceof BlockCable && world.getBlockState(pos.down()).getBlock() instanceof BlockCable) {
            finalBlock = MetroBlocks.BLOCK_CABLE_HORIZONTAL;
        } else if (world.getBlockState(pos.up()).getBlock() instanceof BlockCable) {
            if (world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).getBlock() instanceof BlockCable) {
                finalBlock = MetroBlocks.BLOCK_CABLE_UP_RIGHT;
            } else if (world.getBlockState(pos.offset(facing.rotateYClockwise())).getBlock() instanceof BlockCable) {
                finalBlock = MetroBlocks.BLOCK_CABLE_UP_LEFT;
            }
        } else if (world.getBlockState(pos.down()).getBlock() instanceof BlockCable) {
            if (world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).getBlock() instanceof BlockCable) {
                finalBlock = MetroBlocks.BLOCK_CABLE_DOWN_RIGHT;
            } else if (world.getBlockState(pos.offset(facing.rotateYClockwise())).getBlock() instanceof BlockCable) {
                finalBlock = MetroBlocks.BLOCK_CABLE_DOWN_LEFT;
            }
        }

        return finalBlock.getStateWithProperties(state);
    }
}
