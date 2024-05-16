package team.dovecotmc.metropolis.metropolis.client.block.model.provider;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.client.block.model.BlockMonitorModel;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockMonitorModelProvider implements ModelResourceProvider {
    public static final Identifier MONITOR_MODEL = new Identifier(Metropolis.MOD_ID, "block/embedded/monitor");

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
        if (resourceId.equals(MONITOR_MODEL)) {
            return new BlockMonitorModel();
        } else {
            return null;
        }
    }
}
