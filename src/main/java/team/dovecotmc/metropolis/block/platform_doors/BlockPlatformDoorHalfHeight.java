package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;

public class BlockPlatformDoorHalfHeight extends AbstractBlockPlatformDoor {
    public BlockPlatformDoorHalfHeight(Properties properties) {
        super(properties.dynamicShape());
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
        BlockPos lowerPos = blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? blockPos.below() : blockPos;
        if (blockGetter.getBlockEntity(lowerPos) instanceof BlockEntityPlatformDoor door) {
            // Like MTR, automatic collision is evaluated by the client that receives vehicle door values.
            if (door.isOpen() || door.isAutomaticOpeningEnabled()
                    && door.getLevel() != null && !door.getLevel().isClientSide) {
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }
}
