package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

public class BridgeCreatorConfigurationScreen extends Screen {
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/bridge_creator/background.png");
    protected static final int BG_TEXTURE_WIDTH = 176;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation BUTTON_LOCATION = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/bridge_creator/button.png");
    protected static final int BUTTON_TEXTURE_WIDTH = 24;
    protected static final int BUTTON_TEXTURE_HEIGHT = 24;

    protected int bridge_width;

    public BridgeCreatorConfigurationScreen(int initialWidth) {
        super(Component.translatable("gui.metropolis.bridge_creator.title"));

        this.bridge_width = initialWidth;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.blit(
                BACKGROUND_LOCATION,
                this.width / 2 - BG_TEXTURE_WIDTH / 2,
                this.height / 2 - BG_TEXTURE_HEIGHT / 2,
                0,
                0,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT
        );

        guiGraphics.blit(
                BUTTON_LOCATION,
                this.width / 2 - BG_TEXTURE_WIDTH / 4 - BUTTON_TEXTURE_WIDTH,
                this.height / 2 - BUTTON_TEXTURE_HEIGHT / 2,
                0,
                0,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT
        );

        guiGraphics.blit(
                BUTTON_LOCATION,
                this.width / 2 + BG_TEXTURE_WIDTH / 4,
                this.height / 2 - BUTTON_TEXTURE_HEIGHT / 2,
                0,
                0,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT
        );

        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.bridge_creator.width", bridge_width),
                this.width / 2 - this.font.width(MALocalizationUtil.translatableText("gui.metropolis.bridge_creator.width", bridge_width)) / 2,
                this.height / 2 - this.font.lineHeight / 2,
                0x3F3F3F,
                false
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}