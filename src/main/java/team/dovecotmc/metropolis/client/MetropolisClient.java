package team.dovecotmc.metropolis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.glfw.GLFW;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.client.block.entity.*;
import team.dovecotmc.metropolis.client.block.model.provider.MetroModelProvicer;
import team.dovecotmc.metropolis.client.config.MetroClientConfig;
import team.dovecotmc.metropolis.client.entity.EntitySittableRenderer;
import team.dovecotmc.old.metropolis.client.gui.MetroBlockPlaceHud;
import team.dovecotmc.old.metropolis.client.network.OldMetroClientNetwork;
import team.dovecotmc.metropolis.entity.MetroEntities;
import team.dovecotmc.old.metropolis.client.block.entity.BlockEntityRendererPSDSmallDoorSemiAuto;
import team.dovecotmc.old.metropolis.client.OldMetropolisClient;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class MetropolisClient implements ClientModInitializer {
    public static MetroClientConfig config = MetroClientConfig.load();

    public static final KeyMapping KEY_TOGGLE_SWITCH = new KeyMapping("metropolis.key.toggle_switch", GLFW.GLFW_KEY_LEFT_ALT, "metropolis.key.category.general");

    @Override
    public void onInitializeClient() {
        // Some nte stuff maybe?
        if (FabricLoader.getInstance().isModLoaded("mtrsteamloco")) {
            Metropolis.LOGGER.info("MTR-NTE detected!");
            // ??
        }
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            Metropolis.LOGGER.info("Mod menu detected!");
        }

        OldMetroClientNetwork.registerAll();

        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_BLIND_PATH, RenderType.cutout());

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new MetroModelProvicer());

        BlockEntityRendererRegistry.register(MetroBlockEntities.BUMPER_BLOCK_ENTITY, ctx -> new BumperBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.CAMERA_BLOCK_ENTITY, ctx -> new CameraBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.ITV_MONITOR_BLOCK_ENTITY, ctx -> new ITVMonitorBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.PSD_SMALL_DOOR, ctx -> new BlockEntityRendererPSDSmallDoorSemiAuto());

        EntityRendererRegistry.register(MetroEntities.SITTABLE, EntitySittableRenderer::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ResourceReloadListener());

        OldMetropolisClient.initializeContent();
    }

    private static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return new ResourceLocation(Metropolis.MOD_ID, "metropolis_custom_resources");
        }

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            Metropolis.LOGGER.info("Reloading!");
            MetropolisClient.config = MetroClientConfig.load();
        }
    }
}
