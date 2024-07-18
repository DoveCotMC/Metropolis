package team.dovecotmc.metropolis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class Metropolis implements ModInitializer {
    public static final String MOD_ID = "metropolis";
    public static final Logger LOGGER = LogManager.getLogger("Metropolis");
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "all"))
            .icon(() -> new ItemStack(MetroItems.ITEM_MONITOR))
            .build();
    public static final MetroConfig config = MetroConfig.load();

    @Override
    public void onInitialize() {
        MetroBlocks.initialize();
        MetroBlockEntities.initialize();
        MetroItems.initialize();
        MetroServerNetwork.registerAll();
//        MetroEnumUtil.addRailtype("rail_5", 5, MapColor.BLUE, false, true, true, RailType.RailSlopeStyle.CURVE);
    }
}
