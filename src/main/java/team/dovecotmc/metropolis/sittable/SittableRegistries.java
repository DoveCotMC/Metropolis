package team.dovecotmc.metropolis.sittable;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.Block;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class SittableRegistries {
    private static final Map<Block, SittableRegistry> registries = new HashMap<>();

    public static void registerSittable(SittableRegistry sittable) {
        registries.put(sittable.block(), sittable);
    }

    public static Map<Block, SittableRegistry> getSittables() {
        return ImmutableMap.copyOf(registries);
    }
}
