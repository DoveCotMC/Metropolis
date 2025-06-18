package team.dovecotmc.metropolis.client.block.model.provider;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroModelProvicer implements ModelResourceProvider {
//    public static final Identifier MONITOR_MODEL = new Identifier(Metropolis.MOD_ID, "block/embedded/monitor");
    public static final ResourceLocation TURNSTILE_R_MODEL = new ResourceLocation(Metropolis.MOD_ID, "block/embedded/turnstile_r_model");

    @Override
    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
//        if (resourceId.equals(MONITOR_MODEL)) {
//            return new BlockMonitorModel();
//        } else {
//            return null;
//        }
        return null;
    }
}
