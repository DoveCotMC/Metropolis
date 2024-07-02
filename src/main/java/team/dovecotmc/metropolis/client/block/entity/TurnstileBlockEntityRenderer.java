package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.Station;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TurnstileBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTurnstile> {
    public TurnstileBlockEntityRenderer() {
    }

    @Override
    public void render(BlockEntityTurnstile entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
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

            if (mc.player != null) {
                ClientPlayerEntity player = mc.player;
                if (player.getStackInHand(Hand.MAIN_HAND).getItem() == mtr.Items.BRUSH.get()) {
                    Station station = MtrStationUtil.getStationByPos(entity.getPos(), world);
                    RenderSystem.setShaderColor(ColorHelper.Argb.getRed(station.color) / 255f, ColorHelper.Argb.getGreen(station.color) / 255f, ColorHelper.Argb.getBlue(station.color) / 255f, 1f);
                }
            }

            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.enableTexture();
            mc.getBlockRenderManager().getModelRenderer().render(
                    world,
                    mc.getBlockRenderManager().getModel(block),
                    block,
                    entity.getPos(),
                    matrices,
                    vertexConsumers.getBuffer(RenderLayers.getBlockLayer(block)),
                    false,
                    entity.getWorld().getRandom(),
                    0,
                    overlay
            );
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            if (entity.type == BlockEntityTurnstile.EnumTurnstileType.ENTER) {
                if (!entity.getStack(0).isEmpty()) {

                    float animTime = (float) (world.getTime() - entity.ticketAnimationStartTime) + tickDelta;

                    if (animTime < 3) {
                        matrices.push();
                        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                        matrices.translate(2, 15.75, 0);
                        matrices.scale(16f, 16f, 16f);

                        matrices.scale(0.33f, 0.33f, 0.33f);
                        double var0 = Math.sqrt((0.3 / 16 * (Math.min(animTime, 3) / 3f)) * 2);
                        matrices.translate(0, -var0, var0);
                        matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(-45, 0, 90)));

                        itemRenderer.renderItem(
                                entity.getStack(0),
                                ModelTransformation.Mode.GROUND,
                                light,
                                overlay,
                                matrices,
                                vertexConsumers,
                                0
                        );
                        matrices.pop();
                    } else if (animTime > 7) {
                        animTime -= 7;
                        matrices.push();
                        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                        matrices.translate(2.125, 16.5, 12);
                        matrices.scale(16f, 16f, 16f);

                        matrices.scale(0.33f, 0.33f, 0.33f);
                        matrices.translate(0, -1d / 16d, 0);
                        matrices.translate(0, Math.sqrt((0.2 / 16) * Math.min(animTime, 5) / 5f), 0);
                        matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, 0, 90)));

                        itemRenderer.renderItem(
                                entity.getStack(0),
                                ModelTransformation.Mode.GROUND,
                                light,
                                overlay,
                                matrices,
                                vertexConsumers,
                                0
                        );
                        matrices.pop();
                    }
                }
            } else if (entity.type == BlockEntityTurnstile.EnumTurnstileType.EXIT){
                float animTime = (float) (world.getTime() - entity.ticketAnimationStartTime) + tickDelta;
                if (animTime < 3) {
                    matrices.push();
                    matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                    matrices.translate(2, 15.75, 0);
                    matrices.scale(16f, 16f, 16f);

                    matrices.scale(0.33f, 0.33f, 0.33f);
                    double var0 = Math.sqrt((0.3 / 16 * (Math.min(animTime, 3) / 3f)) * 2);
                    matrices.translate(0, -var0, var0);
                    matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(-45, 0, 90)));

                    itemRenderer.renderItem(
                            entity.getStack(0),
                            ModelTransformation.Mode.GROUND,
                            light,
                            overlay,
                            matrices,
                            vertexConsumers,
                            0
                    );
                    matrices.pop();
                }
            }
        }
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntityTurnstile blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityTurnstile blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
