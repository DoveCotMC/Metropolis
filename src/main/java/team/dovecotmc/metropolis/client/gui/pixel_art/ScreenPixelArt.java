package team.dovecotmc.metropolis.client.gui.pixel_art;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import team.dovecotmc.metropolis.client.gui.base.BaseMetropolisScreen;
import team.dovecotmc.metropolis.client.gui.base.components.TextButton;

public class ScreenPixelArt extends BaseMetropolisScreen {
    private final TextButton button;
    private int x = 0;

    public ScreenPixelArt() {
        super("pixel_art");
        this.button = new TextButton(this, () -> {
            x -= 64;
        });
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        renderBackground(guiGraphics);

        button.setText(Component.literal("Metropolis test"), 8, 0xFFFFFFFF);
        button.setPosition(getRoot().getWidth() - 64f + x - button.getWidth(), 64f);

        super.render(guiGraphics, mouseX, mouseY, tickDelta);
    }
}
