package team.dovecotmc.metropolis.metropolis.client.block.entity;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import team.dovecotmc.metropolis.metropolis.block.BlockMonitor;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityMonitor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MonitorBlockEntityRenderer implements BlockEntityRenderer<BlockEntityMonitor> {
    @Override
    public void render(BlockEntityMonitor entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockState state = entity.getCachedState();
        BakedModel model =  mc.getBakedModelManager().getBlockModels().getModel(state);

        matrices.push();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, (float) (state.get(BlockMonitor.ROTATION) * -22.5), 0)));
        matrices.translate(-0.5f, -0.5f, -0.5f);

        boolean bl = MinecraftClient.isAmbientOcclusionEnabled() && state.getLuminance() == 0 && model.useAmbientOcclusion();
        Vec3d vec3d = state.getModelOffset(entity.getWorld(), entity.getPos());
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);

        try {
            if (bl) {
                mc.getBlockRenderManager().getModelRenderer().renderSmooth(entity.getWorld(), model, state, entity.getPos(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, entity.getWorld().getRandom(), 0, overlay);
            } else {
                mc.getBlockRenderManager().getModelRenderer().renderFlat(entity.getWorld(), model, state, entity.getPos(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, entity.getWorld().getRandom(), 0, overlay);
            }

        } catch (Throwable var17) {
            CrashReport crashReport = CrashReport.create(var17, "Tesselating block model");
            CrashReportSection crashReportSection = crashReport.addElement("Block model being tesselated");
            CrashReportSection.addBlockInfo(crashReportSection, entity.getWorld(), entity.getPos(), state);
            crashReportSection.add("Using AO", bl);
            throw new CrashException(crashReport);
        } finally {
            matrices.pop();
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntityMonitor blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityMonitor blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
