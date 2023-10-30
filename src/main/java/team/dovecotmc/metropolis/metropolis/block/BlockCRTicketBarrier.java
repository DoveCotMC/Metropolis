package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.BlockTicketBarrier;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * @author Arrokoth
 * @project DovecotRailwayDeco-1.19.2
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class BlockCRTicketBarrier extends BlockTicketBarrier {
    public final boolean wide;

    public BlockCRTicketBarrier(boolean isEntrance, boolean wide) {
        super(isEntrance);
        this.wide = wide;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView blockGetter, BlockPos pos, ShapeContext collisionContext) {
        Direction facing = (Direction) IBlock.getStatePropertySafe(state, FACING);
        return VoxelShapes.combine(
                IBlock.getVoxelShapeByDirection(12.0, 0.0, 0.0, 16.0, 16.0, 16.0, facing),
                IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 1.0, 16.0, 16.0, facing),
                BooleanBiFunction.OR
        );
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
        TicketSystem.EnumTicketBarrierOpen open = (TicketSystem.EnumTicketBarrierOpen)IBlock.getStatePropertySafe(state, OPEN);
        VoxelShape base;
        if (this.wide) {
            base = VoxelShapes.combine(
                    IBlock.getVoxelShapeByDirection(12.0, 0.0, -5.0, 16.0, 24.0, 21.0, facing),
                    IBlock.getVoxelShapeByDirection(-4.0, 0.0, -5.0, 1.0, 24.0, 21.0, facing),
                    BooleanBiFunction.OR
            );
        } else {
            base = VoxelShapes.combine(
                    IBlock.getVoxelShapeByDirection(12.0, 0.0, -5.0, 16.0, 24.0, 21.0, facing),
                    IBlock.getVoxelShapeByDirection(0.0, 0.0, -5.0, 1.0, 24.0, 21.0, facing),
                    BooleanBiFunction.OR
            );
        }
        return open.isOpen() ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(1.0, 0.0, 7.0, 12.0, 24.0, 9.0, facing), base);
    }
}
