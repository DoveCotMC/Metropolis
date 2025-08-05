package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import team.dovecotmc.metropolis.block.BlockSecurityInspectionMachine;
import team.dovecotmc.metropolis.block.entity.BlockEntitySecurityInspectionMachine;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class SecurityInspectionMachineBlockEntityRenderer implements BlockEntityRenderer<BlockEntitySecurityInspectionMachine> {
    @Override
    public void render(BlockEntitySecurityInspectionMachine entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();

        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
        matrices.translate(8f, 8f, 8f);
        matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -entity.getBlockState().getValue(HorizontalDirectionalBlock.FACING).toYRot() - 180, 0)));
        matrices.translate(-8f, -8f, -8f);
        matrices.scale(16f, 16f, 16f);

        matrices.pushPose();
        if (!entity.getItem(0).isEmpty()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);

            matrices.translate(7, 14, -8);

            if (Minecraft.getInstance().level != null) {
                matrices.scale(0.75f, 0.75f, 0.75f);
                matrices.translate(
                        0,
                        Mth.sin((Minecraft.getInstance().level.getGameTime() + tickDelta) / 10f),
                        40 * Math.min(1, (Minecraft.getInstance().level.getGameTime() - entity.itemAnimationTime + tickDelta) / BlockSecurityInspectionMachine.PROCESS_DURATION)
                );

                matrices.translate(1, 1, 1);
                matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, Minecraft.getInstance().level.getGameTime() * 3, 0)));
                matrices.translate(-1, -1, -1);
            }

            matrices.scale(16f, 16f, 16f);
            itemRenderer.renderStatic(entity.getItem(0), ItemTransforms.TransformType.GROUND, light, overlay, matrices, vertexConsumers, 0);
        }
        matrices.popPose();
        matrices.popPose();
    }

    @Override
    public int getViewDistance() {
        return Integer.MAX_VALUE;
//        return BlockEntityRenderer.super.getRenderDistance();
    }
}
