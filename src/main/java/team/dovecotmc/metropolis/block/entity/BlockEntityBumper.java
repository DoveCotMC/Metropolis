package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityBumper extends BlockEntity {
    public BlockEntityBumper(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.BUMPER_BLOCK_ENTITY, pos, state);
    }
}
