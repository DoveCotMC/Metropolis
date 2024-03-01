package team.dovecotmc.metropolis.metropolis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import team.dovecotmc.metropolis.metropolis.client.gui.MetroScreens;
import team.dovecotmc.metropolis.metropolis.client.gui.TicketMachineScreen;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetropolisClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MetroScreens.ID_SCREEN_OPEN_TICKET_MACHINE, (client, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
            client.execute(() -> {
                client.setScreen(new TicketMachineScreen(stack));
            });
        });
    }
}
