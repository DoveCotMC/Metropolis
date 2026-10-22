package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

public class BlockPlatformDoorHalfHeight extends AbstractBlockPlatformDoor {
    private final boolean glass;

    public BlockPlatformDoorHalfHeight(Properties properties, DyeColor color, boolean glass) {
        super(properties, color);
        this.glass = glass;
    }

    @Override
    public @NotNull BlockState getTintedBlockState(BlockState oldState, DyeColor dyeColor) {
        Block block = null;
//        switch (dyeColor) {
//            case WHITE -> block = glass ? MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS : MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT;
//            case ORANGE -> block = glass ? MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS_ORANGE : MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_ORANGE;
//        }
        if (this.glass) {
            block = MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS_MAP.get(dyeColor);
        } else {
            block = MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_MAP.get(dyeColor);
        }

        if (block == null)
            return oldState;

        return block.defaultBlockState()
                .setValue(FACING, oldState.getValue(FACING))
                .setValue(FLIPPED, oldState.getValue(FLIPPED))
                .setValue(OPEN, oldState.getValue(OPEN))
                .setValue(HALF, oldState.getValue(HALF));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    0, -16, 5,
                    16, 10, 11,
                    blockState.getValue(FACING)
            );
        } else {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 5,
                    16, 26, 11,
                    blockState.getValue(FACING)
            );
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(OPEN)) {
            return Shapes.empty();
        } else {
            return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
        }
    }
}
