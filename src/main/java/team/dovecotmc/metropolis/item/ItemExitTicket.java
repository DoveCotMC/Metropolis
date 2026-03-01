package team.dovecotmc.metropolis.item;

import net.minecraft.world.item.Item;

public class ItemExitTicket extends Item implements IItemOpenGate {
    public ItemExitTicket(Properties settings) {
        super(settings.stacksTo(1));
    }
}
