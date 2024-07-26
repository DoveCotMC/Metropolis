package team.dovecotmc.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityFareAdjMachine extends BlockEntity {
    public BlockEntityFareAdjMachine(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.FARE_ADJ_MACHINE_BLOCK_ENTITY, pos, state);
    }
}
