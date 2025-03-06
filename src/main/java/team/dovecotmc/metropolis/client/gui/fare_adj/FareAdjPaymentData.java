package team.dovecotmc.metropolis.client.gui.fare_adj;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class FareAdjPaymentData {
    public final EnumTicketVendorPaymentType type;
    public final int value;
    public final Text[] descriptions;
    public final ItemStack resultStack;

    public FareAdjPaymentData(EnumTicketVendorPaymentType type, int value, Text[] descriptions, ItemStack resultStack) {
        this.type = type;
        this.value = value;
        this.descriptions = descriptions;
        this.resultStack = resultStack;
    }

    public enum EnumTicketVendorPaymentType {
        PAY_FARE
    }
}
