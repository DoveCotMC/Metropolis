package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockFluorescentLamp extends BlockHorizontalAxis {
    public BlockFluorescentLamp() {
        super(Settings.of(Material.METAL).strength(4.0f).nonOpaque().luminance(value -> 15));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isSolidBlock(world, pos.up());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, true);
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(4.0, 12.0, 0.0, 12.0, 16.0, 16.0, Direction.get(Direction.AxisDirection.POSITIVE, state.get(AXIS)));
    }
}
