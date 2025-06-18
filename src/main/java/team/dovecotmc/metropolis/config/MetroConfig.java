package team.dovecotmc.metropolis.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import team.dovecotmc.metropolis.Metropolis;

import java.io.File;
import java.io.FileOutputStream;
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
    public static final List<String> DEFAULT_DANGER_ITEMS = List.of(
            Registry.ITEM.getKey(Items.TNT).toString(),
            Registry.ITEM.getKey(Items.TNT_MINECART).toString(),
            Registry.ITEM.getKey(Items.GUNPOWDER).toString(),
            Registry.ITEM.getKey(Items.SHEARS).toString(),
            Registry.ITEM.getKey(Items.ARROW).toString(),
            Registry.ITEM.getKey(Items.BOW).toString(),
            Registry.ITEM.getKey(Items.IRON_AXE).toString(),
            Registry.ITEM.getKey(Items.DIAMOND_AXE).toString(),
            Registry.ITEM.getKey(Items.NETHERITE_AXE).toString(),
            Registry.ITEM.getKey(Items.IRON_SWORD).toString(),
            Registry.ITEM.getKey(Items.DIAMOND_SWORD).toString(),
            Registry.ITEM.getKey(Items.NETHERITE_SWORD).toString()
    );
    public List<String> dangerItems;
    public Item currencyItem;
    public int maxFare;

    public MetroConfig() {
        this.json = new JsonObject();
        this.dangerItems = DEFAULT_DANGER_ITEMS;
        this.currencyItem = Items.EMERALD;
        this.maxFare = 128;
    }

    public void refresh() {
        // Add default properties
        if (!json.has("danger_items")) {
            JsonArray array = new JsonArray();
            for (String i : DEFAULT_DANGER_ITEMS) {
                array.add(i);
            }
            json.add("danger_items", array);
        }
        this.dangerItems = new ArrayList<>();
        json.getAsJsonArray("danger_items").iterator().forEachRemaining(jsonElement -> {
            dangerItems.add(jsonElement.getAsString());
        });

        if (!json.has("currency_item")) {
            json.addProperty("currency_item", Registry.ITEM.getKey(Items.EMERALD).toString());
        }
        Item currency = Registry.ITEM.get(new ResourceLocation(json.get("currency_item").getAsString()));
        this.currencyItem = new ItemStack(currency).isEmpty() ? Items.EMERALD : currency;

        if (!json.has("max_fare")) {
            json.addProperty("max_fare", 128);
        }
        this.maxFare = 128;
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
