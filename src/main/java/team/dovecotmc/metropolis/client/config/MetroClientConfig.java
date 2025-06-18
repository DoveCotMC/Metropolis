package team.dovecotmc.metropolis.client.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import team.dovecotmc.metropolis.Metropolis;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Environment(EnvType.CLIENT)
public class MetroClientConfig {
    public static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("metropolis").resolve("client.json");
    public JsonObject json;
    public boolean enableGlowingTexture;
    public boolean enableStationInfoOverlay;

    public MetroClientConfig() {
        this.json = new JsonObject();
        this.enableGlowingTexture = true;
        this.enableStationInfoOverlay = true;
    }

    public void reload() {
//        if ()
    }

    public void create() {
        // Add default properties
        if (!json.has("enable_glowing_texture")) {
            json.addProperty("enable_glowing_texture", true);
        }
        if (!json.has("enable_station_info_overlay")) {
            json.addProperty("enable_station_info_overlay", true);
        }
        // Write
        this.enableGlowingTexture = json.get("enable_glowing_texture").getAsBoolean();
        this.enableStationInfoOverlay = json.get("enable_station_info_overlay").getAsBoolean();
    }

    public void refresh() {
        json.addProperty("enable_glowing_texture", this.enableGlowingTexture);
        json.addProperty("enable_station_info_overlay", this.enableStationInfoOverlay);
    }

    public static MetroClientConfig load() {
        JsonObject obj = null;
        MetroClientConfig config = new MetroClientConfig();
        if (Files.exists(CONFIG_FILE_PATH)) {
            try {
                byte[] bytes = Files.readAllBytes(CONFIG_FILE_PATH);
                obj = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
            } catch (Exception e) {
                Metropolis.LOGGER.error("Could not load configuration file: ", e);
            }
        } else {
            save(config);
        }
        if (obj != null) {
            config.json = obj;
            config.create();
        }
        return config;
    }

    public static void save(MetroClientConfig config) {
        try {
            config.refresh();
            new File(CONFIG_FILE_PATH.getParent().toUri()).mkdirs();
            Files.writeString(CONFIG_FILE_PATH, (new GsonBuilder()).setPrettyPrinting().create().toJson(config.json));
        } catch (Exception e) {
            Metropolis.LOGGER.error("Could not save configuration file: ", e);
        }
    }
}
