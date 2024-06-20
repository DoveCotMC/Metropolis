package team.dovecotmc.metropolis.metropolis.client;

import fabric.cn.zbx1425.mtrsteamloco.CustomResources;
import fabric.cn.zbx1425.mtrsteamloco.render.scripting.train.ScriptedTrainRenderer;
import mtr.client.IResourcePackCreatorProperties;
import mtr.client.TrainClientRegistry;
import mtr.client.TrainProperties;
import mtr.render.JonModelTrainRenderer;
import mtr.sound.JonTrainSound;
import net.fabricmc.fabric.impl.resource.loader.BuiltinModResourcePackSource;
import net.fabricmc.fabric.impl.resource.loader.client.pack.ProgrammerArtResourcePack;
import net.minecraft.resource.LifecycledResourceManagerImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import team.dovecotmc.metropolis.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroCustomResources implements IResourcePackCreatorProperties {

    public static void reload(ResourceManager manager) {
        Metropolis.LOGGER.info("Loading custom resources depends on NTE.");

//        BuiltinModResourcePackSource
//        ProgrammerArtResourcePack

        TrainProperties properties = new TrainProperties(
                "",
                Text.translatable("train.01a01.name"),
                Text.translatable("train.01a01.description").getString(),
                Text.translatable("train.01a01.wikipedia_article").getString(),
                0xE4002B,
                0,
                0,
                8.5f,
                false,
                true,
                new JonModelTrainRenderer(null, "", "", ""),
                new JonTrainSound("mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true))
        );
//        CustomResources
        TrainClientRegistry.register("01a01", properties);
    }
}
