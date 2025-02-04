package com.siliqon.siliqonmail;

import co.aikar.commands.PaperCommandManager;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.siliqon.siliqonmail.commands.MailCommand;
import com.siliqon.siliqonmail.config.LangFile;
import com.siliqon.siliqonmail.config.MainConfig;
import com.siliqon.siliqonmail.data.Mailbox;
import com.siliqon.siliqonmail.data.YMLStorage;
import com.siliqon.siliqonmail.gui.GUIManager;
import com.siliqon.siliqonmail.listeners.GUIListener;
import com.siliqon.siliqonmail.listeners.PlayerListener;
import com.siliqon.siliqonmail.listeners.ServerListener;
import de.exlll.configlib.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;

public final class SiliqonMail extends JavaPlugin {
    private static SiliqonMail INSTANCE; {INSTANCE = this;}
    private static final String SPIGOT_RESOURCE_ID = "117366";
    public final double PLUGIN_VERSION = 1.1;

    public NamespacedKey customItemKey = new NamespacedKey(this, "siliqonmail-custom-item-for-menus");
    public final String PREFIX = ChatColor.translateAlternateColorCodes('&',"&8&l[&bSiliqon&aMail&r&8&l]&r&f ");

    private PaperCommandManager commandManager;
    public GUIManager guiManager;

    public MainConfig config;
    public LangFile lang;
    GUIListener guiListener;

    public static Map<OfflinePlayer, Mailbox> playerMail = new HashMap<>();
    public List<Material> itemBlacklist = new ArrayList<>();

    @Override
    public void onEnable() {
        // init files
        initConfig();
        initLangFile();

        // plugin enabled?
        if (!config.getPluginEnabled()) {
            logError("Plugin is disabled in config.yml. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // load data from files
        loadItemBlacklist();
        retrieveData();

        // setup gui manager
        guiManager = new GUIManager();
        guiListener = new GUIListener(guiManager);

        // register listeners
        registerListeners();

        // register commands
        commandManager = new PaperCommandManager(this);
        registerCommandCompletions();
        registerCommands();

        // check for plugin updates
        new UpdateChecker(this, UpdateCheckSource.SPIGET, SPIGOT_RESOURCE_ID)
                .setNotifyOpsOnJoin(config.getNotifyUpdates())
                .setDownloadLink("https://www.spigotmc.org/resources/"+SPIGOT_RESOURCE_ID)
                .checkEveryXHours(12)
                .checkNow();

        // done
        log("Enabled successfully.");
        getOnlinePlayerData(); // protect against reloads
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        YMLStorage.saveAllData(true);
        // done
        log("Successfully disabled.");
    }

    private void initConfig() {
        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .header(
                    """
                    SiliqonMail v{version}
                    
                    Do not add or remove any keys, only edit them
                    """.replace("{version}", ""+PLUGIN_VERSION)
                )
                .footer("Authors: Siliqon")
                .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
                .setFieldFilter(field -> !field.getName().startsWith("private_"))
                .build();

        Path configFile = new File(getDataFolder(), "config.yml").toPath();
        config = YamlConfigurations.update(
                configFile, MainConfig.class, properties
        );
    }
    private void initLangFile() {
        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .header(
                    """
                    SiliqonMail v{version}

                    All color codes like &a, &d, &5, etc. are valid
                    Do not add or remove any keys, only edit them
                    """.replace("{version}", ""+PLUGIN_VERSION)
                )
                .footer("Authors: Siliqon")
                .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
                .setFieldFilter(field -> !field.getName().startsWith("private_"))
                .build();

        Path langFile = new File(getDataFolder(), "lang.yml").toPath();
        lang = YamlConfigurations.update(
                langFile, LangFile.class, properties
        );
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ServerListener(), this);
        Bukkit.getPluginManager().registerEvents(guiListener, this);
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

    private void loadItemBlacklist() {
        int itr = 0;
        for (String line : config.getBlacklistedItems()) {
            try {
                Material item = Material.valueOf(line);
                itemBlacklist.add(item);
                itr++;
            } catch (Exception e) {
                logError("Invalid item found in item blacklist, cannot convert to material. ("+line+')');
                e.printStackTrace();
            }
        }
        log("Loaded "+itr+" blacklisted items.");
    }

    private void retrieveData() {
        YMLStorage.load();
        // auto save timer
        if (config.getAutoSaveEnabled())
            getServer().getScheduler().runTaskTimer(this, YMLStorage::save,
                    config.getAutoSaveTimer()*20L, config.getAutoSaveTimer()*20L);
    }

    private void getOnlinePlayerData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerData(player);
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
