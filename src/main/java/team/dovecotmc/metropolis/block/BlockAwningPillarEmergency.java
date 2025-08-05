package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockAwningPillarEmergency extends BlockHorizontalAxis implements IBlockAwningPillar {
    public BlockAwningPillarEmergency(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(4, 0, 4, 12, 16, 12);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getBlock() instanceof IBlockAwningPillar) {
            return Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(AXIS, ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getValue(AXIS));
        } else if (ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof IBlockAwningPillar) {
            return Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(AXIS, ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getValue(AXIS));
        } else {
            return super.getStateForPlacement(ctx);
        }
    }
}
