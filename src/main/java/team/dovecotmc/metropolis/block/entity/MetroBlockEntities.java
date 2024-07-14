package team.dovecotmc.metropolis.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockEntities {
    public static final BlockEntityType<BlockEntityTicketVendor> TICKET_VENDOR_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "ticket_vendor"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTicketVendor::new, MetroBlocks.BLOCK_TICKET_VENDOR_EM10, MetroBlocks.BLOCK_TICKET_VENDOR_EV23, MetroBlocks.BLOCK_TICKET_VENDOR_EV23_YELLOW).build()
    );

    public static final BlockEntityType<BlockEntityMonitor> MONITOR_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "monitor"),
            FabricBlockEntityTypeBuilder.create(BlockEntityMonitor::new, MetroBlocks.BLOCK_MONITOR).build()
    );

    public static final BlockEntityType<BlockEntityTurnstile> TURNSTILE_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "turnstile"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTurnstile::new, MetroBlocks.BLOCK_TURNSTILE_ENTER).build()
    );

    public static final BlockEntityType<BlockEntityBumper> BUMPER_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "bumper"),
            FabricBlockEntityTypeBuilder.create(BlockEntityBumper::new, MetroBlocks.BLOCK_BUMPER).build()
    );

    public static final BlockEntityType<BlockEntitySecurityInspectionMachine> SECURITY_INSPECTION_MACHINE_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Metropolis.MOD_ID, "security_inspection_machine"),
            FabricBlockEntityTypeBuilder.create(BlockEntitySecurityInspectionMachine::new, MetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE).build()
    );

    public static void initialize() {
        Metropolis.LOGGER.info("Registering Block entities");
    }
}
