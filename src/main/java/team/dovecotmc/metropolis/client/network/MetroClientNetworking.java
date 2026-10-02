package team.dovecotmc.metropolis.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import team.dovecotmc.metropolis.network.MetroServerNetworking;

public class MetroClientNetworking {
    public static void c2sTrainStopSignChanges(int index, BlockPos blockPos) {
        FriendlyByteBuf byteBuf = PacketByteBufs.copy(PacketByteBufs.create().writeInt(index).writeLong(blockPos.asLong()));
        ClientPlayNetworking.send(MetroServerNetworking.C2S_TRAIN_STOP_SIGN_CHANGES, byteBuf);
    }

    public static void initialize() {
    }
}
