package team.dovecotmc.metropolis.item;

import mtr.block.BlockGlassFence;
import mtr.item.ItemBridgeCreator;
import mtr.item.ItemRailModifier;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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
    public static final Item ITEM_TICKET_VENDOR_PANEL = register("ticket_vendor_panel", new BlockItem(MetroBlocks.BLOCK_TICKET_VENDOR_PANEL, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EM10 = register("ticket_vendor_em10", new ItemBlockShowStationHUD(MetroBlocks.BLOCK_TICKET_VENDOR_EM10, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EV23 = register("ticket_vendor_ev23", new ItemBlockShowStationHUD(MetroBlocks.BLOCK_TICKET_VENDOR_EV23, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_VENDOR_EV23_YELLOW = register("ticket_vendor_ev23_yellow", new ItemBlockShowStationHUD(MetroBlocks.BLOCK_TICKET_VENDOR_EV23_YELLOW, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Ceiling
    public static final Item ITEM_CEILING_A = registerDovecotFeature("ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Lamp
    public static final Item ITEM_FLUORESCENT_LAMP = register("fluorescent_lamp", new BlockItem(MetroBlocks.BLOCK_FLUORESCENT_LAMP, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    public static final Item ITEM_BUMPER = register("bumper", new BlockItem(MetroBlocks.BLOCK_BUMPER, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Turnstile
    public static final Item ITEM_TURNSTILE_ENTER = register("turnstile_enter", new ItemTurnstile(MetroBlocks.BLOCK_TURNSTILE_ENTER, new Item.Settings().group(Metropolis.ITEM_GROUP), BlockEntityTurnstile.EnumTurnstileType.ENTER));
    public static final Item ITEM_TURNSTILE_EXIT = register("turnstile_exit", new ItemTurnstile(MetroBlocks.BLOCK_TURNSTILE_ENTER, new Item.Settings().group(Metropolis.ITEM_GROUP), BlockEntityTurnstile.EnumTurnstileType.EXIT));

    // Ticket Machine
//    public static final Item ITEM_TICKET_MACHINE = register("ticket_machine", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // PIDS
    public static final Item ITEM_PIDS_1 = register("pids_1", new ItemBlockShowStationHUD(MetroBlocks.BLOCK_PIDS_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Ticket
    public static final Item ITEM_TICKET = register("ticket", new ItemTicket(new Item.Settings().group(Metropolis.ITEM_GROUP), true));
    public static final Item ITEM_CARD = register("card", new ItemCard(new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Monitor
    public static final Item ITEM_MONITOR = register("monitor", new BlockItem(MetroBlocks.BLOCK_MONITOR, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Concrete
    public static final Item ITEM_CONCRETE = register("concrete", new BlockItem(MetroBlocks.BLOCK_CONCRETE, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // Rails
//    public static final Item ITEM_RAIL_CONNECTOR_5 = register("rail_connector_5", new ItemRailModifier());
    // Bridge creator
    public static final Item ITEM_BRIDGE_CREATOR = register("bridge_creator", new ItemDynamicBridgeCreator());

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
