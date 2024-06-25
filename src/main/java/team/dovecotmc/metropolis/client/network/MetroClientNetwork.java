package team.dovecotmc.metropolis.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.metropolis.client.gui.ticket_vendor.TicketVendorScreen1;
import team.dovecotmc.metropolis.network.MetroServerNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroClientNetwork {
    // Slot definitions: 0 = Ticket, 1 = IC Card
    public static void ticketVendorResult(World world, BlockPos pos, ItemStack stack, int slot, int balance) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack);
        buf.writeInt(slot);
        buf.writeInt(balance);
        ClientPlayNetworking.send(MetroServerNetwork.TICKET_VENDOR_RESULT, buf);
    }

    private static void registerTicketVendorGuiReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.TICKET_VENDOR_GUI, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack itemStack = buf.readItemStack();
            client.execute(() -> {
                client.setScreen(new TicketVendorScreen1(pos, itemStack));
            });
        });
    }

    private static void registerRemoveInventoryItem() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.REMOVE_INVENTORY_ITEM, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int slot = buf.readInt();
            client.execute(() -> {
                if (client.world != null) {
                    if (client.world.getBlockEntity(pos) instanceof Inventory inventory) {
                        inventory.removeStack(slot);
                    }
                }
            });
        });
    }

    public static void registerAll() {
        registerTicketVendorGuiReceiver();
        registerRemoveInventoryItem();
    }
}
