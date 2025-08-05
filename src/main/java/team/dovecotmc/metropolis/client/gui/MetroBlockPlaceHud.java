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
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
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
public class MetroBlockPlaceHud extends GuiComponent {
    public boolean shouldRender = false;
    public PoseStack matricesWorld;
    public VertexConsumer vertexConsumerWorld;
    public BlockPos pos = null;

    public MetroBlockPlaceHud() {
        matricesWorld = null;
        vertexConsumerWorld = null;
    }

    public void render(PoseStack matrices, float tickDelta) {
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

        if (hitResult != null && textRenderer != null && hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            BlockPos pos = new BlockPos(hitResult.getLocation());
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();
            int centerX = width / 2;
            int centerY = height / 2;

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
                textRenderer.drawShadow(
                        matrices,
                        pointedStation,
                        centerX - pointedStationWidth / 2f,
                        y0,
                        0xFFFFFF
                );

                y0 = centerY + 8;

                String[] stationNames = station.name.split("\\|");
                Component stationFirstName = MALocalizationUtil.literalText(stationNames[0]);
                int stationFirstNameWidth = textRenderer.width(stationFirstName);
                textRenderer.drawShadow(
                        matrices,
                        stationFirstName,
                        centerX - stationFirstNameWidth / 2f,
                        y0,
                        0xFFFFFF
                );

                if (stationNames.length > 1) {
                    Component stationSecondName = MALocalizationUtil.literalText(stationNames[1]);
                    int stationSecondNameWidth = textRenderer.width(stationSecondName);
                    y0 += textRenderer.lineHeight + 2;
                    textRenderer.drawShadow(
                            matrices,
                            stationSecondName,
                            centerX - stationSecondNameWidth / 2f,
                            y0,
                            0x545454
                    );
                }
            }

            matrices.popPose();
        } else {
            shouldRender = false;
        }
    }
}
