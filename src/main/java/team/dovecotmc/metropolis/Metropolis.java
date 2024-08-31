package team.dovecotmc.metropolis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.config.MetroConfig;
import team.dovecotmc.metropolis.entity.EntitySittable;
import team.dovecotmc.metropolis.entity.MetroEntities;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
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
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "all"))
            .icon(() -> new ItemStack(MetroItems.ITEM_ITV_MONITOR))
            .build();
    public static final MetroConfig config = MetroConfig.load();

    @Override
    public void onInitialize() {
        MetroBlocks.initialize();
        MetroBlockEntities.initialize();
        MetroEntities.initialize();
        MetroItems.initialize();
        MetroServerNetwork.registerAll();
        SittableRegistries.registerSittable(new SittableRegistry(MetroBlocks.BLOCK_BENCH, (state, player, hit) -> Optional.of(new Vec3d(0.5, 0.0, 0.5))));
//        MetroEnumUtil.addRailtype("rail_5", 5, MapColor.BLUE, false, true, true, RailType.RailSlopeStyle.CURVE);

        UseBlockCallback.EVENT.register(
                (player, world, hand, hitResult) -> EntitySittable.trySit(world, hitResult.getBlockPos(), world.getBlockState(hitResult.getBlockPos()), hitResult, player) ?
                        ActionResult.SUCCESS :
                        ActionResult.PASS
        );
    }
}
