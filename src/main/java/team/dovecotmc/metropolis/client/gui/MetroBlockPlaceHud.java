package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.Items;
import mtr.data.Station;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.MetropolisClient;
import team.dovecotmc.metropolis.item.IItemShowStationHUD;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class MetroBlockPlaceHud {
    public boolean shouldRender = false;
    public PoseStack matricesWorld;
    public VertexConsumer vertexConsumerWorld;
    public BlockPos pos = null;

    public MetroBlockPlaceHud() {
        matricesWorld = null;
        vertexConsumerWorld = null;
    }

    public void render(GuiGraphics guiGraphics, float tickDelta) {
        if (!MetropolisClient.config.enableStationInfoOverlay) {
            return;
        }

        Minecraft client = Minecraft.getInstance();

        if (client.player == null || client.level == null || client.getCameraEntity() == null) {
            shouldRender = false;
            return;
        }

        LocalPlayer player = client.player;
        ClientLevel world = client.level;
        HitResult hitResult = client.hitResult;
        Font textRenderer = client.font;

        if (player.isSpectator() || !(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IItemShowStationHUD) && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.BRUSH.get()) {
            shouldRender = false;
            return;
        }

        if (hitResult != null && textRenderer != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();
            int centerX = width / 2;
            int centerY = height / 2;

            PoseStack matrices = guiGraphics.pose();
            matrices.pushPose();

            RenderSystem.assertOnRenderThread();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Station station = MtrStationUtil.getStationByPos(pos, world);
            shouldRender = station != null;

            boolean shouldRenderName = shouldRender;

            if (shouldRenderName) {
                int r = FastColor.ARGB32.red(station.color);
                int g = FastColor.ARGB32.green(station.color);
                int b = FastColor.ARGB32.blue(station.color);
                RenderSystem.setShaderColor(r / 255f, g / 255f, b / 255f, 1);

                int y0 = centerY - 8 - textRenderer.lineHeight;
                Component pointedStation = MALocalizationUtil.translatableText("hud.title.pointed_station");
                int pointedStationWidth = textRenderer.width(pointedStation);
                guiGraphics.drawString(
                        textRenderer,
                        pointedStation,
                        (int) (centerX - pointedStationWidth / 2f),
                        y0,
                        0xFFFFFF,
                        true
                );

                y0 = centerY + 8;

                String[] stationNames = station.name.split("\\|");
                Component stationFirstName = MALocalizationUtil.literalText(stationNames[0]);
                int stationFirstNameWidth = textRenderer.width(stationFirstName);
                guiGraphics.drawString(
                        textRenderer,
                        stationFirstName,
                        (int) (centerX - stationFirstNameWidth / 2f),
                        y0,
                        0xFFFFFF,
                        true
                );

                if (stationNames.length > 1) {
                    Component stationSecondName = MALocalizationUtil.literalText(stationNames[1]);
                    int stationSecondNameWidth = textRenderer.width(stationSecondName);
                    y0 += textRenderer.lineHeight + 2;
                    guiGraphics.drawString(
                            textRenderer,
                            stationSecondName,
                            (int) (centerX - stationSecondNameWidth / 2f),
                            y0,
                            0x545454,
                            true
                    );
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            matrices.popPose();
        } else {
            shouldRender = false;
        }
    }
}