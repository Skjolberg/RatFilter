package me.davamu.bungeecord;

import me.davamu.bungeecord.listener.PlayerChatListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class RatFilterBungeeCord extends Plugin {

    @Override
    public void onEnable() {
        getLogger().info("Starting BungeeCord plugin: RatFilter");
        Configuration config = registerConfig();
        if (config != null) {
            ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerChatListener(config));
        } else {
            throw new NullPointerException("The configuration is null.");
        }

    }

    @Override
    public void onDisable() {

    }

    public Configuration registerConfig() {
        Configuration config;
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            InputStream in = getResourceAsStream("config.yml");
            try {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException exception) {
            throw new RuntimeException("Unable to read file, exception:", exception);
        }
        return config;
    }

}