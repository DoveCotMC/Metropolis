package team.dovecotmc.metropolis.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroServerNetwork {
    public static final Identifier TICKET_VENDOR_GUI = new Identifier(Metropolis.MOD_ID, "ticket_vendor_gui");
    public static void openTicketVendorScreen(BlockPos pos, ServerPlayerEntity player, ItemStack ticketStack) {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeItemStack(ticketStack);
        ServerPlayNetworking.send(player, TICKET_VENDOR_GUI, packet);
    }

    public static final Identifier TICKET_VENDOR_CHARGE_GUI = new Identifier(Metropolis.MOD_ID, "ticket_vendor_charge_gui");
    public static void openTicketVendorChargeScreen(BlockPos pos, ServerPlayerEntity player, ItemStack ticketStack) {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeItemStack(ticketStack);
        ServerPlayNetworking.send(player, TICKET_VENDOR_CHARGE_GUI, packet);
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
            int balance = buf.readInt();

            Item item = Items.EMERALD;
            server.execute(() -> {
                if (balance > 0) {
                    for (int i = 0; i < balance / item.getMaxCount(); i++) {
                        player.getInventory().setStack(player.getInventory().getSlotWithStack(new ItemStack(item)), ItemStack.EMPTY);
                    }
                    if (balance % item.getMaxCount() > 0) {
                        player.getInventory().removeStack(player.getInventory().getSlotWithStack(new ItemStack(item)), balance % item.getMaxCount());
                    }
                }

                World world = player.getWorld();
                if (world != null) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (world.getBlockEntity(pos) instanceof BlockEntityTicketVendor blockEntity) {
                        blockEntity.setStack(0, stack);
                        NbtCompound nbt = blockEntity.createNbt();
                        nbt.putLong(BlockEntityTicketVendor.TICKET_ANIMATION_BEGIN_TIME, world.getTime());
                        blockEntity.readNbt(nbt);
                        player.networkHandler.sendPacket(blockEntity.toUpdatePacket());
                    }
                }
            });
        });
    }

    public static final Identifier TICKET_VENDOR_CLOSE = new Identifier(Metropolis.MOD_ID, "ticket_vendor_close");
    private static void registerTicketVendorCloseReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(TICKET_VENDOR_CLOSE, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItemStack();
            int balance = buf.readInt();
            Item item = Items.EMERALD;
            server.execute(() -> {
                if (balance > 0) {
                    for (int i = 0; i < balance / item.getMaxCount(); i++) {
                        player.getInventory().setStack(player.getInventory().getSlotWithStack(new ItemStack(item)), ItemStack.EMPTY);
                    }
                    if (balance % item.getMaxCount() > 0) {
                        player.getInventory().removeStack(player.getInventory().getSlotWithStack(new ItemStack(item)), balance % item.getMaxCount());
                    }
                }

                World world = player.getWorld();
                if (world != null) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (world.getBlockEntity(pos) instanceof BlockEntityTicketVendor blockEntity) {
                        blockEntity.removeStack(1);
                        NbtCompound nbt = blockEntity.createNbt();
                        player.setStackInHand(Hand.MAIN_HAND, stack);
                        blockEntity.readNbt(nbt);
                        player.networkHandler.sendPacket(blockEntity.toUpdatePacket());
                        MetroServerNetwork.removeInventoryItem(1, pos, player);
                    }
                }
            });
        });
    }

    public static void registerAll() {
        registerTicketVendorResultReceiver();
        registerTicketVendorCloseReceiver();
    }
}
