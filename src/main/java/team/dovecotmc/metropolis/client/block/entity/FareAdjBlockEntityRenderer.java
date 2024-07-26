package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.BlockFareAdjMachine;
import team.dovecotmc.metropolis.block.BlockTicketVendor;
import team.dovecotmc.metropolis.block.entity.BlockEntityFareAdj;
import team.dovecotmc.metropolis.client.MetropolisClient;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class FareAdjBlockEntityRenderer implements BlockEntityRenderer<BlockEntityFareAdj> {
    @Override
    public void render(BlockEntityFareAdj entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();

        BlockState block = entity.getCachedState();
        Direction facing = entity.getCachedState().get(HorizontalFacingBlock.FACING);

        if (MetropolisClient.config.enableGlowingTexture) {
            matrices.push();

            RenderSystem.assertOnRenderThread();
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderTexture(0, new Identifier(Metropolis.MOD_ID, "textures/block/" + Registry.BLOCK.getId(block.getBlock()).getPath() + "_monitor.png"));

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
            matrices.translate(8f, 8f, 8f);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, -facing.asRotation() - 180, 0)));
            matrices.translate(-8f, -8f, -8f);

            matrices.translate(0f, 0f, -0.1f);

            float lightFactor = Math.min(Math.max((Math.max(LightmapTextureManager.getSkyLightCoordinates(light), LightmapTextureManager.getBlockLightCoordinates(light))) / 15f, 7f / 15f), 13f / 15f);
//            float lightFactor = Math.max(1f / 15f, Math.max(LightmapTextureManager.getSkyLightCoordinates(light), LightmapTextureManager.getBlockLightCoordinates(light)) / 15f);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            builder.vertex(matrices.peek().getPositionMatrix(), 0f, 3f, 4f).texture(1, 1).next();
            builder.vertex(matrices.peek().getPositionMatrix(), 0f, 15.9343f, 9.3576f).texture(1, 0).next();
            builder.vertex(matrices.peek().getPositionMatrix(), 16f, 15.9343f, 9.3576f).texture(0, 0).next();
            builder.vertex(matrices.peek().getPositionMatrix(), 16f, 3f, 4f).texture(0, 1).next();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(lightFactor, lightFactor, lightFactor, 1f);

            tessellator.draw();

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();

            matrices.pop();
        }

//            matrices.push();
//            matrices.translate(0.5f, 0.5f, 0.5f);
//            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, -facing.asRotation() - 180, 0)));
//            matrices.translate(-0.5f, -0.5f, -0.5f);
//            if (!entity.getStack(0).isEmpty()) {
//                matrices.push();
//
//                double ticketOffset = 0;
//                if (mc.world != null) {
//                    double time = (double) (mc.world.getTime() - entity.ticket_animation_begin_time) + tickDelta;
//                    if (time < 10) {
//                        ticketOffset = 1 - Math.pow(time / 10d, 2);
//                    }
//                    matrices.translate(13.25d / 16d, 2.25d / 16d, (6 + ticketOffset * 3) / 16d);
//                    matrices.translate(-0.5 / 16f, 0, 0);
//                    matrices.scale(0.33f, 0.33f, 0.33f);
//                    matrices.translate(0.5 / 16f, 0, 0);
//                    matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(-90, 0, 90)));
//                    mc.getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
//                    matrices.pop();
//                }
//            }
//            matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntityFareAdj blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityFareAdj blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
