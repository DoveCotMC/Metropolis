package team.dovecotmc.metropolis.abstractinterface.util;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2025 Arrokoth All Rights Reserved.
 */
public class MACommonUtil {
    public static ResourceLocation identifier(String s1, String s2) {
        return new ResourceLocation(s1, s2);
    }

    public static Component getTooltip(Item item, Style style) {
        ResourceLocation id = Registry.ITEM.getKey(item);
        return MALocalizationUtil.translatableText("tooltip." + id.getNamespace() + "." + id.getPath()).setStyle(style);
    }
}
