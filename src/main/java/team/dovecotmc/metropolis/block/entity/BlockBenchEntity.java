package team.dovecotmc.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockBenchEntity extends BlockEntity {
    public BlockBenchEntity(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.BENCH_BLOCK_ENTITY, pos, state);
        // TODO: Bench
    }
}
