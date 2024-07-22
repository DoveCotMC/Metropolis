package team.dovecotmc.metropolis.client.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import team.dovecotmc.metropolis.block.entity.BlockEntityITVMonitor;
import team.dovecotmc.metropolis.block.BlockITVMonitor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ITVMonitorBlockEntityRenderer implements BlockEntityRenderer<BlockEntityITVMonitor> {
    @Override
    public void render(BlockEntityITVMonitor entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockState state = entity.getCachedState();
        BakedModel model =  mc.getBakedModelManager().getBlockModels().getModel(state);

        matrices.push();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, (float) (state.get(BlockITVMonitor.ROTATION) * -22.5), 0)));
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
    public boolean rendersOutsideBoundingBox(BlockEntityITVMonitor blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityITVMonitor blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
