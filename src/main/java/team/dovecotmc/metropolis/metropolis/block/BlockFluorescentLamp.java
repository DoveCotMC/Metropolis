package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.IBlock;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockFluorescentLamp extends HorizontalFacingBlock {
    public BlockFluorescentLamp() {
        super(Settings.of(Material.METAL).nonOpaque().luminance(value -> 14));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(4.0, 12.0, 0.0, 12.0, 16.0, 16.0, IBlock.getStatePropertySafe(state, FACING));
    }
}
