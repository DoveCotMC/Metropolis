package team.dovecotmc.metropolis.block;

import mtr.block.BlockTrainAnnouncer;
import mtr.block.BlockTrainScheduleSensor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockPSDSmallFenceSemiAuto extends BlockHorizontalFacing implements IBlockPlatform {
    public BlockPSDSmallFenceSemiAuto(Settings settings) {
        super(settings.nonOpaque());
//        mtr.block.BlockPSDDoor
//        BlockTrainAnnouncer
//        BlockTrainScheduleSensor
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 4,
                16, 16, 9,
                state.get(FACING)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 4,
                16, 24, 9,
                state.get(FACING)
        );
    }
}
