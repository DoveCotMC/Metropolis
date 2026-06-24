package team.dovecotmc.metropolis.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockEntities {
    public static final BlockEntityType<BlockEntityITVMonitor> ITV_MONITOR_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "itv_monitor"),
            FabricBlockEntityTypeBuilder.create(BlockEntityITVMonitor::new, MetroBlocks.BLOCK_ITV_MONITOR).build()
    );

    public static final BlockEntityType<BlockEntityBumper> BUMPER_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "bumper"),
            FabricBlockEntityTypeBuilder.create(BlockEntityBumper::new, MetroBlocks.BLOCK_BUMPER).build()
    );

    public static final BlockEntityType<BlockEntityCamera> CAMERA_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "camera"),
            FabricBlockEntityTypeBuilder.create(BlockEntityCamera::new, MetroBlocks.BLOCK_CAMERA_CEILING).build()
    );

    public static final BlockEntityType<BlockEntityTrainStopSign> TRAIN_STOP_SIGN_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Metropolis.MOD_ID, "train_stop_sign"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTrainStopSign::new, MetroBlocks.BLOCK_TRAIN_STOP_SIGN).build()
    );

    public static void initialize() {
        Metropolis.LOGGER.info("Registering Block entities");
    }
}
