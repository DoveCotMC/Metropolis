package team.dovecotmc.metropolis.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public class MetroItems {
    public static final Item ITEM_CABLE = register("cable", new BlockItem(MetroBlocks.BLOCK_CABLE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // TODO: Tunnel light
//    public static final Item ITEM_TUNNEL_LIGHT_A = register("tunnel_light_a", new BlockItem(MetroBlocks.BLOCK_TUNNEL_LIGHT_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_TOP = register("ticket_vendor_top", new ItemBlockWithTooltips(MetroBlocks.BLOCK_TICKET_VENDOR_TOP, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_PANEL = register("ticket_vendor_panel", new ItemBlockWithTooltips(MetroBlocks.BLOCK_TICKET_VENDOR_PANEL, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EM10 = register("ticket_vendor_em10", new ItemBlockWithTooltipShowStationHUD(MetroBlocks.BLOCK_TICKET_VENDOR_EM10, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EV23 = register("ticket_vendor_ev23", new ItemBlockWithTooltipShowStationHUD(MetroBlocks.BLOCK_TICKET_VENDOR_EV23, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_FARE_ADJ_EV23 = register("fare_adj_ev23", new ItemBlockWithTooltipShowStationHUD(MetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Ceiling
    public static final Item ITEM_CEILING_A = register("ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Lamp
    public static final Item ITEM_FLUORESCENT_LAMP = register("fluorescent_lamp", new BlockItem(MetroBlocks.BLOCK_FLUORESCENT_LAMP, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    public static final Item ITEM_BUMPER = register("bumper", new BlockItem(MetroBlocks.BLOCK_BUMPER, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CAMERA = register("camera", new BlockItem(MetroBlocks.BLOCK_CAMERA_CEILING, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Turnstile
    public static final Item ITEM_TURNSTILE_ENTER = register("turnstile_enter", new ItemTurnstile(MetroBlocks.BLOCK_TURNSTILE, new Item.Settings().group(Metropolis.ITEM_GROUP), BlockEntityTurnstile.EnumTurnstileType.ENTER));
    public static final Item ITEM_TURNSTILE_EXIT = register("turnstile_exit", new ItemTurnstile(MetroBlocks.BLOCK_TURNSTILE, new Item.Settings().group(Metropolis.ITEM_GROUP), BlockEntityTurnstile.EnumTurnstileType.EXIT));
    public static final Item ITEM_TURNSTILE_IC_ONLY_ENTER = register("turnstile_ic_only_enter", new ItemTurnstile(MetroBlocks.BLOCK_TURNSTILE_IC_ONLY, new Item.Settings().group(Metropolis.ITEM_GROUP), BlockEntityTurnstile.EnumTurnstileType.ENTER));
    public static final Item ITEM_TURNSTILE_IC_ONLY_EXIT = register("turnstile_ic_only_exit", new ItemTurnstile(MetroBlocks.BLOCK_TURNSTILE_IC_ONLY, new Item.Settings().group(Metropolis.ITEM_GROUP), BlockEntityTurnstile.EnumTurnstileType.EXIT));

    // Sign
    public static final Item ITEM_SIGN_NO_PHOTO = register("sign_no_photo", new BlockItem(MetroBlocks.BLOCK_SIGN_NO_PHOTO, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Ticket Machine
//    public static final Item ITEM_TICKET_MACHINE = register("ticket_machine", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // PIDS
//    public static final Item ITEM_PIDS_1 = register("pids_1", new ItemBlockShowStationHUD(MetroBlocks.BLOCK_PIDS_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Ticket
    public static final Item ITEM_SINGLE_TRIP_TICKET = register("single_trip_ticket", new ItemTicket(new Item.Settings(), true));
    public static final Item ITEM_SINGLE_TRIP_TICKET_USED = register("single_trip_ticket_used", new ItemTicket(new Item.Settings(), true));
    public static final Item ITEM_CARD = register("card", new ItemCard(new Item.Settings().group(Metropolis.ITEM_GROUP), false));
    public static final Item ITEM_CREATIVE_CARD = register("creative_card", new ItemCard(new Item.Settings().group(Metropolis.ITEM_GROUP), true));
    // Monitor
    public static final Item ITEM_ITV_MONITOR = register("itv_monitor", new BlockItem(MetroBlocks.BLOCK_ITV_MONITOR, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Security instruments
    public static final Item ITEM_SECURITY_DOOR = register("security_door", new BlockItem(MetroBlocks.BLOCK_SECURITY_DOOR, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_SECURITY_INSPECTION_MACHINE = register("security_inspection_machine", new BlockItem(MetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    /* =========== *
     * Decorations *
     * =========== */
    // Concrete
    public static final Item ITEM_CONCRETE = register("concrete", new BlockItem(MetroBlocks.BLOCK_CONCRETE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Platform
    public static final Item ITEM_PLATFORM_A = register("platform_a", new BlockItem(MetroBlocks.BLOCK_PLATFORM_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Tiles
    // White
    public static final Item ITEM_TILES_WHITE = register("tiles_white", new BlockItem(MetroBlocks.BLOCK_TILES_WHITE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TILES_LARGE_WHITE = register("tiles_large_white", new BlockItem(MetroBlocks.BLOCK_TILES_LARGE_WHITE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TILES_HORIZONTAL_WHITE = register("tiles_horizontal_white", new BlockItem(MetroBlocks.BLOCK_TILES_HORIZONTAL_WHITE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TILES_SMALL_WHITE = register("tiles_small_white", new BlockItem(MetroBlocks.BLOCK_TILES_SMALL_WHITE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Gray
    public static final Item ITEM_TILES_GRAY = register("tiles_gray", new BlockItem(MetroBlocks.BLOCK_TILES_GRAY, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TILES_HORIZONTAL_GRAY = register("tiles_horizontal_gray", new BlockItem(MetroBlocks.BLOCK_TILES_HORIZONTAL_GRAY, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TILES_SMALL_GRAY = register("tiles_small_gray", new BlockItem(MetroBlocks.BLOCK_TILES_SMALL_GRAY, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Cordon blocks
    public static final Item ITEM_CORDON_YELLOW_BLACK = register("cordon_yellow_black", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_YELLOW_BLACK, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CORDON_YELLOW_WHITE = register("cordon_yellow_white", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_YELLOW_WHITE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CORDON_RED_BLACK = register("cordon_red_black", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_RED_BLACK, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CORDON_RED_WHITE = register("cordon_red_white", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_RED_WHITE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Bench
    public static final Item ITEM_BENCH = register("bench", new BlockItem(MetroBlocks.BLOCK_BENCH, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // TODO: Blind path
//    public static final Item ITEM_BLIND_PATH_STRIP = register("blind_path_strip", new BlockItem(MetroBlocks.BLOCK_BLIND_PATH_STRIP, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_BLIND_PATH_POINT = register("blind_path_point", new BlockItem(MetroBlocks.BLOCK_BLIND_PATH_POINT, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // TODO?: Rails
//    public static final Item ITEM_RAIL_CONNECTOR_5 = register("rail_connector_5", new ItemRailModifier());
    // TODO: Bridge creator
//    public static final Item ITEM_BRIDGE_CREATOR = register("bridge_creator", new ItemDynamicBridgeCreator());

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Metropolis.MOD_ID, id), item);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Items");
    }
}
