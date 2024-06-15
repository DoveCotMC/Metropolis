package team.dovecotmc.metropolis.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityTicketVendor extends BlockEntity {
    public BlockEntityTicketVendor(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY, pos, state);
    }
}
