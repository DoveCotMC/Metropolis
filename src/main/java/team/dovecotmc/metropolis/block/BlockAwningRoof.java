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
import team.dovecotmc.metropolis.util.MetroBlockUtil;

import java.util.Objects;

public class BlockAwningRoof extends BlockHorizontalAxis {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public BlockAwningRoof(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return getStateForUpdate(levelAccessor, blockPos, blockState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getStateForUpdate(ctx.getLevel(), ctx.getClickedPos(), Objects.requireNonNull(super.getStateForPlacement(ctx)));
    }

    public BlockState getStateForUpdate(LevelAccessor level, BlockPos pos, BlockState state) {
        Direction.Axis axis = state.getValue(AXIS);
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        BlockState leftState = level.getBlockState(pos.relative(facing.getClockWise()));
        BlockState rightState = level.getBlockState(pos.relative(facing.getCounterClockWise()));
        boolean left = leftState.getBlock() instanceof BlockAwningRoof || (leftState.getBlock() instanceof BlockAwningPillar && leftState.getValue(BlockAwningPillar.TYPE).equals(BlockAwningPillar.Type.TOP));
        boolean right = rightState.getBlock() instanceof BlockAwningRoof || (rightState.getBlock() instanceof BlockAwningPillar && rightState.getValue(BlockAwningPillar.TYPE).equals(BlockAwningPillar.Type.TOP));

        if (left && right) {
            return state.setValue(TYPE, Type.MIDDLE);
        } else if (right) {
            return state.setValue(TYPE, Type.RIGHT);
        } else if (left) {
            return state.setValue(TYPE, Type.LEFT);
        }
        return state.setValue(TYPE, Type.MIDDLE);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction facing = Direction.fromAxisAndDirection(blockState.getValue(AXIS), Direction.AxisDirection.POSITIVE);
        return Shapes.join(MetroBlockUtil.getVoxelShapeByDirection(0, 2, 0, 16, 7, 16, facing), MetroBlockUtil.getVoxelShapeByDirection(6, 0, 0, 10, 7, 16, facing), BooleanOp.OR);
    }

    public enum Type implements StringRepresentable {
        LEFT("left"),
        MIDDLE("middle"),
        RIGHT("right");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public java.lang.@NotNull String getSerializedName() {
            return this.name;
        }
    }
}
