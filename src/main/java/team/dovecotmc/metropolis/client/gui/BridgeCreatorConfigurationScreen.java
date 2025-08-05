package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);

        this.fillGradient(poseStack, 0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        blit(
                poseStack,
                this.width / 2 - BG_TEXTURE_WIDTH / 2,
                this.height / 2 - BG_TEXTURE_HEIGHT / 2,
                0,
                0,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT
        );

        RenderSystem.setShaderTexture(0, BUTTON_LOCATION);
        blit(
                poseStack,
                this.width / 2 - BG_TEXTURE_WIDTH / 4 - BUTTON_TEXTURE_WIDTH,
                this.height / 2 - BUTTON_TEXTURE_HEIGHT / 2,
                0,
                0,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT
        );

        RenderSystem.setShaderTexture(0, BUTTON_LOCATION);
        blit(
                poseStack,
                this.width / 2 + BG_TEXTURE_WIDTH / 4,
                this.height / 2 - BUTTON_TEXTURE_HEIGHT / 2,
                0,
                0,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT
        );

        this.font.draw(
                poseStack,
                MALocalizationUtil.translatableText("gui.metropolis.bridge_creator.width", 0),
                (float) this.width / 2 - this.font.width(MALocalizationUtil.translatableText("gui.metropolis.bridge_creator.width", bridge_width)) / 2f,
                (float) this.height / 2 - this.font.lineHeight / 2f,
                0x3F3F3F
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
