package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

public class BlockTrainSpeedLimitSign extends AbstractBlockTracksideSignBase {
    public BlockTrainSpeedLimitSign(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction facing = blockState.getValue(FACING);
        return Shapes.or(
                MetroBlockUtil.getVoxelShapeByDirection(
                        6, 0, 6,
                        10, 16, 10,
                        facing
                ),
                MetroBlockUtil.getVoxelShapeByDirection(
                        2, 3.5, 5.5,
                        14, 12.5, 8,
                        facing
                )
        );
    }
}
