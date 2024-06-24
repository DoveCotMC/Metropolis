package team.dovecotmc.metropolis.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import team.dovecotmc.metropolis.client.gui.TicketVendorScreen1;
import team.dovecotmc.metropolis.network.MetroServerNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroClientNetwork {
    public static void registerTicketVendorGuiReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.TICKET_VENDOR_GUI, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack itemStack = buf.readItemStack();
            client.execute(() -> {
                client.setScreen(new TicketVendorScreen1(pos, itemStack));
            });
        });
    }
}
