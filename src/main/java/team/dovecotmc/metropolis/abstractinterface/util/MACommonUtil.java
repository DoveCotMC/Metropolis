package team.dovecotmc.metropolis.abstractinterface.util;

import net.minecraft.util.Identifier;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2025 Arrokoth All Rights Reserved.
 */
public class MACommonUtil {
    public static Identifier identifier(String s1, String s2) {
        return new Identifier(s1, s2);
    }
}
