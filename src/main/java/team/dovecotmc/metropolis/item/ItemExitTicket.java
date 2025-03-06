package team.dovecotmc.metropolis.item;

import net.minecraft.item.Item;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemExitTicket extends Item implements IItemOpenGate {
    public ItemExitTicket(Settings settings) {
        super(settings.maxCount(1));
    }
}
