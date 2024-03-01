package team.dovecotmc.metropolis.metropolis.client.gui;

import net.minecraft.util.Identifier;
import team.dovecotmc.metropolis.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroScreens {
    public static final Identifier ID_SCREEN_OPEN_TICKET_MACHINE = new Identifier(Metropolis.MOD_ID, "open_ticket_machine");

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Client screens.");
    }
}
