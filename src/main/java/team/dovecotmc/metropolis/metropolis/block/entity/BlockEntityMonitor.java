package team.dovecotmc.metropolis.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityMonitor extends BlockEntity {
    public BlockEntityMonitor(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.MONITOR_BLOCK_ENTITY, pos, state);
    }
}
