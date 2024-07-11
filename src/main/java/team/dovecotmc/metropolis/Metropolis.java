package team.dovecotmc.metropolis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.config.MetroConfig;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroServerNetwork;

import java.util.Random;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class Metropolis implements ModInitializer {
    public static final String MOD_ID = "metropolis";
    public static final Logger LOGGER = LogManager.getLogger("Metropolis");
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "decorations"))
            .icon(() -> {
                Random random = new Random(System.currentTimeMillis());
//                return switch (random.nextInt(4)) {
//                    case 0 -> MetroItems.ITEM_CARD.getDefaultStack();
//                    case 1 -> MetroItems.ITEM_TICKET.getDefaultStack();
//                    case 2 -> MetroItems.ITEM_TILES_SMALL_WHITE.getDefaultStack();
//                    case 3 -> MetroItems.ITEM_TICKET_VENDOR_EV23.getDefaultStack();
//                    default -> MetroItems.ITEM_CABLE.getDefaultStack();
//                };
                return MetroItems.ITEM_TICKET_VENDOR_EV23.getDefaultStack();
            })
            .build();
    public static final MetroConfig config = new MetroConfig();

    @Override
    public void onInitialize() {
//        MetroConfig.save(config);
        MetroBlocks.initialize();
        MetroBlockEntities.initialize();
        MetroItems.initialize();
        MetroServerNetwork.registerAll();
//        MetroEnumUtil.addRailtype("rail_5", 5, MapColor.BLUE, false, true, true, RailType.RailSlopeStyle.CURVE);
    }
}
