package team.dovecotmc.old.metropolis.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.old.metropolis.block.entity.BlockEntityFareAdj;
import team.dovecotmc.old.metropolis.block.entity.BlockEntitySecurityInspectionMachine;
import team.dovecotmc.old.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.old.metropolis.block.entity.BlockEntityTurnstile;

public class OldMetroBlockEntities {
    public static final BlockEntityType<BlockEntitySecurityInspectionMachine> SECURITY_INSPECTION_MACHINE_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "security_inspection_machine"),
            FabricBlockEntityTypeBuilder.create(BlockEntitySecurityInspectionMachine::new, MetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE).build()
    );

    public static final BlockEntityType<BlockEntityTicketVendor> TICKET_VENDOR_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "ticket_vendor"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTicketVendor::new, OldMetroBlocks.BLOCK_TICKET_VENDOR_EM10, OldMetroBlocks.BLOCK_TICKET_VENDOR_EV23).build()
    );

    public static final BlockEntityType<BlockEntityFareAdj> FARE_ADJ_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "fare_adj"),
            FabricBlockEntityTypeBuilder.create(BlockEntityFareAdj::new, OldMetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW).build()
    );

    public static final BlockEntityType<BlockEntityTurnstile> TURNSTILE_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "turnstile"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTurnstile::new, OldMetroBlocks.BLOCK_TURNSTILE, OldMetroBlocks.BLOCK_TURNSTILE_IC_ONLY).build()
    );

    public static void initialize() {
    }
}
