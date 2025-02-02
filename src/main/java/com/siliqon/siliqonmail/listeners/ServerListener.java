package com.siliqon.siliqonmail.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import static com.siliqon.siliqonmail.helper.GeneralUtils.logError;
import static com.siliqon.siliqonmail.helper.GeneralUtils.sendMessage;

public class ServerListener implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        if (e.getType() == ServerLoadEvent.LoadType.RELOAD) {
            logError("Server reload detected! Reloading your server is NOT supported!");
            logError("Please DO NOT report any issues that arise from this");
            logError("It is recommended to restart the server after this message");
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                sendMessage(player, "&cServer reload detected! Reloading your server with the plugin is NOT supported!");
                sendMessage(player, "&cIt is suggested to restart the server after this message");
            }
        }
    }
}
