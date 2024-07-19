package team.dovecotmc.metropolis.client.network;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.Text;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class PacketGetCurrencyItem implements Packet<ClientPlayPacketListener> {
    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {
    }
}
