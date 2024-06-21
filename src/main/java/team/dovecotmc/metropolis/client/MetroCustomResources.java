package team.dovecotmc.metropolis.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.client.*;
import mtr.mappings.Text;
import mtr.render.JonModelTrainRenderer;
import mtr.sound.JonTrainSound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import team.dovecotmc.metropolis.Metropolis;

import java.io.IOException;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroCustomResources implements IResourcePackCreatorProperties {

    public static void reload(ResourceManager manager) {
        Metropolis.LOGGER.info("Loading custom resources depends on NTE.");

        try {
            JsonObject dummyModel = new JsonObject();
            dummyModel.add("elements", new JsonArray());
            dummyModel.add("outliner", new JsonArray());

            JsonObject dummyModelResolution = new JsonObject();
            dummyModel.addProperty("width", 0);
            dummyModel.addProperty("height", 0);
            dummyModel.add("resolution", dummyModelResolution);

            dummyModel.addProperty("zbxFlag", "dummyBbData.resourceLocation");
            dummyModel.addProperty("actualPath", Metropolis.MOD_ID + ":trains/01a01/01a01.obj");
            dummyModel.addProperty("textureId", "mtr:air");
            dummyModel.addProperty("flipV", true);
            dummyModel.addProperty("preloadBbModel", false);

            JsonObject modelProperties = new Gson().fromJson(manager.getResource(new Identifier(Metropolis.MOD_ID, "trains/01a01/01a01.json")).get().getReader(), JsonObject.class);

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
                    new JonModelTrainRenderer(new DynamicTrainModel(dummyModel, modelProperties, DoorAnimationType.BOUNCY_1), "mtr:air", "mtr:air", "mtr:air"),
                    new JonTrainSound("mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true))
            );
            TrainClientRegistry.register("01a01", properties);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
