package net.dodian.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dodian.Server;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

/**
 * This is the configuration utility that will fetch the configs from config.json.
 * If no config.json is preset, it will fallback to the default configs set in this class.
 *
 * DIRECTORIES CONFIGURATION
 * With directories configuration, you configure the data directory (optionally),
 * and then where in the data directory you can find definitions, characters, clipping etc.
 * Because those directories are expected to be sub-directories of the data directory.
 */
@Component
@Scope("singleton")
public class GameConfiguration {
    private HashMap<String, JsonElement> configValues = new HashMap<>();

    private final String ROOT_DIRECTORY = System.getProperty("user.dir") + "/";
    private final String DATA_DIRECTORY = ROOT_DIRECTORY + "/data/";

    public GameConfiguration() {
        loadConfig();
    }

    public void loadConfig() {
        Gson gson = new Gson();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(ROOT_DIRECTORY + "config.json"));
            JsonObject configData = gson.fromJson(reader, JsonObject.class);
            configData.keySet().forEach(key -> addConfig(key, configData.get(key)));
        } catch (FileNotFoundException e) {
            Server.getLogger().info("Couldn't find a config.json in the server's data folder. Running with default values.");
        }
    }

    public void addConfig(String key, JsonElement value) {
        configValues.put(key, value);
    }

    public JsonElement get(String key) {
        return this.configValues.get(key);
    }
}
