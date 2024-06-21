package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.BlockTicketVendor;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTicketVendor> {
    @Override
    public void render(BlockEntityTicketVendor entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = mc.getBlockRenderManager();

        if (((BlockTicketVendor) entity.getCachedState().getBlock()).isFunctional) {
            BlockState block = entity.getCachedState();
            Direction facing = entity.getCachedState().get(HorizontalFacingBlock.FACING);

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

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            builder.vertex(matrices.peek().getPositionMatrix(), 0f, 3f, 4f).texture(1, 1).next();
            builder.vertex(matrices.peek().getPositionMatrix(), 0f, 15.9343f, 9.3576f).texture(1, 0).next();
            builder.vertex(matrices.peek().getPositionMatrix(), 16f, 15.9343f, 9.3576f).texture(0, 0).next();
            builder.vertex(matrices.peek().getPositionMatrix(), 16f, 3f, 4f).texture(0, 1).next();
//        builder.end();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            tessellator.draw();

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();

            matrices.pop();
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntityTicketVendor blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityTicketVendor blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
