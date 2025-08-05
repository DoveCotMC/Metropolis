package team.dovecotmc.metropolis.client.modmenu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.MetropolisClient;
import team.dovecotmc.metropolis.client.config.MetroClientConfig;
import team.dovecotmc.metropolis.config.MetroConfig;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroModMenuConfigScreen extends Screen {
    private final Screen parent;
    public static final ResourceLocation SWITCH_ON_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/config/switch_on.png");
    public static final ResourceLocation SWITCH_ON_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/config/switch_on_hover.png");
    public static final ResourceLocation SWITCH_OFF_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/config/switch_off.png");
    public static final ResourceLocation SWITCH_OFF_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/config/switch_off_hover.png");
    public static final int SWITCH_TEXTURE_WIDTH = 32;
    public static final int SWITCH_TEXTURE_HEIGHT = 16;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = false;
    private boolean lastPressing = false;
    protected boolean pressed = false;

    public MetroModMenuConfigScreen(Screen parent) {
        super(MALocalizationUtil.translatableText("metropolis.modmenu.config.title"));
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
//        this.renderBackground(matrices);
        this.renderDirtBackground(0);

        super.render(matrices, mouseX, mouseY, delta);

        // Mod container
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(Metropolis.MOD_ID);

        if (modContainer.isEmpty()) {
            return;
        }

        ModContainer mod = modContainer.get();
        int button_offset = (SWITCH_TEXTURE_HEIGHT - font.lineHeight) / 2;

        matrices.pushPose();
        matrices.scale(2f, 2f, 2f);
        Component name = MALocalizationUtil.literalText(mod.getMetadata().getName());
        font.drawShadow(
                matrices,
                name,
                (width / 2f - font.width(name) * 2f / 2f) / 2f, 16 / 2f,
                0xFFFFFF
        );
        matrices.popPose();

        // Enable glowing texture
        matrices.pushPose();
        Component text = MALocalizationUtil.translatableText("config.metropolis.client.enable_glowing_texture");
        font.draw(
                matrices,
                text,
                width / 2f - 32 - font.width(text),
                64,
                0xFFFFFF
        );

        RenderSystem.enableTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x0 = width / 2 + 32;
        int y0 = 64 - button_offset;
        boolean isGlowingTextureHovered = mouseX >= x0 && mouseY >= y0 && mouseX <= x0 + SWITCH_TEXTURE_WIDTH && mouseY <= y0 + SWITCH_TEXTURE_HEIGHT;
        if (MetropolisClient.config.enableGlowingTexture) {
            RenderSystem.setShaderTexture(0, isGlowingTextureHovered ? SWITCH_ON_HOVER_TEXTURE_ID : SWITCH_ON_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, isGlowingTextureHovered ? SWITCH_OFF_HOVER_TEXTURE_ID : SWITCH_OFF_TEXTURE_ID);
        }

        if (isGlowingTextureHovered && pressed) {
            MetropolisClient.config.enableGlowingTexture = !MetropolisClient.config.enableGlowingTexture;
            MetroClientConfig.save(MetropolisClient.config);
            playDownSound();
        }

        blit(
                matrices,
                x0,
                y0,
                0,
                0,
                SWITCH_TEXTURE_WIDTH,
                SWITCH_TEXTURE_HEIGHT,
                SWITCH_TEXTURE_WIDTH,
                SWITCH_TEXTURE_HEIGHT
        );

        // Enable station info overlay
        text = MALocalizationUtil.translatableText("config.metropolis.client.enable_station_info_overlay");
        font.draw(
                matrices,
                text,
                width / 2f - 32 - font.width(text),
                64 + (16 + font.lineHeight),
                0xFFFFFF
        );

        RenderSystem.enableTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int y1 = 64 + (16 + font.lineHeight) - button_offset;
        boolean isEnableStationInfoDisplayHovered = mouseX >= x0 && mouseY >= y1 && mouseX <= x0 + SWITCH_TEXTURE_WIDTH && mouseY <= y1 + SWITCH_TEXTURE_HEIGHT;
        if (MetropolisClient.config.enableStationInfoOverlay) {
            RenderSystem.setShaderTexture(0, isEnableStationInfoDisplayHovered ? SWITCH_ON_HOVER_TEXTURE_ID : SWITCH_ON_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, isEnableStationInfoDisplayHovered ? SWITCH_OFF_HOVER_TEXTURE_ID : SWITCH_OFF_TEXTURE_ID);
        }

        if (isEnableStationInfoDisplayHovered && pressed) {
            MetropolisClient.config.enableStationInfoOverlay = !MetropolisClient.config.enableStationInfoOverlay;
            MetroClientConfig.save(MetropolisClient.config);
            playDownSound();
        }

        blit(
                matrices,
                x0,
                y1,
                0,
                0,
                SWITCH_TEXTURE_WIDTH,
                SWITCH_TEXTURE_HEIGHT,
                SWITCH_TEXTURE_WIDTH,
                SWITCH_TEXTURE_HEIGHT
        );
        matrices.popPose();

        if (pressing) {
            pressed = !lastPressing;
        } else {
            pressed = false;
        }
        lastPressing = pressing;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
            MetroConfig.save(Metropolis.config);
            MetroClientConfig.save(MetropolisClient.config);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.pressing = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.pressing = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void playDownSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
