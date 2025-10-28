package team.dovecotmc.metropolis.util;

import net.minecraft.world.item.Item;


/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrCommonUtil {
    public static Item getBrushItem() {
        // MTR4
//        return org.mtr.mod.Items.BRUSH.get().data;
        // MTR3
        return mtr.Items.BRUSH.get();
    }
}
