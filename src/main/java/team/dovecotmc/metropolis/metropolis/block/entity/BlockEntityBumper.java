package team.dovecotmc.metropolis.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityBumper extends BlockEntity {
    public BlockEntityBumper(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.BUMPER_BLOCK_ENTITY, pos, state);
    }
}
