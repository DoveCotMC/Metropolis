package team.dovecotmc.metropolis.abstractinterface.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2025 Arrokoth All Rights Reserved.
 */
public class MALocalizationUtil {
    public static MutableComponent translatableText(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent translatableText(String key, Object... args) {
        return Component.translatable(key, args);
    }

    public static MutableComponent literalText(String text) {
        return Component.literal(text);
    }

    public static MutableComponent empty() {
        return Component.empty();
    }
}
