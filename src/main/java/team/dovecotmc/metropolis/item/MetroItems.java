package team.dovecotmc.metropolis.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.old.metropolis.init.OldMetroBlocks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public class MetroItems {
    public static final List<Item> TAB_ITEMS = new ArrayList<>();

    public static final Item ITEM_CABLE = register("cable", new BlockItem(MetroBlocks.BLOCK_CABLE, new Item.Properties()));

    // Ceiling
    public static final Item ITEM_CEILING_A = register("ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Properties()));

    // Lamp
    public static final Item ITEM_FLUORESCENT_LAMP = register("fluorescent_lamp", new BlockItem(MetroBlocks.BLOCK_FLUORESCENT_LAMP, new Item.Properties()));

    public static final Item ITEM_BUMPER = register("bumper", new BlockItem(MetroBlocks.BLOCK_BUMPER, new Item.Properties()));
    public static final Item ITEM_CAMERA = register("camera", new BlockItem(MetroBlocks.BLOCK_CAMERA_CEILING, new Item.Properties()));

    // Sign
    public static final Item ITEM_SIGN_NO_PHOTO = register("sign_no_photo", new BlockItem(MetroBlocks.BLOCK_SIGN_NO_PHOTO, new Item.Properties()), false);

    // Ticket Machine
//    public static final Item ITEM_TICKET_MACHINE = register("ticket_machine", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Monitor
    public static final Item ITEM_ITV_MONITOR = register("itv_monitor", new BlockItem(MetroBlocks.BLOCK_ITV_MONITOR, new Item.Properties()));

    /* =========== *
     * Decorations *
     * =========== */
    // Concrete
    public static final Item ITEM_CONCRETE = register("concrete", new BlockItem(MetroBlocks.BLOCK_CONCRETE, new Item.Properties()));

    // Tiles
    // White
    public static final Item ITEM_TILES_WHITE = register("tiles_white", new BlockItem(MetroBlocks.BLOCK_TILES_WHITE, new Item.Properties()));
    public static final Item ITEM_TILES_LARGE_WHITE = register("tiles_large_white", new BlockItem(MetroBlocks.BLOCK_TILES_LARGE_WHITE, new Item.Properties()));
    public static final Item ITEM_TILES_HORIZONTAL_WHITE = register("tiles_horizontal_white", new BlockItem(MetroBlocks.BLOCK_TILES_HORIZONTAL_WHITE, new Item.Properties()));
    public static final Item ITEM_TILES_SMALL_WHITE = register("tiles_small_white", new BlockItem(MetroBlocks.BLOCK_TILES_SMALL_WHITE, new Item.Properties()));

    // Gray
    public static final Item ITEM_TILES_GRAY = register("tiles_gray", new BlockItem(MetroBlocks.BLOCK_TILES_GRAY, new Item.Properties()));
    public static final Item ITEM_TILES_HORIZONTAL_GRAY = register("tiles_horizontal_gray", new BlockItem(MetroBlocks.BLOCK_TILES_HORIZONTAL_GRAY, new Item.Properties()));
    public static final Item ITEM_TILES_SMALL_GRAY = register("tiles_small_gray", new BlockItem(MetroBlocks.BLOCK_TILES_SMALL_GRAY, new Item.Properties()));

    // Cordon blocks
    public static final Item ITEM_CORDON_YELLOW_BLACK = register("cordon_yellow_black", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_YELLOW_BLACK, new Item.Properties()));
    public static final Item ITEM_CORDON_YELLOW_WHITE = register("cordon_yellow_white", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_YELLOW_WHITE, new Item.Properties()));
    public static final Item ITEM_CORDON_RED_BLACK = register("cordon_red_black", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_RED_BLACK, new Item.Properties()));
    public static final Item ITEM_CORDON_RED_WHITE = register("cordon_red_white", new ItemBlockWithTooltips(MetroBlocks.BLOCK_CORDON_RED_WHITE, new Item.Properties()));

    // Bench
    public static final Item ITEM_BENCH = register("bench", new BlockItem(MetroBlocks.BLOCK_BENCH, new Item.Properties()));

    // Awning
    public static final Item ITEM_AWNING_PILLAR = register("awning_pillar", new BlockItem(MetroBlocks.BLOCK_AWNING_PILLAR, new Item.Properties()));
    public static final Item ITEM_AWNING_PILLAR_EMERGENCY = register("awning_pillar_emergency", new BlockItem(MetroBlocks.BLOCK_AWNING_PILLAR_EMERGENCY, new Item.Properties()));
    public static final Item ITEM_AWNING_BEAM = register("awning_beam", new BlockItem(MetroBlocks.BLOCK_AWNING_BEAM, new Item.Properties()));
    public static final Item ITEM_AWNING_ROOF = register("awning_roof", new BlockItem(MetroBlocks.BLOCK_AWNING_ROOF, new Item.Properties()));

    // Blind path
    public static final Item ITEM_BLIND_PATH = register("blind_path", new BlockItem(MetroBlocks.BLOCK_BLIND_PATH, new Item.Properties()));

    // Platform
    public static final Item ITEM_PLATFORM_A = register("platform_a", new BlockItem(MetroBlocks.BLOCK_PLATFORM_A, new Item.Properties()));
    public static final Item ITEM_PLATFORM_A_SLIM = register("platform_a_slim", new BlockItem(MetroBlocks.BLOCK_PLATFORM_A_SLIM, new Item.Properties()));

    // Sketch board
    public static final Item ITEM_SKETCH_BOARD = register("sketch_board", new ItemSketchBoard(new Item.Properties().stacksTo(1)));

    public static Item register(String id, Item item) {
        return register(id, item, true);
    }

    public static Item register(String id, Item item, boolean displayInTab) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Metropolis.MOD_ID, id), item);
        if (displayInTab) {
            TAB_ITEMS.add(registered);
        }
        return registered;
    }

    public static List<Item> getTabItems() {
        return TAB_ITEMS;
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Items");
    }
}
