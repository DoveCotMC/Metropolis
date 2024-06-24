package team.dovecotmc.metropolis.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.client.block.entity.TicketVendorBlockEntityRenderer;

import java.util.function.Predicate;

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

    public static final Identifier REMOVE_INVENTORY_ITEM = new Identifier(Metropolis.MOD_ID, "remove_item_in_inventory");
    public static void removeInventoryItem(int slot, BlockPos pos, ServerPlayerEntity player) {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeInt(slot);
        ServerPlayNetworking.send(player, REMOVE_INVENTORY_ITEM, packet);
    }

    public static final Identifier TICKET_VENDOR_RESULT = new Identifier(Metropolis.MOD_ID, "ticket_vendor_result");
    private static void registerTicketVendorResultReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(TICKET_VENDOR_RESULT, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItemStack();

            // Slot definitions: 0 = Ticket, 1 = IC Card
            int slot = buf.readInt();
            int balance = buf.readInt();

            Item item = Items.EMERALD;
            server.execute(() -> {
                int balance1 = balance;
                for (int i = 0; i < balance1 / item.getMaxCount(); i++) {
                    player.getInventory().setStack(player.getInventory().getSlotWithStack(new ItemStack(item)), ItemStack.EMPTY);
                }
                player.getInventory().removeStack(player.getInventory().getSlotWithStack(new ItemStack(item)), balance1 % item.getMaxCount());

                World world = player.getWorld();
                if (world != null) {
                    BlockEntity entityRaw = world.getBlockEntity(pos);
                    if (entityRaw instanceof BlockEntityTicketVendor blockEntity) {
                        blockEntity.setStack(slot, stack);
                        NbtCompound nbt = blockEntity.createNbt();
                        nbt.putLong(BlockEntityTicketVendor.TICKET_ANIMATION_BEGIN_TIME, world.getTime());
                        blockEntity.readNbt(nbt);
                        player.networkHandler.sendPacket(blockEntity.toUpdatePacket());
                    }
                }
//                player.getInventory().insertStack(stack);
            });
        });
    }

    public static void registerAll() {
        registerTicketVendorResultReceiver();
    }
}
