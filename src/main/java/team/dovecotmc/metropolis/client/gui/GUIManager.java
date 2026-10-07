package team.dovecotmc.metropolis.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import team.dovecotmc.metropolis.client.gui.pixel_art.ScreenPixelArt;
import team.dovecotmc.metropolis.client.gui.train_stop_sign.TrainStopSignScreen;

public class GUIManager {
    public static void openPixelArtScreen() {
        Minecraft.getInstance().setScreen(new ScreenPixelArt());
    }

    public static void openTrainStopSignEditScreen(BlockState blockState, int index, BlockPos blockPos) {
        Minecraft.getInstance().setScreen(new TrainStopSignScreen(index, blockPos));
    }
}
