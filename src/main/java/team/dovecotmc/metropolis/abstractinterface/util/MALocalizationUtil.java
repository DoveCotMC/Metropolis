package team.dovecotmc.metropolis.abstractinterface.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2025 Arrokoth All Rights Reserved.
 */
public class MALocalizationUtil {
    public static MutableText translatableText(String key) {
        return new TranslatableText(key);
    }

    public static MutableText translatableText(String key, Object... args) {
        return new TranslatableText(key, args);
    }

    public static MutableText literalText(String text) {
        return new LiteralText(text);
    }

    public static MutableText empty() {
        return new LiteralText("");
    }
}
