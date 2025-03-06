package team.dovecotmc.metropolis.client.block.entity;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import team.dovecotmc.metropolis.block.BlockSecurityInspectionMachine;
import team.dovecotmc.metropolis.block.entity.BlockEntitySecurityInspectionMachine;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class SecurityInspectionMachineBlockEntityRenderer implements BlockEntityRenderer<BlockEntitySecurityInspectionMachine> {
    @Override
    public void render(BlockEntitySecurityInspectionMachine entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
        matrices.translate(8f, 8f, 8f);
        matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, -entity.getCachedState().get(HorizontalFacingBlock.FACING).asRotation() - 180, 0)));
        matrices.translate(-8f, -8f, -8f);
        matrices.scale(16f, 16f, 16f);

        matrices.push();
        if (!entity.getStack(0).isEmpty()) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);

            matrices.translate(7, 14, -8);

            if (MinecraftClient.getInstance().world != null) {
                matrices.scale(0.75f, 0.75f, 0.75f);
                matrices.translate(
                        0,
                        MathHelper.sin((MinecraftClient.getInstance().world.getTime() + tickDelta) / 10f),
                        40 * Math.min(1, (MinecraftClient.getInstance().world.getTime() - entity.itemAnimationTime + tickDelta) / BlockSecurityInspectionMachine.PROCESS_DURATION)
                );

                matrices.translate(1, 1, 1);
                matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, MinecraftClient.getInstance().world.getTime() * 3, 0)));
                matrices.translate(-1, -1, -1);
            }

            matrices.scale(16f, 16f, 16f);
            itemRenderer.renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        }
        matrices.pop();
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntitySecurityInspectionMachine blockEntity) {
        return true;
//        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return Integer.MAX_VALUE;
//        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntitySecurityInspectionMachine blockEntity, Vec3d pos) {
//        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
        return true;
    }
}
