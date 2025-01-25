package com.siliqon.siliqonmail.listeners;

import com.siliqon.siliqonmail.SiliqonMail;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.siliqon.siliqonmail.SiliqonMail.loadPlayerData;
import static com.siliqon.siliqonmail.SiliqonMail.savePlayerData;
import static com.siliqon.siliqonmail.helper.GeneralUtils.sendMessage;

public class PlayerListener implements Listener {
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerData(player);

        int mails = plugin.playerMail.get(player).getMail().size();
        if (mails > 0) {
            sendMessage(player, plugin.lang.getPendingMail().replace("{amount}", String.valueOf(mails)));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerData(player);
    }
}
