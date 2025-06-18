package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockFluorescentLamp extends BlockHorizontalAxis {
    public BlockFluorescentLamp() {
        super(Properties.of(Material.METAL).strength(4.0f).noOcclusion().lightLevel(value -> 15));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above());
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!canSurvive(state, world, pos)) {
            world.destroyBlock(pos, true);
        }
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(4.0, 12.0, 0.0, 12.0, 16.0, 16.0, Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS)));
    }
}
