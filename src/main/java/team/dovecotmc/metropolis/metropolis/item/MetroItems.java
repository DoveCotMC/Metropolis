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
    // byd老闸机
    public static final Item ITEM_TICKET_BARRIER_ENTRANCE_A = registerDovecotFeature("ticket_barrier_entrance_a", new BlockItem(MetroBlocks.BLOCK_TICKET_BARRIER_ENTRANCE_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_BARRIER_EXIT_A = registerDovecotFeature("ticket_barrier_exit_a", new BlockItem(MetroBlocks.BLOCK_TICKET_BARRIER_EXIT_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Ticker barrier
    // Ceiling
    public static final Item ITEM_CEILING_A = registerDovecotFeature("ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    public static final Item ITEM_BUMPER_A = register("bumper_a", new BlockItem(MetroBlocks.BLOCK_BUMPER_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Turnstile
    public static final Item ITEM_TURNSTILE_A_ENTRANCE = register("turnstile_a_entrance", new BlockItem(MetroBlocks.BLOCK_TURNSTILE_A_ENTRANCE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TURNSTILE_A_EXIT = register("turnstile_a_exit", new BlockItem(MetroBlocks.BLOCK_TURNSTILE_A_EXIT, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Ticket
    public static final Item ITEM_TICKET = register("ticket", new ItemTicket(new Item.Settings().group(Metropolis.ITEM_GROUP), true));

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
