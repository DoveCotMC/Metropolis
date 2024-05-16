package team.dovecotmc.metropolis.metropolis.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.impl.client.rendering.WorldRenderContextImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.BlockMonitor;
import team.dovecotmc.metropolis.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityMonitor;
import team.dovecotmc.metropolis.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.metropolis.client.block.entity.BumperBlockEntityRenderer;
import team.dovecotmc.metropolis.metropolis.client.block.entity.MonitorBlockEntityRenderer;
import team.dovecotmc.metropolis.metropolis.client.block.entity.TicketMachineBlockEntityRenderer;
import team.dovecotmc.metropolis.metropolis.client.block.entity.TurnstileBlockEntityRenderer;
import team.dovecotmc.metropolis.metropolis.client.block.model.provider.BlockMonitorModelProvider;
import team.dovecotmc.metropolis.metropolis.client.gui.MetroScreens;
import team.dovecotmc.metropolis.metropolis.client.gui.TicketMachineScreen;

import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetropolisClient implements ClientModInitializer {
    public static final String RECEIVER_TICKET_MACHINE_NBT_UPDATE = "ticket_machine_nbt_update";

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Metropolis.ID_SCREEN_OPEN_TICKET_MACHINE, (client, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
//            System.out.println(buf.readVarInt());
            client.execute(() -> {
                client.setScreen(new TicketMachineScreen(stack));
            });
        });

        BlockRenderLayerMap.INSTANCE.putBlock(MetroBlocks.BLOCK_TICKET_MACHINE, RenderLayer.getCutout());
        BlockEntityRendererRegistry.register(MetroBlockEntities.TICKET_MACHINE_BLOCK_ENTITY, ctx -> new TicketMachineBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.BUMPER_BLOCK_ENTITY, ctx -> new BumperBlockEntityRenderer());
        BlockEntityRendererRegistry.register(MetroBlockEntities.TURNSTILE_BLOCK_ENTITY, ctx -> new TurnstileBlockEntityRenderer());


        // TODO: A new renderer...
        BlockEntityRendererRegistry.register(MetroBlockEntities.MONITOR_BLOCK_ENTITY, ctx -> new MonitorBlockEntityRenderer());
//        ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> new BlockMonitorModelProvider());
//        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
//            if (context instanceof WorldRenderContextImpl) {
//                ClientWorld world = context.world();
//                RenderSystem.assertOnRenderThread();
//                if (!Objects.isNull(world) && !Objects.isNull(((WorldRenderContextImpl) context).blockPos()) && world.getBlockState(((WorldRenderContextImpl) context).blockPos()).getBlock() instanceof BlockMonitor) {
//                    MinecraftClient mc = MinecraftClient.getInstance();
////                    BlockModelRenderer blockModelRenderer = mc.getBlockRenderManager().getModelRenderer();
//                    MatrixStack matrices = context.matrixStack();
//                    BlockState state = ((WorldRenderContextImpl) context).blockState();
//
//                    matrices.push();
//
//                    BakedModel model = mc.getBakedModelManager().getBlockModels().getModel(state);
//                    boolean bl = MinecraftClient.isAmbientOcclusionEnabled() && state.getLuminance() == 0 && model.useAmbientOcclusion();
////                    Vec3d vec3d = state.getModelOffset(context.world(), ((WorldRenderContextImpl) context).blockPos());
////                    matrices.translate(vec3d.x, vec3d.y, vec3d.z);
//                    matrices.translate(0.5f, 0.5f, 0.5f);
//                    matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, (float) (state.get(BlockMonitor.ROTATION) * -22.5), 0)));
//                    matrices.translate(-0.5f, -0.5f, -0.5f);
//                    matrices.scale(0.5f, 0.5f, 0.5f);
//
//                    try {
//                        if (bl) {
//                            mc.getBlockRenderManager().getModelRenderer().renderSmooth(context.world(), model, state, ((WorldRenderContextImpl) context).blockPos(), matrices, ((WorldRenderContextImpl) context).vertexConsumer(), false, context.world().getRandom(), 0, 655360);
//                        } else {
//                            mc.getBlockRenderManager().getModelRenderer().renderFlat(context.world(), model, state, ((WorldRenderContextImpl) context).blockPos(), matrices, ((WorldRenderContextImpl) context).vertexConsumer(), false, context.world().getRandom(), 0, 655360);
//                        }
//
//                    } catch (Throwable var17) {
//                        CrashReport crashReport = CrashReport.create(var17, "Tesselating block model");
//                        CrashReportSection crashReportSection = crashReport.addElement("Block model being tesselated");
//                        CrashReportSection.addBlockInfo(crashReportSection, context.world(), ((WorldRenderContextImpl) context).blockPos(), state);
//                        crashReportSection.add("Using AO", bl);
//                        throw new CrashException(crashReport);
//                    } finally {
//                        matrices.pop();
//                    }
//                }
//            }
//        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
        });
    }
}
