package com.siliqon.siliqonmail;

import co.aikar.commands.PaperCommandManager;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.siliqon.siliqonmail.commands.MailCommand;
import com.siliqon.siliqonmail.data.Mailbox;
import com.siliqon.siliqonmail.data.YMLStorage;
import com.siliqon.siliqonmail.gui.GUIManager;
import com.siliqon.siliqonmail.listeners.GUIListener;
import com.siliqon.siliqonmail.listeners.PlayerListener;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;

public final class SiliqonMail extends JavaPlugin {
    private static SiliqonMail INSTANCE; {INSTANCE = this;}
    private static final String SPIGOT_RESOURCE_ID = "117366";

    public NamespacedKey customItemKey = new NamespacedKey(this, "siliqonmail-custom-item-for-menus");
    public final String PREFIX = ChatColor.translateAlternateColorCodes('&',"&8&l[&bSiliqon&aMail&r&8&l]&r&f ");

    public FileConfiguration lang;
    private PaperCommandManager commandManager;
    public GUIManager guiManager;

    public static Map<OfflinePlayer, Mailbox> playerMail = new HashMap<>();
    public List<Material> itemBlacklist = new ArrayList<>();

    @Override
    public void onEnable() {
        log("Enabling...");
        // plugin enabled?
        if (!getConfig().getBoolean("plugin-enabled")) {
            logError("Plugin is disabled in config.yml. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // load files
        saveDefaultConfig();
        createLangFile();
        loadItemBlacklist();
        // load data
        YMLStorage.load();
        if (getConfig().getBoolean("auto-save-enabled"))
            getServer().getScheduler().runTaskTimer(this, YMLStorage::save,
                    getConfig().getInt("auto-save-timer")*20L, getConfig().getInt("auto-save-timer")*20L);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        // load command manager
        commandManager = new PaperCommandManager(this);
        registerCommandCompletions();
        registerCommands();
        // setup gui manager and listener
        guiManager = new GUIManager();
        GUIListener guiListener = new GUIListener(guiManager);
        Bukkit.getPluginManager().registerEvents(guiListener, this);
        // create update checker
        new UpdateChecker(this, UpdateCheckSource.SPIGOT, SPIGOT_RESOURCE_ID)
                .setNotifyOpsOnJoin(getConfigBoolean("notify-update"))
                .setNotifyByPermissionOnJoin("siliqonmail.updatecheck")
                .setChangelogLink(SPIGOT_RESOURCE_ID)
                .checkEveryXHours(12)
                .checkNow();
        // done
        log("Enabled successfully.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log("Disabling...");
        YMLStorage.saveAllData(true);
        // done
        log("Successfully disabled.");
    }

    private void registerCommandCompletions() {
        commandManager.getCommandCompletions().registerCompletion("AllPlayers", c -> {
            List<String> nameList = new ArrayList<>();
            for (OfflinePlayer player: Bukkit.getOfflinePlayers()) {
                nameList.add(player.getName());
            }
            return nameList;
        });
    }
    private void registerCommands() {
        commandManager.registerCommand(new MailCommand());
    }

    private void createLangFile() {
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        lang = YamlConfiguration.loadConfiguration(langFile);
    }
    private void loadItemBlacklist() {
        for (String line : getConfig().getStringList("vault-item-blacklist")) {
            try {
                Material item = Material.valueOf(line);
                itemBlacklist.add(item);
            } catch (Exception e) {
                logError("Invalid item found in item blacklist, cannot convert to material. ("+line+')');
            }
        }
    }

    public static void loadPlayerData(OfflinePlayer player) {
        playerMail.put(player, YMLStorage.getPlayerData(player));
    }
    public static void savePlayerData(OfflinePlayer player) {
        YMLStorage.savePlayerData(player, true);
        playerMail.remove(player);
    }

    public static SiliqonMail getInstance() {
        return INSTANCE;
    }
}
