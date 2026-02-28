package team.dovecotmc.metropolis.block;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

public class BlockBlindPath extends BlockHorizontalAxis {
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

    public BlockBlindPath(Properties properties) {
        super(properties.noOcclusion().noCollission());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(SHAPE).equals(Shape.STRAIGHT))
            return MetroBlockUtil.getVoxelShapeByDirection(
                    2, 0, 0,
                    14, 0.5, 16,
                    Direction.fromAxisAndDirection(blockState.getValue(AXIS), Direction.AxisDirection.POSITIVE)
            );

        return MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 0,
                16, 0.5, 16,
                Direction.NORTH
        );
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).isSolid();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return getUpdatedState(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), super.getStateForPlacement(blockPlaceContext));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
        BlockState newState = getUpdatedState(world, pos, state);

        if (newState == null)
            world.destroyBlock(pos, true);
        else
            world.setBlockAndUpdate(pos, newState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS).add(SHAPE);
    }

    public static @Nullable BlockState getUpdatedState(Level level, BlockPos pos, @Nullable BlockState superState) {
        if (superState == null)
            return null;

        if (!level.getBlockState(pos.below()).isSolid())
            return null;

        BlockState northState = level.getBlockState(pos.north());
        BlockState southState = level.getBlockState(pos.south());
        BlockState westState = level.getBlockState(pos.west());
        BlockState eastState = level.getBlockState(pos.east());

        int amountJunction = 0;

        if (isBlindPath(northState))
            amountJunction++;
        if (isBlindPath(southState))
            amountJunction++;
        if (isBlindPath(westState))
            amountJunction++;
        if (isBlindPath(eastState))
            amountJunction++;

        switch (amountJunction) {
            case 0, 1, 3, 4 -> superState = superState.setValue(AXIS, Direction.Axis.X).setValue(SHAPE, Shape.JUNCTION);
            case 2 -> {
                if (isBlindPath(northState) && isBlindPath(southState)) {
                    superState = superState.setValue(AXIS, Direction.Axis.Z).setValue(SHAPE, Shape.STRAIGHT);
                } else if (isBlindPath(westState) && isBlindPath(eastState)) {
                    superState = superState.setValue(AXIS, Direction.Axis.X).setValue(SHAPE, Shape.STRAIGHT);
                } else {
                    superState = superState.setValue(AXIS, Direction.Axis.X).setValue(SHAPE, Shape.JUNCTION);
                }
            }
        }

        return superState;
    }

    public static boolean isBlindPath(BlockState blockState) {
        return blockState.getBlock() instanceof BlockBlindPath;
    }

    public enum Shape implements StringRepresentable {
        JUNCTION("junction"),
        STRAIGHT("straight");

        private final String name;

        Shape(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
