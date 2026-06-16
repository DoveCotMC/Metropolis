package team.dovecotmc.metropolis.client.gui;

import net.minecraft.client.Minecraft;
import team.dovecotmc.metropolis.client.gui.pixel_art.ScreenPixelArt;

public class GUIManager {
    public static void openPixelArtScreen() {
        Minecraft.getInstance().setScreen(new ScreenPixelArt());
    }
}
