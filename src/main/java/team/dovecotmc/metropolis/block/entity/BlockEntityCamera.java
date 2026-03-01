package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCamera extends BlockEntity {
    public BlockEntityCamera(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.CAMERA_BLOCK_ENTITY, pos, state);
    }
}
