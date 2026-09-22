package team.dovecotmc.metropolis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformFence;
import team.dovecotmc.metropolis.client.block.entity.*;
import team.dovecotmc.metropolis.client.block.model.provider.MetroModelProvicer;
import team.dovecotmc.metropolis.client.entity.EntitySittableRenderer;
import team.dovecotmc.metropolis.entity.MetroEntities;
import team.dovecotmc.old.metropolis.client.OldMetropolisClient;
import team.dovecotmc.old.metropolis.client.config.OldMetroClientConfig;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class MetropolisClient implements ClientModInitializer {
//    public static final KeyMapping KEY_TOGGLE_SWITCH = new KeyMapping("metropolis.key.toggle_switch", GLFW.GLFW_KEY_LEFT_ALT, "metropolis.key.category.general");

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            Metropolis.LOGGER.info("Mod menu detected!");
        }

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new MetroModelProvicer());

        // Render layers
        registerRenderLayers();

        // Block tint
        registerBlockTint();

        // Block entity renderers
        registerBlockEntityRenderers();

        // Entity renderer
        EntityRendererRegistry.register(MetroEntities.SITTABLE, EntitySittableRenderer::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ResourceReloadListener());

        OldMetropolisClient.initializeContent();
    }

    private void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TRAIN_STOP_SIGN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TRACKSIDE_SIGN_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_BLIND_PATH, RenderType.cutout());
    }

    private void registerBlockTint() {
        ColorProviderRegistry.BLOCK.register((blockState, blockAndTintGetter, blockPos, i) -> DyeColor.byId(blockState.getValue(AbstractBlockPlatformFence.TINT_COLOR)).getTextColor(), MetroBlocks.BLOCK_PLATFORM_FENCE_HALF_HEIGHT);
        ColorProviderRegistry.BLOCK.register((blockState, blockAndTintGetter, blockPos, i) -> DyeColor.byId(blockState.getValue(AbstractBlockPlatformFence.TINT_COLOR)).getTextColor(), MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT);
    }

    private void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(MetroBlockEntities.BUMPER_BLOCK_ENTITY, ctx -> new BumperBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.CAMERA_BLOCK_ENTITY, ctx -> new CameraBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.ITV_MONITOR_BLOCK_ENTITY, ctx -> new ITVMonitorBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.TRAIN_STOP_SIGN_BLOCK_ENTITY, ctx -> new TrainStopSignBlockEntityRenderer());
    }

    private static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return new ResourceLocation(Metropolis.MOD_ID, "metropolis_custom_resources");
        }

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            Metropolis.LOGGER.info("Reloading!");
            OldMetropolisClient.config = OldMetroClientConfig.load();
        }
    }
}
