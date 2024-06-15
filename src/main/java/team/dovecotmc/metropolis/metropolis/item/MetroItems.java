package team.dovecotmc.metropolis.metropolis.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.MetroBlocks;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public class MetroItems {
    // Experimental features in other mods
//    public static final Item ITEM_CABLE_A = registerDovecotFeature("cable_a", new BlockItem(MetroBlocks.BLOCK_CABLE_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_1 = registerDovecotFeature("cable_a_1", new BlockItem(MetroBlocks.BLOCK_CABLE_A_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_2 = registerDovecotFeature("cable_a_2", new BlockItem(MetroBlocks.BLOCK_CABLE_A_2, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_3 = registerDovecotFeature("cable_a_3", new BlockItem(MetroBlocks.BLOCK_CABLE_A_3, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_4 = registerDovecotFeature("cable_a_4", new BlockItem(MetroBlocks.BLOCK_CABLE_A_4, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_5 = registerDovecotFeature("cable_a_5", new BlockItem(MetroBlocks.BLOCK_CABLE_A_5, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_6 = registerDovecotFeature("cable_a_6", new BlockItem(MetroBlocks.BLOCK_CABLE_A_6, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_7 = registerDovecotFeature("cable_a_7", new BlockItem(MetroBlocks.BLOCK_CABLE_A_7, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_8 = registerDovecotFeature("cable_a_8", new BlockItem(MetroBlocks.BLOCK_CABLE_A_8, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item ITEM_CABLE_A_9 = registerDovecotFeature("cable_a_9", new BlockItem(MetroBlocks.BLOCK_CABLE_A_9, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // TODO: Tunnel light
    public static final Item ITEM_TUNNEL_LIGHT_A = registerDovecotFeature("tunnel_light_a", new BlockItem(MetroBlocks.BLOCK_TUNNEL_LIGHT_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_MACHINE_A = registerDovecotFeature("ticket_machine_a", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_MACHINE_B = registerDovecotFeature("ticket_machine_b", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE_B, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_PANEL = register("ticket_vendor_panel", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_PANEL, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EM10 = register("ticket_vendor_em10", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_EM10, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EV23 = register("ticket_vendor_ev23", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_EV23, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // byd老闸机
    public static final Item ITEM_TICKET_BARRIER_ENTRANCE_A = registerDovecotFeature("ticket_barrier_entrance_a", new BlockItem(MetroBlocks.BLOCK_TICKET_BARRIER_ENTRANCE_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_BARRIER_EXIT_A = registerDovecotFeature("ticket_barrier_exit_a", new BlockItem(MetroBlocks.BLOCK_TICKET_BARRIER_EXIT_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item BLOCK_TICKET_VENDOR_UP_1 = register("ticket_vendor_up_1", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_UP_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item BLOCK_TICKET_VENDOR_UP_EV23 = register("ticket_vendor_up_ev23", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_UP_EV23, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item BLOCK_TICKET_VENDOR_UP_EV23_GREEN = register("ticket_vendor_up_ev23_green", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_UP_EV23_GREEN, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item BLOCK_TICKET_VENDOR_UP_EV23_YELLOW = register("ticket_vendor_up_ev23_yellow", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_UP_EV23_YELLOW, new Item.Settings().group(Metropolis.ITEM_GROUP)));
//    public static final Item BLOCK_TICKET_VENDOR_UP = register("ticket_vendor_up", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_UP_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Ticker barrier
    // Ceiling
    public static final Item ITEM_CEILING_A = registerDovecotFeature("ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Lamp
    public static final Item ITEM_FLUORESCENT_LAMP = register("fluorescent_lamp", new BlockItem(MetroBlocks.BLOCK_FLUORESCENT_LAMP, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    public static final Item ITEM_BUMPER = register("bumper", new BlockItem(MetroBlocks.BLOCK_BUMPER, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Turnstile
    public static final Item ITEM_TURNSTILE_A_ENTRANCE = register("turnstile", new BlockItem(MetroBlocks.BLOCK_TURNSTILE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Ticket Machine
    public static final Item ITEM_TICKET_MACHINE = register("ticket_machine", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Ticket
    public static final Item ITEM_TICKET = register("ticket", new ItemTicket(new Item.Settings().group(Metropolis.ITEM_GROUP), true));
    // TODO: 鸽达通 - Dovepus Card (
    public static final Item ITEM_CARD = register("card", new ItemTicket(new Item.Settings().group(Metropolis.ITEM_GROUP), false));
    // Monitor
    public static final Item ITEM_MONITOR = register("monitor", new BlockItem(MetroBlocks.BLOCK_MONITOR, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Concrete
    public static final Item ITEM_CONCRETE = register("concrete", new BlockItem(MetroBlocks.BLOCK_CONCRETE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    public static Item registerDovecotFeature(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Metropolis.MOD_ID, id), item);
    }

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Metropolis.MOD_ID, id), item);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Items");
    }
}
