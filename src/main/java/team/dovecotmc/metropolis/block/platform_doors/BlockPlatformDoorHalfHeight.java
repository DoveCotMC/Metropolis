package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

public class BlockPlatformDoorHalfHeight extends AbstractBlockPlatformDoor {
    public BlockPlatformDoorHalfHeight(Properties properties) {
        super(properties);
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
