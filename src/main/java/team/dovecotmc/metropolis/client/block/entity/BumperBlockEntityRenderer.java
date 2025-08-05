package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.dovecotmc.metropolis.block.BlockITVMonitor;
import team.dovecotmc.metropolis.block.entity.BlockEntityBumper;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BumperBlockEntityRenderer implements BlockEntityRenderer<BlockEntityBumper> {
    @Override
    public void render(BlockEntityBumper entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = entity.getBlockState();
        BakedModel model =  mc.getModelManager().getBlockModelShaper().getBlockModel(state);

        matrices.pushPose();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, (float) (state.getValue(BlockITVMonitor.ROTATION) * -22.5), 0)));
        matrices.translate(-0.5f, -0.5f, -0.5f);

        boolean bl = Minecraft.useAmbientOcclusion() && state.getLightEmission() == 0 && model.useAmbientOcclusion();
        Vec3 vec3d = state.getOffset(entity.getLevel(), entity.getBlockPos());
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);

        try {
            if (bl) {
                mc.getBlockRenderer().getModelRenderer().tesselateWithAO(entity.getLevel(), model, state, entity.getBlockPos(), matrices, vertexConsumers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(state)), false, entity.getLevel().getRandom(), 0, overlay);
            } else {
                mc.getBlockRenderer().getModelRenderer().tesselateWithoutAO(entity.getLevel(), model, state, entity.getBlockPos(), matrices, vertexConsumers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(state)), false, entity.getLevel().getRandom(), 0, overlay);
            }

        } catch (Throwable var17) {
            CrashReport crashReport = CrashReport.forThrowable(var17, "Tesselating block model");
            CrashReportCategory crashReportSection = crashReport.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails(crashReportSection, entity.getLevel(), entity.getBlockPos(), state);
            crashReportSection.setDetail("Using AO", bl);
            throw new ReportedException(crashReport);
        } finally {
            matrices.popPose();
        }
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }
}
