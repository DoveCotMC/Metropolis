package team.dovecotmc.metropolis.abstractinterface.util;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2025 Arrokoth All Rights Reserved.
 */
public class MALocalizationUtil {
    public static BaseComponent translatableText(String key) {
        return new TranslatableComponent(key);
    }

    public static BaseComponent translatableText(String key, Object... args) {
        return new TranslatableComponent(key, args);
    }

    public static BaseComponent literalText(String text) {
        return new TextComponent(text);
    }

    public static BaseComponent empty() {
        return new TextComponent("");
    }
}
