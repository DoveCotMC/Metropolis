package team.dovecotmc.metropolis.config;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import team.dovecotmc.metropolis.Metropolis;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroConfig {
    public static final Path CONFIG_FILE_PATH;
    public JsonObject json;

    public MetroConfig() {
        this.json = new JsonObject();
    }

    public static void save(MetroConfig config) {
        try {
            Files.writeString(CONFIG_FILE_PATH, config.json.getAsString());
        } catch (Exception e) {
            Metropolis.LOGGER.error("Could not save configuration file: ", e);
        }
    }

    static {
        CONFIG_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("mtr.json");
    }
}
