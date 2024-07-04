package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockFluorescentLamp extends BlockHorizontalAxis {
    public BlockFluorescentLamp() {
        super(Settings.of(Material.METAL).nonOpaque().luminance(value -> 15));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(4.0, 12.0, 0.0, 12.0, 16.0, 16.0, Direction.get(Direction.AxisDirection.POSITIVE, state.get(AXIS)));
    }
}
