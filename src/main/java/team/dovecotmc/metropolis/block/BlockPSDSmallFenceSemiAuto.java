package team.dovecotmc.metropolis.block;

import mtr.block.BlockPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockPSDSmallFenceSemiAuto extends BlockHorizontalFacing implements IBlockPlatform {
    public BlockPSDSmallFenceSemiAuto(Properties settings) {
        super(settings.noOcclusion());
//        mtr.block.BlockPSDDoor
//        BlockTrainAnnouncer
//        BlockTrainScheduleSensor
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return state.isRedstoneConductor(world, pos.below()) || world.getBlockState(pos.below()).getBlock() instanceof IBlockPlatform || world.getBlockState(pos.below()).getBlock() instanceof BlockPlatform;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 4,
                16, 16, 9,
                state.getValue(FACING)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 4,
                16, 24, 9,
                state.getValue(FACING)
        );
    }
}
