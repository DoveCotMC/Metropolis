package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.Items;
import mtr.client.ClientCache;
import mtr.data.Station;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.BlockOutlineDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Matrix4f;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.item.IItemShowStationHUD;
import team.dovecotmc.metropolis.util.MtrStationUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Environment(EnvType.CLIENT)
public class MetroBlockPlaceHud extends DrawableHelper {
    public static final Identifier BACKGROUND_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/blanco.png");
    public boolean shouldRender = false;
    public MatrixStack matricesWorld;
    public VertexConsumer vertexConsumerWorld;

    public MetroBlockPlaceHud() {
        matricesWorld = null;
        vertexConsumerWorld = null;
    }

    public void render(MatrixStack matrices, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.world == null || client.getCameraEntity() == null) {
            shouldRender = false;
            return;
        }

        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        HitResult hitResult = client.crosshairTarget;
        TextRenderer textRenderer = client.textRenderer;

        if (player.isSpectator() || !(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof IItemShowStationHUD) && player.getStackInHand(Hand.MAIN_HAND).getItem() != Items.BRUSH.get()) {
            shouldRender = false;
            return;
        }

        if (hitResult != null && textRenderer != null && hitResult.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
            BlockPos pos = new BlockPos(hitResult.getPos());
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int centerX = width / 2;
            int centerY = height / 2;

            matrices.push();

            RenderSystem.assertOnRenderThread();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Station station = MtrStationUtil.getStationByPos(pos, world);
            shouldRender = station != null;

            if (shouldRender) {
                int r = ColorHelper.Argb.getRed(station.color);
                int g = ColorHelper.Argb.getGreen(station.color);
                int b = ColorHelper.Argb.getBlue(station.color);
                RenderSystem.setShaderColor(r / 255f, g / 255f, b / 255f, 1);

                int y0 = centerY - 8 - textRenderer.fontHeight;
                Text pointedStation = Text.translatable("hud.title.pointed_station");
                int pointedStationWidth = textRenderer.getWidth(pointedStation);
                textRenderer.drawWithShadow(
                        matrices,
                        pointedStation,
                        centerX - pointedStationWidth / 2f,
                        y0,
                        0xFFFFFF
                );

                y0 = centerY + 8;

                String[] stationNames = station.name.split("\\|");
                Text stationFirstName = Text.literal(stationNames[0]);
                int stationFirstNameWidth = textRenderer.getWidth(stationFirstName);
                textRenderer.drawWithShadow(
                        matrices,
                        stationFirstName,
                        centerX - stationFirstNameWidth / 2f,
                        y0,
                        0xFFFFFF
                );

                if (stationNames.length > 1) {
                    Text stationSecondName = Text.literal(stationNames[1]);
                    int stationSecondNameWidth = textRenderer.getWidth(stationSecondName);
                    y0 += textRenderer.fontHeight + 2;
                    textRenderer.drawWithShadow(
                            matrices,
                            stationSecondName,
                            centerX - stationSecondNameWidth / 2f,
                            y0,
                            0x545454
                    );
                }

                if (matricesWorld != null && vertexConsumerWorld != null) {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();

//                    RenderSystem.

                    matricesWorld.push();

//                    Tessellator tessellator = Tessellator.getInstance();
//                    BufferBuilder builder = tessellator.getBuffer();
//                    builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//
//                    builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//                    builder.vertex(matricesWorld.peek().getPositionMatrix(), x, y, z).color(1f, 0f, 0f, 0.5f).next();
//                    builder.vertex(matricesWorld.peek().getPositionMatrix(), x + 1, y, z).color(1f, 0f, 0f, 0.5f).next();
//                    builder.vertex(matricesWorld.peek().getPositionMatrix(), x + 1, y + 1, z).color(1f, 0f, 0f, 0.5f).next();
//                    builder.vertex(matricesWorld.peek().getPositionMatrix(), x, y + 1, z).color(1f, 0f, 0f, 0.5f).next();
//
//                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
////                    RenderSystem.setShaderColor(lightFactor, lightFactor, lightFactor, 1f);
//
//                    builder.end();
//                    tessellator.draw();

                    matricesWorld.pop();
                }
            }

            matrices.pop();
        } else {
            shouldRender = false;
        }
    }

    private void fillRectSimple(MatrixStack matrices, int x, int y, int w, int h, int color) {
        fillGradientSimple(
                matrices,
                x,
                y,
                w,
                h,
                color,
                color
        );
    }

    private void fillGradientSimple(MatrixStack matrices, int x, int y, int w, int h, int colorStart, int colorEnd) {
        fillGradient(
                matrices,
                x,
                y,
                x + w,
                y + h,
                colorStart,
                colorEnd
        );
    }

//    private static void fillRect(MatrixStack matrices, int x, int y, int w, int h, int color) {
//        fillGradient(
//                matrices,
//                centerX + 16,
//                centerY - textRenderer.fontHeight / 2,
//                centerX + stationFirstNameWidth + 4,
//                centerY + textRenderer.fontHeight + 4,
//                color,
//                color
//        );
//    }
}
