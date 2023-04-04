package me.davamu.velocity.listener;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.serialize.SerializationException;

public class PlayerChatListener {

    private List<String> words;
    private String reason;
    private String punishment;
    private final ConsoleCommandSource consoleCommandSource;
    private final CommandManager manager;

    public PlayerChatListener(ConfigurationNode node, ProxyServer proxyServer, Logger logger) {
        try {
            this.words = node.node("blacklisted-words").getList(TypeToken.get(String.class), new ArrayList<>());
            this.reason = node.node("ban-reason").getString();
            this.punishment = node.node("punishment-command").getString();
        } catch (SerializationException e) {
            logger.error("Unable to get blacklisted-words");
        }
        consoleCommandSource = proxyServer.getConsoleCommandSource();
        manager = proxyServer.getCommandManager();
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        String message = event.getMessage().toLowerCase(Locale.ROOT);
        for (String word : words) {
            String wordLowerCase = word.toLowerCase(Locale.ROOT);

            if (!message.contains(wordLowerCase)) continue;

            Player player = event.getPlayer();
            String command = punishment + " " + player.getUsername() + " " + reason;
            manager.executeAsync(consoleCommandSource, command);
        }
    }


}
