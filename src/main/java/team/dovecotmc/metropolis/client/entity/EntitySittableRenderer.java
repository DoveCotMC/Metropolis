package team.dovecotmc.metropolis.client.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import team.dovecotmc.metropolis.entity.EntitySittable;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class EntitySittableRenderer extends EntityRenderer<Entity> {
    public EntitySittableRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return null;
    }
}
