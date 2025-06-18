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
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.block.BlockTurnstile;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.client.MetropolisClient;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TurnstileBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTurnstile> {
    @Override
    public void render(BlockEntityTurnstile entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        Level world = entity.getLevel();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        matrices.pushPose();
        if (world != null) {
            BlockState block = entity.getBlockState();
            Direction facing = block.getValue(HorizontalDirectionalBlock.FACING);

            if (mc.player != null && mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == mtr.Items.BRUSH.get()) {
                matrices.pushPose();

                Component text = MALocalizationUtil.translatableText("misc.metropolis.turnstile_mode." + BlockEntityTurnstile.EnumTurnstileType.get(block.getValue(BlockTurnstile.TYPE)).name().toLowerCase());

                float textScale = (float) (mc.font.lineHeight) / (float) mc.font.width(text);
                matrices.translate(0, 1.25, 0);
                matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                matrices.translate(8, 8, 8);
                matrices.scale(textScale, textScale, textScale);
                matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -mc.player.getRotationVector().y, 0)));
                matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(mc.player.getRotationVector().x, 0, 0)));
                matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, 0, 180)));
                matrices.translate(-mc.font.width(text) / 2f, 0, 0);

                mc.font.draw(
                        matrices,
                        text,
                        0,
                        0,
                        0xFFFFFF
                );
                matrices.popPose();
            }

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
            matrices.translate(8f, 8f, 8f);
            matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -facing.toYRot() - 180, 0)));
            matrices.translate(-8f, -8f, -8f);
            matrices.scale(16f, 16f, 16f);

            if (MetropolisClient.config.enableGlowingTexture) {
                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();
                RenderSystem.defaultBlendFunc();

                float lightFactor = Math.min(Math.max((Math.max(LightTexture.sky(light), LightTexture.block(light))) / 15f, 7f / 15f), 13f / 15f);
                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder builder = tessellator.getBuilder();
                float d = 4f / 16f;

                // Forwards
                matrices.pushPose();
                matrices.translate(0, 0, -d - 0.1f / 16f);

                RenderSystem.setShaderTexture(0, new ResourceLocation(Metropolis.MOD_ID, "textures/block/turnstile_light_entry.png"));
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(matrices.last().pose(), 0, 1, 0).uv(1, 0).endVertex();
                builder.vertex(matrices.last().pose(), 1, 1, 0).uv(0, 0).endVertex();
                builder.vertex(matrices.last().pose(), 1, 0, 0).uv(0, 1).endVertex();
                builder.vertex(matrices.last().pose(), 0, 0, 0).uv(1, 1).endVertex();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(lightFactor, lightFactor, lightFactor, 1f);

                tessellator.end();
                matrices.popPose();

                // Backwards
                matrices.pushPose();
                matrices.translate(0.5f, 0.5f, 0.5f);
                matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, 180, 0)));
                matrices.translate(-0.5f, -0.5f, -0.5f);
                matrices.translate(0, 0, -d - 0.1f / 16f);

                RenderSystem.setShaderTexture(0, new ResourceLocation(Metropolis.MOD_ID, "textures/block/turnstile_light_exit.png"));
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(matrices.last().pose(), 0, 1, 0).uv(1, 0).endVertex();
                builder.vertex(matrices.last().pose(), 1, 1, 0).uv(0, 0).endVertex();
                builder.vertex(matrices.last().pose(), 1, 0, 0).uv(0, 1).endVertex();
                builder.vertex(matrices.last().pose(), 0, 0, 0).uv(1, 1).endVertex();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(lightFactor, lightFactor, lightFactor, 1f);

                tessellator.end();
                matrices.popPose();

                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
            }

            BlockEntityTurnstile.EnumTurnstileType type = BlockEntityTurnstile.EnumTurnstileType.get(block.getValue(BlockTurnstile.TYPE));
            if (type == BlockEntityTurnstile.EnumTurnstileType.ENTER) {
                if (!entity.getItem(0).isEmpty()) {

                    float animTime = (float) (world.getGameTime() - entity.ticketAnimationStartTime) + tickDelta;

                    if (animTime < 3) {
                        matrices.pushPose();
                        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                        matrices.translate(2, 15.75, 0);
                        matrices.scale(16f, 16f, 16f);

                        matrices.scale(0.33f, 0.33f, 0.33f);
                        double var0 = Math.sqrt((0.3 / 16 * (Math.min(animTime, 3) / 3f)) * 2);
                        matrices.translate(0, -var0, var0);
                        matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(-45, 0, 90)));

                        itemRenderer.renderStatic(
                                entity.getItem(0),
                                ItemTransforms.TransformType.GROUND,
                                light,
                                overlay,
                                matrices,
                                vertexConsumers,
                                0
                        );
                        matrices.popPose();
                    } else if (animTime > 7) {
                        animTime -= 7;
                        matrices.pushPose();
                        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                        matrices.translate(2.125, 16.5, 12);
                        matrices.scale(16f, 16f, 16f);

                        matrices.scale(0.33f, 0.33f, 0.33f);
                        matrices.translate(0, -1d / 16d, 0);
                        matrices.translate(0, Math.sqrt((0.2 / 16) * Math.min(animTime, 5) / 5f), 0);
                        matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(15, 0, 90)));

                        itemRenderer.renderStatic(
                                entity.getItem(0),
                                ItemTransforms.TransformType.GROUND,
                                light,
                                overlay,
                                matrices,
                                vertexConsumers,
                                0
                        );
                        matrices.popPose();
                    }
                }

//                // TODO: Render balance
//                ClientPlayerEntity player = MinecraftClient.getInstance().player;
//                if (player != null) {
//                    ItemStack heldStack = player.getStackInHand(Hand.MAIN_HAND);
//                    if (heldStack.getItem() instanceof ItemTicket || heldStack.getItem() instanceof ItemCard) {
//                        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//                        HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
//                        if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
//                            System.out.println(hitResult.getPos().equals(entity.getPos()));
//                        }
////                        if (MinecraftClient.getInstance().crosshairTarget.getType().equals(HitResult.Type.BLOCK) && MinecraftClient.getInstance().crosshairTarget.getPos())
//                    }
//                }
            } else if (type == BlockEntityTurnstile.EnumTurnstileType.EXIT) {
                float animTime = (float) (world.getGameTime() - entity.ticketAnimationStartTime) + tickDelta;
                if (animTime < 3) {
                    matrices.pushPose();
                    matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
                    matrices.translate(2, 15.75, 0);
                    matrices.scale(16f, 16f, 16f);

                    matrices.scale(0.33f, 0.33f, 0.33f);
                    double var0 = Math.sqrt((0.3 / 16 * (Math.min(animTime, 3) / 3f)) * 2);
                    matrices.translate(0, -var0, var0);
                    matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(-45, 0, 90)));

                    itemRenderer.renderStatic(
                            entity.getItem(0),
                            ItemTransforms.TransformType.GROUND,
                            light,
                            overlay,
                            matrices,
                            vertexConsumers,
                            0
                    );
                    matrices.popPose();
                }
            }
        }
        matrices.popPose();
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }
}
