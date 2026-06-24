package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import team.dovecotmc.metropolis.block.BlockTrainStopSign;
import team.dovecotmc.metropolis.block.entity.BlockEntityTrainStopSign;

public class TrainStopSignBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTrainStopSign> {
    @Override
    public void render(BlockEntityTrainStopSign blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        int length = blockEntity.length;
        Font font = Minecraft.getInstance().font;

        if (length == 0)
            return;

        float scaleFactor = length >= 10 ? 1f / 48f : 1f / 32f;
        poseStack.pushPose();
        poseStack.mulPoseMatrix(new Matrix4f()
                        .translate(0.5f, 0.5f, 0.5f)
                        .rotateY((180 - blockEntity.getBlockState().getValue(BlockTrainStopSign.FACING).toYRot()) * Mth.DEG_TO_RAD)
                        .translate(font.width(String.valueOf(length)) / -2f / 16f, 4f / 16f, -0.25f)
                        .scale(scaleFactor, scaleFactor, scaleFactor)
//                .translate(1, 1, 0)
//                        .translate(0.5f, 0.5f - 7f / 16f, 0)
                        );
        font.drawInBatch(String.valueOf(length), 0, 0, 0xFF000000, false, poseStack.last().pose(), multiBufferSource, Font.DisplayMode.NORMAL, 0x00000000, i);
        poseStack.popPose();

    }
}
