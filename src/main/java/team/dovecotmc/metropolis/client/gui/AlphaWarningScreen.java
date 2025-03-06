package team.dovecotmc.metropolis.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Environment(EnvType.CLIENT)
public class AlphaWarningScreen extends Screen {
    protected AlphaWarningScreen(Text title) {
        super(title);
    }
}
