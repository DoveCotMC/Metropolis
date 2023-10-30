package team.dovecotmc.metropolis.metropolis.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.MetroBlocks;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetroItems {
    public static final Item ITEM_CABLE_A = register("cable_a", new BlockItem(MetroBlocks.BLOCK_CABLE_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_1 = register("cable_a_1", new BlockItem(MetroBlocks.BLOCK_CABLE_A_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_2 = register("cable_a_2", new BlockItem(MetroBlocks.BLOCK_CABLE_A_2, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_3 = register("cable_a_3", new BlockItem(MetroBlocks.BLOCK_CABLE_A_3, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_4 = register("cable_a_4", new BlockItem(MetroBlocks.BLOCK_CABLE_A_4, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_5 = register("cable_a_5", new BlockItem(MetroBlocks.BLOCK_CABLE_A_5, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_6 = register("cable_a_6", new BlockItem(MetroBlocks.BLOCK_CABLE_A_6, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_7 = register("cable_a_7", new BlockItem(MetroBlocks.BLOCK_CABLE_A_7, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_8 = register("cable_a_8", new BlockItem(MetroBlocks.BLOCK_CABLE_A_8, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CABLE_A_9 = register("cable_a_9", new BlockItem(MetroBlocks.BLOCK_CABLE_A_9, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // TODO: 隧道灯
    public static final Item ITEM_TUNNEL_LIGHT_A = register("tunnel_light_a", new BlockItem(MetroBlocks.BLOCK_TUNNEL_LIGHT_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_MACHINE_0 = register("ticket_machine_0", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE_0, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_MACHINE_1 = register("ticket_machine_1", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE_1, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // byd老闸机
    public static final Item ITEM_TICKET_BARRIER_ENTRANCE_0 = register("ticket_barrier_entrance_0", new BlockItem(MetroBlocks.BLOCK_TICKET_BARRIER_ENTRANCE_0, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_TICKET_BARRIER_EXIT_0 = register("ticket_barrier_exit_0", new BlockItem(MetroBlocks.BLOCK_TICKET_BARRIER_EXIT_0, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    // 国铁闸机
    // 天花板
    public static final Item ITEM_CEILING_A = register("ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Settings().group(Metropolis.ITEM_GROUP)));
    public static final Item ITEM_CEILING_A_LIGHT = register("ceiling_a_light", new BlockItem(MetroBlocks.BLOCK_CEILING_A_LIGHT, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Metropolis.MOD_ID, id), item);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Items");
    }
}
