package team.dovecotmc.metropolis.metropolis.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroServerNetwork {
    public static final Identifier TICKET_VENDOR_GUI = new Identifier(Metropolis.MOD_ID, "ticket_vendor_gui");
    public static void openTicketVendorScreen(World world, BlockPos pos, ServerPlayerEntity player) {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeItemStack(ItemStack.EMPTY);
        ServerPlayNetworking.send(player, TICKET_VENDOR_GUI, packet);
    }
}
