package team.dovecotmc.metropolis.client.gui.ticket_vendor;

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

    public TicketVendorPaymentData(EnumTicketVendorPaymentType type, int value, Text[] descriptions) {
        this.type = type;
        this.value = value;
        this.descriptions = descriptions;
    }

    public enum EnumTicketVendorPaymentType {
        SINGLE_TRIP,
        IC_CARD,
        IC_CARD_CHARGE
    }
}
