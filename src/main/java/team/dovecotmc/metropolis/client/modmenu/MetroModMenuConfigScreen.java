package team.dovecotmc.metropolis.client.modmenu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.client.MetropolisClient;
import team.dovecotmc.metropolis.config.MetroClientConfig;
import team.dovecotmc.metropolis.config.MetroConfig;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroModMenuConfigScreen extends Screen {
    private final Screen parent;
    public static final Identifier SWITCH_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/config/switch.png");
    public static final int SWITCH_TEXTURE_WIDTH = 32;
    public static final int SWITCH_TEXTURE_HEIGHT = 16;

    public MetroModMenuConfigScreen(Screen parent) {
        super(Text.translatable("metropolis.modmenu.config.title"));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        // Mod container
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(Metropolis.MOD_ID);

        if (modContainer.isEmpty()) {
            return;
        }

        ModContainer mod = modContainer.get();

        matrices.push();
        matrices.scale(2f, 2f, 2f);
        Text name = Text.literal(mod.getMetadata().getName());
        textRenderer.drawWithShadow(
                matrices,
                name,
                (width / 2f - textRenderer.getWidth(name) * 2f / 2f) / 2f, 16 / 2f,
                0xFFFFFF
        );
        matrices.pop();

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();

        matrices.push();
        Text text = Text.translatable("config.metropolis.client.enable_glowing_texture");
        textRenderer.draw(
                matrices,
                text,
                width / 2f - 32 - textRenderer.getWidth(text),
                64,
                0xFFFFFF
        );

        RenderSystem.setShaderTexture(0, SWITCH_TEXTURE_ID);
        drawTexture(
                matrices,
                0,
                0,
                SWITCH_TEXTURE_WIDTH,
                SWITCH_TEXTURE_HEIGHT,
                SWITCH_TEXTURE_WIDTH,
                SWITCH_TEXTURE_HEIGHT
        );

        text = Text.translatable("config.metropolis.client.enable_station_info_overlay");
        textRenderer.draw(
                matrices,
                text,
                width / 2f - 32 - textRenderer.getWidth(text),
                64 + (16 + textRenderer.fontHeight),
                0xFFFFFF
        );
        matrices.pop();
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
            MetroConfig.save(Metropolis.config);
            MetroClientConfig.save(MetropolisClient.config);
        }
    }
}
