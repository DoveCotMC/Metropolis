package team.dovecotmc.metropolis.metropolis.client;

import mtr.client.IResourcePackCreatorProperties;
import mtr.client.TrainClientRegistry;
import mtr.client.TrainProperties;
import net.minecraft.resource.ResourceManager;
import team.dovecotmc.metropolis.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroCustomResources implements IResourcePackCreatorProperties {

    public static void reload(ResourceManager manager) {
        Metropolis.LOGGER.info("Loading custom resources depends on NTE.");
//        TrainClientRegistry.register("01a01", new TrainProperties("mlr_train"));
    }
}
