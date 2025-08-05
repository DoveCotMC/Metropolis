package team.dovecotmc.metropolis.item;

import net.minecraft.world.item.Item;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemExitTicket extends Item implements IItemOpenGate {
    public ItemExitTicket(Properties settings) {
        super(settings.stacksTo(1));
    }
}
