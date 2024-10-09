package team.dovecotmc.metropolis.client.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.block.entity.BlockEntityPSDSmallDoorSemiAuto;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityRendererPSDSmallDoorSemiAuto implements BlockEntityRenderer<BlockEntityPSDSmallDoorSemiAuto> {
    @Override
    public void render(BlockEntityPSDSmallDoorSemiAuto entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        World world = entity.getWorld();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        matrices.push();
        if (world != null) {
            BlockState block = entity.getCachedState();
            Direction facing = block.get(HorizontalFacingBlock.FACING);

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
            matrices.translate(8f, 8f, 8f);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, -facing.asRotation() - 180, 0)));
            matrices.translate(-8f, -8f, -8f);
            matrices.scale(16f, 16f, 16f);

            double animTime = Math.min(20, (world.getTime() - entity.animationStartTime) + tickDelta);
            System.out.println(entity.animationStartTime);
            matrices.translate(animTime / 15.5f * 16f, 0f, 0f);

            BlockState state = entity.getCachedState();
            BakedModel model = mc.getBlockRenderManager().getModel(state);
            mc.getBlockRenderManager().getModelRenderer().renderSmooth(entity.getWorld(), model, state, entity.getPos(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, entity.getWorld().getRandom(), 0, overlay);
        }
        matrices.pop();
    }
}
