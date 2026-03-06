package team.dovecotmc.old.metropolis.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.block.*;

public class OldMetroBlocks {
    // Security instruments
    public static final Block BLOCK_SECURITY_DOOR = register("security_door", new BlockSecurityDoor());
    public static final Block BLOCK_SECURITY_INSPECTION_MACHINE = register("security_inspection_machine", new BlockSecurityInspectionMachine());

    // Ticket Vendors
    public static final Block BLOCK_TICKET_VENDOR_UP_1 = register("ticket_vendor_up_1", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EM10 = register("ticket_vendor_up_em10", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EV23 = register("ticket_vendor_up_ev23", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EV23_GREEN = register("ticket_vendor_up_ev23_green", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EV23_YELLOW = register("ticket_vendor_up_ev23_yellow", new BlockTicketVendorUp());

    public static final Block BLOCK_TICKET_VENDOR_TOP = register("ticket_vendor_top", new BlockTicketVendorTop());

    // Ticket Vendor
    public static final Block BLOCK_TICKET_VENDOR_PANEL = register("ticket_vendor_panel", new BlockTicketVendor(false));
    public static final Block BLOCK_TICKET_VENDOR_EM10 = register("ticket_vendor_em10", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EM10));
    public static final Block BLOCK_TICKET_VENDOR_EV23 = register("ticket_vendor_ev23", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EV23_GREEN));
    public static final Block BLOCK_FARE_ADJ_EV23_YELLOW = register("fare_adj_ev23", new BlockFareAdjMachine(BLOCK_TICKET_VENDOR_UP_EV23_YELLOW));

    // Turnstile
    public static final Block BLOCK_TURNSTILE = register("turnstile", new BlockTurnstile(false));
    public static final Block BLOCK_TURNSTILE_IC_ONLY = register("turnstile_ic_only", new BlockTurnstile(true));

    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Metropolis.MOD_ID, id), block);
    }

    public static void initialize() {
        OldMetroBlockEntities.initialize();
    }
}
