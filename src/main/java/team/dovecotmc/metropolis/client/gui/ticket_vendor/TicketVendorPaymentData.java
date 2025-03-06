package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorPaymentData {
    public final EnumTicketVendorPaymentType type;
    public final int value;
    public final Text[] descriptions;
    public final ItemStack resultStack;

    public TicketVendorPaymentData(EnumTicketVendorPaymentType type, int value, Text[] descriptions, ItemStack resultStack) {
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
