package me.davamu.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.davamu.velocity.listener.PlayerChatListener;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "ratfilter",
        name = "Rat filter",
        version = "0.1.0-SNAPSHOT",
        url = "https://davamu.me",
        description = "Filter for rats",
        authors = {"DaVaMu"}
)
public class RatFilterVelocity {

    private final Path pluginPath;
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public RatFilterVelocity(@DataDirectory Path pluginPath, ProxyServer server, Logger logger) {
        this.pluginPath = pluginPath;
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(final ProxyInitializeEvent event) {
        ConfigurationNode node = registerConfig(pluginPath);
        if (node != null) {
            server.getEventManager().register(this, new PlayerChatListener(node, server, logger));
        } else {
            throw new NullPointerException("The configuration is null.");
        }

    }

    private ConfigurationNode registerConfig(Path pluginPath) {
        // Plugin directory
        if (Files.notExists(pluginPath)) {
            try {
                Files.createDirectory(pluginPath);
            } catch (IOException e) {
                logger.error("Unable to create plugin directory", e);
            }
        }

        // Config directory
        Path configPath = pluginPath.resolve("config.yml");
        if (Files.notExists(configPath)) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(in, configPath);
                logger.info("A config.yml has been created because it did not exist.");
            } catch (IOException e) {
                logger.error("Unable to create plugin configuration", e);
            }
        } else {
            logger.info("A config.yml has not been created because it already exists.");
        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(pluginPath.resolve("config.yml"))
                .build();

        try {
            return loader.load();
        } catch (ConfigurateException e) {
            logger.error("Unable to load configuration");
            return null;
        }

    }


}