package me.lewi.dev.mineskies.managers;

import me.lewi.dev.mineskies.Core;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(Core core) {
        ConfigManager.config = core.getConfig();
        core.saveDefaultConfig();
    }

    public static String getConnectionString() {
        return config.getString("connection-string");
    }

}
