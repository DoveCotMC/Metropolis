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
        return org.mtr.mod.MtrCommonUtil.getBrushItem().data;
        // MTR3
//        return mtr.MtrCommonUtil.getBrushItem();
    }
}
