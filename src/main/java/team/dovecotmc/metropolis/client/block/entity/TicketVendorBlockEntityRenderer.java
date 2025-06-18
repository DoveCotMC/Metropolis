package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.BlockTicketVendor;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.metropolis.client.MetropolisClient;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTicketVendor> {
    @Override
    public void render(BlockEntityTicketVendor entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();

        if (((BlockTicketVendor) entity.getBlockState().getBlock()).isFunctional) {
            BlockState block = entity.getBlockState();
            Direction facing = entity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            if (MetropolisClient.config.enableGlowingTexture) {
                matrices.pushPose();

                RenderSystem.assertOnRenderThread();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderTexture(0, new ResourceLocation(Metropolis.MOD_ID, "textures/block/" + Registry.BLOCK.getKey(block.getBlock()).getPath() + "_monitor.png"));

                matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                matrices.translate(8f, 8f, 8f);
                matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -facing.toYRot() - 180, 0)));
                matrices.translate(-8f, -8f, -8f);

                matrices.translate(0f, 0f, -0.1f);

                float lightFactor = Math.min(Math.max((Math.max(LightTexture.sky(light), LightTexture.block(light))) / 15f, 7f / 15f), 13f / 15f);
//            float lightFactor = Math.max(1f / 15f, Math.max(LightmapTextureManager.getSkyLightCoordinates(light), LightmapTextureManager.getBlockLightCoordinates(light)) / 15f);
                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder builder = tessellator.getBuilder();
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(matrices.last().pose(), 0f, 3f, 4f).uv(1, 1).endVertex();
                builder.vertex(matrices.last().pose(), 0f, 15.9343f, 9.3576f).uv(1, 0).endVertex();
                builder.vertex(matrices.last().pose(), 16f, 15.9343f, 9.3576f).uv(0, 0).endVertex();
                builder.vertex(matrices.last().pose(), 16f, 3f, 4f).uv(0, 1).endVertex();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(lightFactor, lightFactor, lightFactor, 1f);

                tessellator.end();

                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();

                matrices.popPose();
            }

            matrices.pushPose();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -facing.toYRot() - 180, 0)));
            matrices.translate(-0.5f, -0.5f, -0.5f);
            if (!entity.getItem(0).isEmpty()) {
                matrices.pushPose();

                double ticketOffset = 0;
                if (mc.level != null) {
                    double time = (double) (mc.level.getGameTime() - entity.ticket_animation_begin_time) + tickDelta;
                    if (time < 10) {
                        ticketOffset = 1 - Math.pow(time / 10d, 2);
                    }
                    matrices.translate(13.25d / 16d, 2.25d / 16d, (6 + ticketOffset * 3) / 16d);
                    matrices.translate(-0.5 / 16f, 0, 0);
                    matrices.scale(0.33f, 0.33f, 0.33f);
                    matrices.translate(0.5 / 16f, 0, 0);
                    matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(-90, 0, 90)));
                    mc.getItemRenderer().renderStatic(entity.getItem(0), ItemTransforms.TransformType.GROUND, light, overlay, matrices, vertexConsumers, 0);
                    matrices.popPose();
                }
            }
            matrices.popPose();
        }
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }
}
