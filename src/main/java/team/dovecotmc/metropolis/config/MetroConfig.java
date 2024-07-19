package team.dovecotmc.metropolis.config;

import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import team.dovecotmc.metropolis.Metropolis;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroConfig {
    public static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("metropolis").resolve("common.json");
    public JsonObject json;
    public List<String> dangerItems;

    public MetroConfig() {
        this.json = new JsonObject();
        this.dangerItems = List.of("minecraft:tnt", "minecraft:iron_sword", "minecraft:diamond_sword", "minecraft:netherite_sword");
    }

    public void refresh() {
        // Add default properties
        if (!json.has("danger_items")) {
            List<String> items = List.of("minecraft:tnt", "minecraft:iron_sword", "minecraft:diamond_sword", "minecraft:netherite_sword");
            JsonArray array = new JsonArray();
            for (String i : items) {
                array.add(i);
            }
            json.add("danger_items", array);
        }
        this.dangerItems = new ArrayList<>();
        json.getAsJsonArray("danger_items").iterator().forEachRemaining(jsonElement -> {
            dangerItems.add(jsonElement.getAsString());
        });
    }

    public static MetroConfig load() {
        JsonObject obj = null;
        MetroConfig config = new MetroConfig();
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
            config.refresh();
        }
        return config;
    }

    public static void save(MetroConfig config) {
        try {
            config.refresh();
            new File(CONFIG_FILE_PATH.getParent().toUri()).mkdirs();
            Files.writeString(CONFIG_FILE_PATH, (new GsonBuilder()).setPrettyPrinting().create().toJson(config.json));
        } catch (Exception e) {
            Metropolis.LOGGER.error("Could not save configuration file: ", e);
        }
    }
}
