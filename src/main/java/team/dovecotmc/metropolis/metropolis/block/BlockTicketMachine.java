package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.BlockDirectionalDoubleBlockBase;
import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTicketMachine extends BlockDirectionalDoubleBlockBase {
    public BlockTicketMachine() {
        super(Settings.of(Material.METAL).nonOpaque());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}
