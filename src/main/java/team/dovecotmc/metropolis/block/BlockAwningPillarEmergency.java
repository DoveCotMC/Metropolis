package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;

public class BlockAwningPillarEmergency extends BlockHorizontalAxis implements IBlockAwningPillar {
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

    public BlockAwningPillarEmergency(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(4, 0, 4, 12, 16, 12);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state;

        if (ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getBlock() instanceof IBlockAwningPillar) {
            state = Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(AXIS, ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getValue(AXIS));
        } else if (ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof IBlockAwningPillar) {
            state = Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(AXIS, ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getValue(AXIS));
        } else {
            state = super.getStateForPlacement(ctx);
        }

        // Additional rotation detection
        Direction direction = Direction.fromYRot(ctx.getRotation());
        if (state.getValue(AXIS).equals(Direction.Axis.X)) {
            Direction d0 = Direction.fromAxisAndDirection(state.getValue(AXIS), Direction.AxisDirection.POSITIVE);
            float delta = Mth.wrapDegrees(d0.toYRot() - ctx.getRotation() + 90);

            if (Mth.abs(delta) < 90) {
                state = state.setValue(FLIPPED, true);
            } else {
                state = state.setValue(FLIPPED, false);
            }
        } else {
            Direction d0 = Direction.fromAxisAndDirection(state.getValue(AXIS), Direction.AxisDirection.POSITIVE);
            float delta = Mth.wrapDegrees(d0.toYRot() - ctx.getRotation() + 90);

            if (Mth.abs(delta) < 90) {
                state = state.setValue(FLIPPED, true);
            } else {
                state = state.setValue(FLIPPED, false);
            }
        }

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FLIPPED);
    }
}
