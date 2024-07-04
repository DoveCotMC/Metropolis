package team.dovecotmc.metropolis.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetroBlocks {
    // TODO: Merge Cables into a single block c
    public static final Block BLOCK_CABLE = registerDovecotFeatures("cable", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_INNER_CORNER = registerDovecotFeatures("cable_inner_corner", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_OUTER_CORNER = registerDovecotFeatures("cable_outer_corner", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_2 = registerDovecotFeatures("cable_2", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_3 = registerDovecotFeatures("cable_3", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_4 = registerDovecotFeatures("cable_4", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_5 = registerDovecotFeatures("cable_5", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_6 = registerDovecotFeatures("cable_6", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_7 = registerDovecotFeatures("cable_7", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_8 = registerDovecotFeatures("cable_8", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_9 = registerDovecotFeatures("cable_9", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    // Tunnel light
    public static final Block BLOCK_TUNNEL_LIGHT_A = registerDovecotFeatures("tunnel_light_a", new BlockTunnelLight(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).nonOpaque()));

    // Ticket Vendor Decos
    public static final Block BLOCK_TICKET_VENDOR_UP_1 = register("ticket_vendor_up_1", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EV23 = register("ticket_vendor_up_ev23", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EV23_GREEN = register("ticket_vendor_up_ev23_green", new BlockTicketVendorUp());
    public static final Block BLOCK_TICKET_VENDOR_UP_EV23_YELLOW = register("ticket_vendor_up_ev23_yellow", new BlockTicketVendorUp());

    // Ticket Vendor
    public static final Block BLOCK_TICKET_VENDOR_PANEL = register("ticket_vendor_panel", new BlockTicketVendor(false));
    public static final Block BLOCK_TICKET_VENDOR_EM10 = register("ticket_vendor_em10", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EV23));
    public static final Block BLOCK_TICKET_VENDOR_EV23 = register("ticket_vendor_ev23", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EV23_GREEN));
    public static final Block BLOCK_TICKET_VENDOR_EV23_YELLOW = register("ticket_vendor_ev23_yellow", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EV23_YELLOW));

    // Ceiling
    public static final Block BLOCK_CEILING_A = registerDovecotFeatures("ceiling_a", new BlockCeilingA(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).nonOpaque().luminance(1)));

    // Lamp
    public static final Block BLOCK_FLUORESCENT_LAMP = register("fluorescent_lamp", new BlockFluorescentLamp());
    // Train bumpers
    public static final Block BLOCK_BUMPER = register("bumper", new BlockBumper());

    // Turnstile
    public static final Block BLOCK_TURNSTILE_ENTER = register("turnstile", new BlockTurnstile());

    // PIDS
    public static final Block BLOCK_PIDS_1 = register("pids_1", new BlockMetroPIDSOne());

    // Monitor
    public static final Block BLOCK_MONITOR = register("monitor", new BlockMonitor());
    // Concrete
    public static final Block BLOCK_CONCRETE = register("concrete", new Block(AbstractBlock.Settings.of(Material.STONE, DyeColor.LIGHT_GRAY)));

    private static Block registerDovecotFeatures(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Metropolis.MOD_ID, id), block);
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Metropolis.MOD_ID, id), block);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Blocks");
    }
}
