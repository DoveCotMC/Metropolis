package team.dovecotmc.old.metropolis.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.item.*;
import team.dovecotmc.old.metropolis.OldMetropolis;
import team.dovecotmc.old.metropolis.item.*;

public class OldMetroItems {
    // Security instruments
    public static final Item ITEM_SECURITY_DOOR = register("security_door", new BlockItem(OldMetroBlocks.BLOCK_SECURITY_DOOR, new Item.Properties()));
    public static final Item ITEM_SECURITY_INSPECTION_MACHINE = register("security_inspection_machine", new BlockItem(OldMetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE, new Item.Properties()));

    // PSD
    public static final Item ITEM_PSD_JR_DOOR_1 = register("psd_jr_door_1", new ItemBlockWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_DOOR_1, new Item.Properties()));
    public static final Item ITEM_PSD_JR_DOOR_2 = register("psd_jr_door_2", new ItemBlockWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_DOOR_2, new Item.Properties()));
    public static final Item ITEM_PSD_JR_FENCE_1 = register("psd_jr_fence_1", new ItemBlockWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_FENCE_1, new Item.Properties()));
    public static final Item ITEM_PSD_JR_FENCE_2 = register("psd_jr_fence_2", new ItemBlockWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_FENCE_2, new Item.Properties()/**/));

    // Platform
    public static final Item ITEM_PLATFORM_A = register("platform_a", new BlockItem(OldMetroBlocks.BLOCK_PLATFORM_A, new Item.Properties()));
    public static final Item ITEM_PLATFORM_A_SLIM = register("platform_a_slim", new BlockItem(OldMetroBlocks.BLOCK_PLATFORM_A_SLIM, new Item.Properties()));

    // Ticket System
    public static final Item ITEM_SINGLE_TRIP_TICKET = register("single_trip_ticket", new ItemTicket(new Item.Properties(), true));
    public static final Item ITEM_SINGLE_TRIP_TICKET_USED = register("single_trip_ticket_used", new ItemTicket(new Item.Properties(), true));
    public static final Item ITEM_CARD = register("card", new ItemCard(new Item.Properties(), false));
    public static final Item ITEM_CREATIVE_CARD = register("creative_card", new ItemCard(new Item.Properties(), true));
    public static final Item ITEM_EXIT_TICKET = register("exit_ticket", new ItemExitTicket(new Item.Properties()));

    // Vendors
    public static final Item ITEM_TICKET_VENDOR_TOP = register("ticket_vendor_top", new ItemBlockWithTooltips(OldMetroBlocks.BLOCK_TICKET_VENDOR_TOP, new Item.Properties()));
    public static final Item ITEM_TICKET_VENDOR_PANEL = register("ticket_vendor_panel", new ItemBlockWithTooltips(OldMetroBlocks.BLOCK_TICKET_VENDOR_PANEL, new Item.Properties()));
    public static final Item ITEM_TICKET_VENDOR_EM10 = register("ticket_vendor_em10", new ItemBlockWithTooltipShowStationHUD(OldMetroBlocks.BLOCK_TICKET_VENDOR_EM10, new Item.Properties()));
    public static final Item ITEM_TICKET_VENDOR_EV23 = register("ticket_vendor_ev23", new ItemBlockWithTooltipShowStationHUD(OldMetroBlocks.BLOCK_TICKET_VENDOR_EV23, new Item.Properties()));
    public static final Item ITEM_FARE_ADJ_EV23 = register("fare_adj_ev23", new ItemBlockWithTooltipShowStationHUD(OldMetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW, new Item.Properties()));

    // Turnstile
    public static final Item ITEM_TURNSTILE_ENTER = register("turnstile_enter", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.ENTER));
    public static final Item ITEM_TURNSTILE_EXIT = register("turnstile_exit", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.EXIT));
    public static final Item ITEM_TURNSTILE_IC_ONLY_ENTER = register("turnstile_ic_only_enter", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE_IC_ONLY, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.ENTER));
    public static final Item ITEM_TURNSTILE_IC_ONLY_EXIT = register("turnstile_ic_only_exit", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE_IC_ONLY, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.EXIT));

    public static Item register(String id, Item item) {
        return register(id, item, true);
    }

    public static Item register(String id, Item item, boolean addToTab) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Metropolis.MOD_ID, id), item);
        if (addToTab && OldMetropolis.shouldAddItemsToTab()) {
            MetroItems.TAB_ITEMS.add(registered);
        }
        return registered;
    }

    public static void initialize() {
    }
}
