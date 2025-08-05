package team.dovecotmc.metropolis.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.entity.BlockEntityFareAdj;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.metropolis.item.ItemCard;
import team.dovecotmc.metropolis.item.ItemTicket;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroServerNetwork {
    public static final ResourceLocation TICKET_VENDOR_GUI = new ResourceLocation(Metropolis.MOD_ID, "ticket_vendor_gui");
    public static void openTicketVendorScreen(BlockPos pos, ServerPlayer player, ItemStack ticketStack) {
        FriendlyByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeItem(ticketStack);
        ServerPlayNetworking.send(player, TICKET_VENDOR_GUI, packet);
    }

    public static final ResourceLocation TICKET_VENDOR_CHARGE_GUI = new ResourceLocation(Metropolis.MOD_ID, "ticket_vendor_charge_gui");
    public static void openTicketVendorChargeScreen(BlockPos pos, ServerPlayer player, ItemStack ticketStack) {
        FriendlyByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeItem(ticketStack);
        ServerPlayNetworking.send(player, TICKET_VENDOR_CHARGE_GUI, packet);
    }

    public static final ResourceLocation REMOVE_INVENTORY_ITEM = new ResourceLocation(Metropolis.MOD_ID, "remove_item_in_inventory");
    public static void removeInventoryItem(int slot, BlockPos pos, ServerPlayer player) {
        FriendlyByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        packet.writeInt(slot);
        ServerPlayNetworking.send(player, REMOVE_INVENTORY_ITEM, packet);
    }

    public static final ResourceLocation FARE_ADJ_GUI = new ResourceLocation(Metropolis.MOD_ID, "fare_adj_gui");
    public static void openFareAdjustmentScreen(BlockPos pos, ServerPlayer player) {
        FriendlyByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(pos);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        // TODO: Card charger
        stack = ItemStack.EMPTY;
//        if (!(stack.getItem() instanceof ItemTicket || stack.getItem() instanceof ItemCard)) {
//            stack = ItemStack.EMPTY;
//        }
        packet.writeItem(stack);
        ServerPlayNetworking.send(player, FARE_ADJ_GUI, packet);
    }

    public static final ResourceLocation TICKET_VENDOR_RESULT = new ResourceLocation(Metropolis.MOD_ID, "ticket_vendor_result");
    private static void registerTicketVendorResultReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(TICKET_VENDOR_RESULT, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItem();

            // Slot definitions: 0 = Ticket, 1 = IC Card
            int balance = buf.readInt();

            Item item = Metropolis.config.currencyItem;
            server.execute(() -> {
                if (balance > 0) {
                    for (int i = 0; i < balance / item.getMaxStackSize(); i++) {
                        player.getInventory().setItem(player.getInventory().findSlotMatchingItem(new ItemStack(item)), ItemStack.EMPTY);
                    }
                    if (balance % item.getMaxStackSize() > 0) {
                        player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(new ItemStack(item)), balance % item.getMaxStackSize());
                    }
                }

                Level world = player.getLevel();
                if (world != null) {
                    world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    if (world.getBlockEntity(pos) instanceof BlockEntityTicketVendor blockEntity) {
                        blockEntity.setItem(0, stack);
                        CompoundTag nbt = blockEntity.saveWithoutMetadata();
                        nbt.putLong(BlockEntityTicketVendor.TICKET_ANIMATION_BEGIN_TIME, world.getGameTime());
                        blockEntity.load(nbt);
                        player.connection.send(blockEntity.getUpdatePacket());
                    }
                }
            });
        });
    }

    public static final ResourceLocation TICKET_VENDOR_CLOSE = new ResourceLocation(Metropolis.MOD_ID, "ticket_vendor_close");
    private static void registerTicketVendorCloseReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(TICKET_VENDOR_CLOSE, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItem();
            int balance = buf.readInt();
            Item item = Metropolis.config.currencyItem;
            server.execute(() -> {
                if (balance > 0) {
                    for (int i = 0; i < balance / item.getMaxStackSize(); i++) {
                        player.getInventory().setItem(player.getInventory().findSlotMatchingItem(new ItemStack(item)), ItemStack.EMPTY);
                    }
                    if (balance % item.getMaxStackSize() > 0) {
                        player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(new ItemStack(item)), balance % item.getMaxStackSize());
                    }
                }

                Level world = player.getLevel();
                if (world != null) {
                    world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    if (world.getBlockEntity(pos) instanceof BlockEntityTicketVendor blockEntity) {
                        blockEntity.removeItemNoUpdate(1);
                        CompoundTag nbt = blockEntity.saveWithoutMetadata();
                        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                        blockEntity.load(nbt);
                        player.connection.send(blockEntity.getUpdatePacket());
                        MetroServerNetwork.removeInventoryItem(1, pos, player);
                    }
                }
            });
        });
    }

    public static final ResourceLocation FARE_ADJ_CLOSE = new ResourceLocation(Metropolis.MOD_ID, "fare_adj_close");
    private static void registerFareAdjCloseReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(FARE_ADJ_CLOSE, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItem();
            int balance = buf.readInt();
            Item item = Metropolis.config.currencyItem;
            server.execute(() -> {
                System.out.println(pos);
                System.out.println(stack);
                System.out.println(balance);
                System.out.println(item);
                if (balance > 0) {
                    for (int i = 0; i < balance / item.getMaxStackSize(); i++) {
                        player.getInventory().setItem(player.getInventory().findSlotMatchingItem(new ItemStack(item)), ItemStack.EMPTY);
                    }
                    if (balance % item.getMaxStackSize() > 0) {
                        player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(new ItemStack(item)), balance % item.getMaxStackSize());
                    }
                }
                
                Level world = player.getLevel();
                if (world != null) {
                    world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    if (world.getBlockEntity(pos) instanceof BlockEntityFareAdj blockEntity) {
                        blockEntity.setItem(0, stack);
                        CompoundTag nbt = blockEntity.saveWithoutMetadata();
                        nbt.putLong(BlockEntityTicketVendor.TICKET_ANIMATION_BEGIN_TIME, world.getGameTime());
                        blockEntity.load(nbt);
                        player.connection.send(blockEntity.getUpdatePacket());
                    }
                }
            });
        });
    }

    public static final ResourceLocation GET_CURRENCY_ITEM = new ResourceLocation(Metropolis.MOD_ID, "get_currency_item");
    public static final ResourceLocation GET_CURRENCY_ITEM_RECEIVER = new ResourceLocation(Metropolis.MOD_ID, "get_currency_item_receiver");
    public static void registerCurrencyItemReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(GET_CURRENCY_ITEM, (server, player, handler, buf, responseSender) -> {
            ServerPlayNetworking.send(player, GET_CURRENCY_ITEM_RECEIVER, PacketByteBufs.create().writeItem(new ItemStack(Metropolis.config.currencyItem)));
        });
    }

    public static final ResourceLocation GET_MAX_FARE = new ResourceLocation(Metropolis.MOD_ID, "get_max_fare");
    public static final ResourceLocation GET_MAX_FARE_RECEIVER = new ResourceLocation(Metropolis.MOD_ID, "get_max_fare_receiver");
    public static void registerMaxFareReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(GET_MAX_FARE, (server, player, handler, buf, responseSender) -> {
            ServerPlayNetworking.send(player, GET_MAX_FARE_RECEIVER, PacketByteBufs.copy(PacketByteBufs.create().writeInt(Metropolis.config.maxFare)));
        });
    }

    public static void registerAll() {
        registerTicketVendorResultReceiver();
        registerTicketVendorCloseReceiver();
        registerCurrencyItemReceiver();
        registerFareAdjCloseReceiver();
    }
}
