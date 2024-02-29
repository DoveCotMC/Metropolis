package team.dovecotmc.metropolis.metropolis.item;

import net.minecraft.item.Item;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemTicket extends Item {
    public static final String REMAIN_MONEY = "remain_money";
    public static final String ENTERED = "entered";
    public static final String ENTERED_ZONE = "entered_zone";
    public final boolean disposable;

    public ItemTicket(Settings settings, boolean disposable) {
        super(settings.maxCount(1));
        this.disposable = disposable;
    }
}
