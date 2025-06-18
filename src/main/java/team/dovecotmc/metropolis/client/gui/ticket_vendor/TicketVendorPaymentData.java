package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorPaymentData {
    public final EnumTicketVendorPaymentType type;
    public final int value;
    public final Component[] descriptions;
    public final ItemStack resultStack;

    public TicketVendorPaymentData(EnumTicketVendorPaymentType type, int value, Component[] descriptions, ItemStack resultStack) {
        this.type = type;
        this.value = value;
        this.descriptions = descriptions;
        this.resultStack = resultStack;
    }

    public enum EnumTicketVendorPaymentType {
        SINGLE_TRIP,
        IC_CARD,
        IC_CARD_CHARGE
    }
}
