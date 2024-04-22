package team.dovecotmc.metropolis.metropolis.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.BlockMonitor;
import team.dovecotmc.metropolis.metropolis.block.MetroBlocks;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockEntities {
    public static final BlockEntityType<BlockEntityTicketMachine> TICKET_MACHINE_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "ticket_machine"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTicketMachine::new, MetroBlocks.BLOCK_TICKET_MACHINE).build());

    public static final BlockEntityType<BlockEntityMonitor> MONITOR_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "monitor"),
            FabricBlockEntityTypeBuilder.create(BlockEntityMonitor::new, MetroBlocks.BLOCK_MONITOR).build());

    public static final BlockEntityType<BlockEntityBumper> BUMPER_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "bumper"),
            FabricBlockEntityTypeBuilder.create(BlockEntityBumper::new, MetroBlocks.BLOCK_BUMPER).build());

    public static void initialize() {
        Metropolis.LOGGER.info("Registering Block entities");
    }
}
