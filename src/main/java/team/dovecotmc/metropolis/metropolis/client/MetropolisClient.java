package team.dovecotmc.metropolis.metropolis.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.client.CustomResources;
import mtr.render.RenderRailwaySign;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.fabricmc.fabric.impl.resource.loader.client.pack.ProgrammerArtResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.metadata.ModMetadataParser;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.metropolis.client.block.entity.*;
import team.dovecotmc.metropolis.metropolis.client.block.model.provider.MetroModelProvicer;
import team.dovecotmc.metropolis.metropolis.client.gui.TicketMachineScreen;
import team.dovecotmc.metropolis.metropolis.client.network.MetroClientNetwork;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetropolisClient implements ClientModInitializer {
    public static final String RECEIVER_TICKET_MACHINE_NBT_UPDATE = "ticket_machine_nbt_update";
    public static final String CUSTOM_RESOURCE_ID = "metropolis_custom_resources";

    @Override
    public void onInitializeClient() {
//        ClientPlayNetworking.registerGlobalReceiver(Metropolis.ID_SCREEN_OPEN_TICKET_MACHINE, (client, handler, buf, responseSender) -> {
//            ItemStack stack = buf.readItemStack();
////            System.out.println(buf.readVarInt());
//            client.execute(() -> {
//                client.setScreen(new TicketMachineScreen());
//            });
//        });

        // Load custom trains
        if (FabricLoader.getInstance().isModLoaded("mtrsteamloco")) {
            Metropolis.LOGGER.info("MTR-NTE detected");
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new CustomResourcesWrapper());
        }

        MetroClientNetwork.registerTicketVendorGuiReceiver();

        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_MACHINE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_EM10, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_EV23, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_EV23_YELLOW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_VENDOR_PANEL, RenderLayer.getCutout());

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new MetroModelProvicer());

        BlockEntityRendererRegistry.register(MetroBlockEntities.TICKET_MACHINE_BLOCK_ENTITY, ctx -> new TicketMachineBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.BUMPER_BLOCK_ENTITY, ctx -> new BumperBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.TURNSTILE_BLOCK_ENTITY, TurnstileBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(MetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY, ctx -> new TicketVendorBlockEntityRenderer());
        EntityModelLayerRegistry.registerModelLayer(TurnstileBlockEntityRenderer.MODEL_LAYER, TurnstileBlockEntityRenderer::getTexturedModelData);
        BlockEntityRendererRegistry.register(MetroBlockEntities.MONITOR_BLOCK_ENTITY, ctx -> new MonitorBlockEntityRenderer());

//        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
//        });
//
//        WorldRenderEvents.LAST.register(context -> {
//        });
    }

    static class CustomResourcesWrapper implements SimpleSynchronousResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return new Identifier(Metropolis.MOD_ID, CUSTOM_RESOURCE_ID);
        }

        @Override
        public void reload(ResourceManager manager) {
            MetroCustomResources.reload(manager);
//            ProgrammerArtResourcePack
//            FileResourcePackProvider
//            for (Resource resource : manager.getAllResources(new Identifier("mtr", "mtr_custom_resources"))) {
//                System.out.println(resource);
//            }
//            ModResourcePackUtil.openDefault(FabricLoader.getInstance().getModContainer(Metropolis.MOD_ID).get().getMetadata(), ResourceType.CLIENT_RESOURCES, "");
        }
    }
}
