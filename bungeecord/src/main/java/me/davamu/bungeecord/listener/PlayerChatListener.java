package me.davamu.bungeecord.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.Locale;

public class PlayerChatListener implements Listener {

    private final List<String> words;
    private final String reason;
    private final String punishment;

    public PlayerChatListener(Configuration configuration) {
        this.words = configuration.getStringList("blacklisted-words");
        this.reason = configuration.getString("ban-reason");
        this.punishment = configuration.getString("punishment-command");
    }


    @EventHandler
    public void onChat(ChatEvent event) {
        String message = event.getMessage().toLowerCase(Locale.ROOT);
        for (String word : words) {
            String wordLowerCase = word.toLowerCase(Locale.ROOT);

            if (!wordLowerCase.contains(message)) continue;

            Connection connection = event.getSender();
            if (!(connection instanceof ProxiedPlayer)) return;

            ProxiedPlayer player = (ProxiedPlayer)connection;
            String command = punishment + " " + player.getName() + " " + reason;
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
        }
    }

}
