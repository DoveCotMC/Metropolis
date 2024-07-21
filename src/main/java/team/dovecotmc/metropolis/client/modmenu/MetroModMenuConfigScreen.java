package team.dovecotmc.metropolis.client.modmenu;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.gui.screen.RealmsSettingsScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import team.dovecotmc.metropolis.Metropolis;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroModMenuConfigScreen extends Screen {
    private final Screen parent;

    public MetroModMenuConfigScreen(Screen parent) {
        super(Text.translatable("metropolis.modmenu.config.title"));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

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

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
