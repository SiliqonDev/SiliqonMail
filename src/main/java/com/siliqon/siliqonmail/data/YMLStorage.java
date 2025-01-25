package com.siliqon.siliqonmail.data;

import com.siliqon.siliqonmail.SiliqonMail;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.siliqon.siliqonmail.helper.GeneralUtils.log;

public class YMLStorage {
    private static final SiliqonMail plugin = SiliqonMail.getInstance();
    private static File dataFile; public static FileConfiguration data;

    public YMLStorage() {}

    public static void load() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
        }

        data = YamlConfiguration.loadConfiguration(dataFile);
    }
    public static void save() {
        saveAllData(true);
    }

    public static Mailbox getPlayerData(OfflinePlayer player) {
        List<Mail> existingMail = new ArrayList<>();

        String uuid = player.getUniqueId().toString();
        if (data.getConfigurationSection(uuid) != null) {
            for (String key : data.getConfigurationSection(uuid).getKeys(false)) {
                String dataPath = uuid+"."+key;

                OfflinePlayer sender = Bukkit.getOfflinePlayer(UUID.fromString(data.getString(dataPath+".sender")));
                Long sent_at = data.getLong(dataPath+".sent_at");
                String contents = data.getString(dataPath+".contents");

                Mail mail = new Mail(contents, sender, sent_at);
                existingMail.add(mail);
            }
        }

        Mailbox mailbox = new Mailbox(existingMail);
        return mailbox;
    }
    public static void savePlayerData(OfflinePlayer player, boolean log) {
        data.set(player.getUniqueId().toString(), null);
        Mailbox mailbox = plugin.playerMail.get(player);
        int itr = 1;
        for (Mail mail : mailbox.getMail()) {
            String dataPath = player.getUniqueId()+"."+itr;

            data.set(dataPath+".sender", mail.getSender().getUniqueId().toString());
            data.set(dataPath+".sent_at", mail.getSentAt());
            data.set(dataPath+".contents", mail.getContents());

            itr++;
        }

        try {
            data.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(log) log("Saved data for "+player.getName());
    }
    public static void saveAllData(boolean log) {
        plugin.playerMail.forEach((player, mailbox) -> {
            data.set(player.getUniqueId().toString(), null);
            int itr = 1;
            for (Mail mail : mailbox.getMail()) {
                String dataPath = player.getUniqueId()+"."+itr;

                data.set(dataPath+".sender", mail.getSender().getUniqueId().toString());
                data.set(dataPath+".sent_at", mail.getSentAt());
                data.set(dataPath+".contents", mail.getContents());

                itr++;
            }
        });

        try {
            data.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(log) log("Saved data for "+plugin.playerMail.size()+" player(s).");
    }
}
