package team.dovecotmc.old.metropolis.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import team.dovecotmc.old.metropolis.client.block.entity.FareAdjBlockEntityRenderer;
import team.dovecotmc.old.metropolis.client.block.entity.TicketVendorBlockEntityRenderer;
import team.dovecotmc.old.metropolis.client.block.entity.TurnstileBlockEntityRenderer;
import team.dovecotmc.old.metropolis.init.OldMetroBlocks;
import team.dovecotmc.old.metropolis.init.OldMetroBlockEntities;

public class OldMetropolisClient {
    public static void initializeContent() {
        boolean isMtr4Loaded;

        try {
            Class<?> clazz = Class.forName("org.mtr.mod.Init");
            isMtr4Loaded = true;
        } catch (ClassNotFoundException e) {
            isMtr4Loaded = false;
        }

        if (isMtr4Loaded) {
            BlockRenderLayerMap.INSTANCE.putBlock(OldMetroBlocks.BLOCK_TICKET_VENDOR_EM10, RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(OldMetroBlocks.BLOCK_TICKET_VENDOR_EV23, RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(OldMetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW, RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(OldMetroBlocks.BLOCK_TICKET_VENDOR_PANEL, RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(OldMetroBlocks.BLOCK_TICKET_VENDOR_TOP, RenderType.cutout());

            BlockEntityRendererRegistry.register(OldMetroBlockEntities.TURNSTILE_BLOCK_ENTITY, ctx -> new TurnstileBlockEntityRenderer());
            BlockEntityRendererRegistry.register(OldMetroBlockEntities.FARE_ADJ_BLOCK_ENTITY, ctx -> new FareAdjBlockEntityRenderer());
            BlockEntityRendererRegistry.register(OldMetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY, ctx -> new TicketVendorBlockEntityRenderer());
        }
    }
}
