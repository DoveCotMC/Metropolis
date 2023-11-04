package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * @author Arrokoth
 * @project DovecotRailwayDeco-1.19.2
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class BlockCeilingA extends Block {
    public BlockCeilingA(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0.0, 10.0, 0.0, 16.0, 12.0, 16.0);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0.0, 10.0, 0.0, 16.0, 12.0, 16.0);
    }
}
