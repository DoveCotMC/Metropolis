package team.dovecotmc.metropolis.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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
    public static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getGameDir().resolve("config").resolve("metropolis").resolve("common.json");
    public JsonObject json;
    public List<String> dangerItems;
    public Item currencyItem;

    public MetroConfig() {
        this.json = new JsonObject();
        this.dangerItems = List.of(
                Registry.ITEM.getId(Items.TNT).toString(),
                Registry.ITEM.getId(Items.IRON_SWORD).toString(),
                Registry.ITEM.getId(Items.DIAMOND_SWORD).toString(),
                Registry.ITEM.getId(Items.NETHERITE_SWORD).toString()
        );
        this.currencyItem = Items.EMERALD;
    }

    public void refresh() {
        // Add default properties
        if (!json.has("danger_items")) {
            List<String> items = List.of(
                    Registry.ITEM.getId(Items.TNT).toString(),
                    Registry.ITEM.getId(Items.IRON_SWORD).toString(),
                    Registry.ITEM.getId(Items.DIAMOND_SWORD).toString(),
                    Registry.ITEM.getId(Items.NETHERITE_SWORD).toString()
            );
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

        if (!json.has("currency_item")) {
            json.addProperty("currency_item", Registry.ITEM.getId(Items.EMERALD).toString());
        }
        this.currencyItem = Registry.ITEM.get(new Identifier(json.get("currency_item").getAsString()));
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
        }
        config.refresh();
        MetroConfig.save(config);
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
