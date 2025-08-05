package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityITVMonitor extends BlockEntity {
    public BlockEntityITVMonitor(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.ITV_MONITOR_BLOCK_ENTITY, pos, state);
    }
}
