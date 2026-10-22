package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

public class BlockPlatformFenceHalfHeight extends AbstractBlockPlatformFence {
    public BlockPlatformFenceHalfHeight(Properties properties, DyeColor color) {
        super(properties, color);
    }

    @Override
    public @NotNull Item asItem() {
        return MetroItems.ITEM_PLATFORM_FENCE_HALF_HEIGHT;
    }

    @Override
    public @NotNull BlockState getTintedBlockState(BlockState oldState, DyeColor dyeColor) {
        Block block = MetroBlocks.BLOCK_PLATFORM_FENCE_HALF_HEIGHT_MAP.get(dyeColor);

        if (block == null)
            return oldState;

        return block.defaultBlockState()
                .setValue(FACING, oldState.getValue(FACING))
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
}
