package team.dovecotmc.old.metropolis.client.gui.ticket_vendor;

import net.minecraft.world.item.ItemStack;
import team.dovecotmc.old.metropolis.mtr.WrappedMtrStation;

import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorData {
    public final ItemStack cardStack;
    public final Set<WrappedMtrStation> stations;

    public TicketVendorData(ItemStack cardStack, Set<WrappedMtrStation> stations) {
        this.cardStack = cardStack;
        this.stations = stations;
    }
}
