package team.dovecotmc.metropolis.client.block.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
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
        if (!entity.getStack(0).isEmpty()) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
            matrices.translate(0, 12, 0);
            matrices.scale(16f, 16f, 16f);
//            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, 0, 90)));
            itemRenderer.renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        }
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntitySecurityInspectionMachine blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntitySecurityInspectionMachine blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
