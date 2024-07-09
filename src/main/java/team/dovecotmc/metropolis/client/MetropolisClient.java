package team.dovecotmc.metropolis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.client.block.entity.*;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.client.block.model.provider.MetroModelProvicer;
import team.dovecotmc.metropolis.client.gui.MetroBlockPlaceHud;
import team.dovecotmc.metropolis.client.network.MetroClientNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class MetropolisClient implements ClientModInitializer {
    public static final MetroBlockPlaceHud BLOCK_PLACE_HUD = new MetroBlockPlaceHud();

    @Override
    public void onInitializeClient() {
        // Some nte stuff maybe?
        if (FabricLoader.getInstance().isModLoaded("mtrsteamloco")) {
            Metropolis.LOGGER.info("MTR-NTE detected");
            // ??
        }

        MetroClientNetwork.registerAll();

        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_EM10, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_EV23, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_EV23_YELLOW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_PANEL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_TOP, RenderLayer.getCutout());

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new MetroModelProvicer());

        BlockEntityRendererRegistry.register(MetroBlockEntities.BUMPER_BLOCK_ENTITY, ctx -> new BumperBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.TURNSTILE_BLOCK_ENTITY, ctx -> new TurnstileBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY, ctx -> new TicketVendorBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.MONITOR_BLOCK_ENTITY, ctx -> new MonitorBlockEntityRenderer());

        HudRenderCallback.EVENT.register(BLOCK_PLACE_HUD::render);
    }
}
