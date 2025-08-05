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

public class BlockAwningPillar extends BlockHorizontalAxis implements IBlockAwningPillar {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public BlockAwningPillar(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return getStateForUpdate(levelAccessor, blockPos, blockState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState superState = getStateForUpdate(ctx.getLevel(), ctx.getClickedPos(), super.getStateForPlacement(ctx));
        if (ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getBlock() instanceof IBlockAwningPillar) {
            return superState.setValue(AXIS, ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getValue(AXIS));
        } else if (ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof IBlockAwningPillar) {
            return superState.setValue(AXIS, ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getValue(AXIS));
        } else {
            return superState;
        }
    }

    public BlockState getStateForUpdate(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockState above = level.getBlockState(pos.above());
        BlockState below = level.getBlockState(pos.below());
        if (above.getBlock() instanceof IBlockAwningPillar && below.getBlock() instanceof IBlockAwningPillar) {
            return state.setValue(TYPE, Type.MIDDLE);
        } else if (above.getBlock() instanceof IBlockAwningPillar) {
            if (above.getValue(AXIS).equals(state.getValue(AXIS))) {
                return state.setValue(TYPE, Type.BOTTOM);
            } else {
                return state.setValue(TYPE, Type.MIDDLE);
            }
        } else if (below.getBlock() instanceof IBlockAwningPillar) {
            if (below.getValue(AXIS).equals(state.getValue(AXIS))) {
                if (
                        level.getBlockState(pos.north()).getBlock() instanceof BlockAwningBeam ||
                                level.getBlockState(pos.south()).getBlock() instanceof BlockAwningBeam ||
                                level.getBlockState(pos.west()).getBlock() instanceof BlockAwningBeam ||
                                level.getBlockState(pos.east()).getBlock() instanceof BlockAwningBeam
                ) {
                    return state.setValue(TYPE, Type.TOP);
                } else {
                    return state.setValue(TYPE, Type.MIDDLE);
                }
            } else {
                return state.setValue(TYPE, Type.MIDDLE);
            }
        } else {
            return state.setValue(TYPE, Type.MIDDLE);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(TYPE).equals(Type.TOP)) {
            return Shapes.join(
                    Block.box(4, 0, 4, 12, 16, 12),
                    Shapes.join(
                            Block.box(0, 8, 4, 16, 16, 12),
                            Block.box(4, 8, 0, 12, 16, 16),
                            BooleanOp.OR
                    ),
                    BooleanOp.OR);
        } else {
            return Block.box(4, 0, 4, 12, 16, 12);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    public enum Type implements StringRepresentable {
        BOTTOM("bottom"),
        TOP("top"),
        MIDDLE("middle");

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
