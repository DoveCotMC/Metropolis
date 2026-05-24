package team.dovecotmc.metropolis.client.gui.pixel_art;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import team.dovecotmc.metropolis.client.gui.base.BaseMetropolisScreen;
import team.dovecotmc.metropolis.client.gui.base.components.Button;

public class ScreenPixelArt extends BaseMetropolisScreen {
    private final Button button;

    public ScreenPixelArt() {
        super("pixel_art");
        this.button = new Button(this);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        renderBackground(guiGraphics);

        button.setBackgroundColor(0xFFFF0000);
        button.setText(Component.literal("Metropolis test"), 16, 0xFFFFFFFF);
        button.setPosition(getRoot().getWidth() - 64f - button.getWidth(), 64f);

        super.render(guiGraphics, mouseX, mouseY, tickDelta);
    }
}
