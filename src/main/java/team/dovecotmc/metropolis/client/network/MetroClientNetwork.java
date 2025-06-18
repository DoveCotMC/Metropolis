package team.dovecotmc.metropolis.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.client.gui.fare_adj.FareAdjData;
import team.dovecotmc.metropolis.client.gui.fare_adj.FareAdjScreenMain;
import team.dovecotmc.metropolis.client.gui.ticket_vendor.TicketVendorData;
import team.dovecotmc.metropolis.client.gui.ticket_vendor.TicketVendorScreen1;
import team.dovecotmc.metropolis.client.gui.ticket_vendor.TicketVendorScreen4;
import team.dovecotmc.metropolis.network.MetroServerNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroClientNetwork {
    public static void ticketVendorClose(BlockPos pos, ItemStack stack, int balance) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
        buf.writeInt(balance);
        ClientPlayNetworking.send(MetroServerNetwork.TICKET_VENDOR_CLOSE, buf);
    }

    public static void ticketVendorResult(BlockPos pos, ItemStack stack, int balance) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
        buf.writeInt(balance);
        ClientPlayNetworking.send(MetroServerNetwork.TICKET_VENDOR_RESULT, buf);
    }

    public static void fareAdjClose(BlockPos pos, ItemStack stack, int balance) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
        buf.writeInt(balance);
        ClientPlayNetworking.send(MetroServerNetwork.FARE_ADJ_CLOSE, buf);
    }

    private static void registerTicketVendorGuiReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.TICKET_VENDOR_GUI, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack itemStack = buf.readItem();
            client.execute(() -> client.setScreen(new TicketVendorScreen1(pos, new TicketVendorData(itemStack))));
        });
    }

    private static void registerTicketVendorChargeGuiReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.TICKET_VENDOR_CHARGE_GUI, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack itemStack = buf.readItem();
            client.execute(() -> client.setScreen(new TicketVendorScreen4(pos, null, new TicketVendorData(itemStack))));
        });
    }

    private static void registerRemoveInventoryItem() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.REMOVE_INVENTORY_ITEM, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int slot = buf.readInt();
            client.execute(() -> {
                if (client.level != null) {
                    if (client.level.getBlockEntity(pos) instanceof Container inventory) {
                        inventory.removeItemNoUpdate(slot);
                    }
                }
            });
        });
    }

    private static void registerFareAdjGuiReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.FARE_ADJ_GUI, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItem();
            client.execute(() -> client.setScreen(new FareAdjScreenMain(pos, new FareAdjData(stack))));
        });
    }

    public static Item currencyItem = Items.EMERALD;

    public static void updateCurrencyItem() {
        ClientPlayNetworking.send(MetroServerNetwork.GET_CURRENCY_ITEM, PacketByteBufs.create());
    }

    public static void registerGetCurrencyItemReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.GET_CURRENCY_ITEM_RECEIVER, (client, handler, buf, responseSender) -> {
            currencyItem = buf.readItem().getItem();
        });
    }


    public static int maxFare = Metropolis.config.maxFare;

    public static void updateMaxFare() {
        ClientPlayNetworking.send(MetroServerNetwork.GET_MAX_FARE, PacketByteBufs.create());
    }

    public static void registerGetMaxFareReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(MetroServerNetwork.GET_MAX_FARE_RECEIVER, (client, handler, buf, responseSender) -> {
            maxFare = buf.readInt();
        });
    }

    public static void registerAll() {
        registerTicketVendorGuiReceiver();
        registerTicketVendorChargeGuiReceiver();
        registerRemoveInventoryItem();
        registerGetCurrencyItemReceiver();
        registerFareAdjGuiReceiver();
        registerGetMaxFareReceiver();
    }
}
