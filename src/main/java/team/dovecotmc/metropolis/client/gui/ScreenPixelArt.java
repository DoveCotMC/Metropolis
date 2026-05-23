package team.dovecotmc.metropolis.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenPixelArt extends Screen {
    public ScreenPixelArt() {
        super(Component.translatable("gui.metropolis.pixel_art.name"));
    }

    @Override
    protected void init() {
        super.init();

//        addRenderableWidget(Button)
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, i, j, f);
    }
}
