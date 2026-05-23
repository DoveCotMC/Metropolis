package team.dovecotmc.metropolis;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.dovecotmc.old.metropolis.OldMetropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.config.MetroConfig;
import team.dovecotmc.metropolis.entity.EntitySittable;
import team.dovecotmc.metropolis.entity.MetroEntities;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.sittable.SittableRegistries;
import team.dovecotmc.metropolis.sittable.SittableRegistry;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class Metropolis implements ModInitializer {
    public static final String MOD_ID = "metropolis";
    public static final Logger LOGGER = LogManager.getLogger("Metropolis");
    public static final CreativeModeTab ITEM_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(MOD_ID, "all"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(MetroItems.ITEM_ITV_MONITOR))
                    .title(Component.translatable("itemGroup.metropolis.all"))
                    .displayItems((parameters, output) -> {
                        for (Item tabItem : MetroItems.getTabItems()) {
                            output.accept(tabItem);
                        }
                    })
                    .build()
    );
    public static final MetroConfig config = MetroConfig.load();

    @Override
    public void onInitialize() {
        MetroBlocks.initialize();
        MetroBlockEntities.initialize();
        MetroEntities.initialize();
        MetroItems.initialize();
        SittableRegistries.registerSittable(new SittableRegistry(MetroBlocks.BLOCK_BENCH, (state, player, hit) -> Optional.of(new Vec3(0.5, 0.1, 0.5))));

        OldMetropolis.initializeOldContent();

        UseBlockCallback.EVENT.register(
                (player, world, hand, hitResult) -> !player.isShiftKeyDown() && EntitySittable.trySit(world, hitResult.getBlockPos(), world.getBlockState(hitResult.getBlockPos()), hitResult, player) ?
                        InteractionResult.SUCCESS :
                        InteractionResult.PASS
        );
    }
}
