package team.dovecotmc.metropolis.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import team.dovecotmc.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroEntities {
    public static final EntityType<Entity> SITTABLE = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Metropolis.MOD_ID, "sittable"), FabricEntityTypeBuilder.create().entityFactory(EntitySittable::new).build());

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Entities");
    }
}
