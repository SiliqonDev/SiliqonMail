package com.siliqon.siliqonmail.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.util.List;

@Configuration
public class MainConfig {

    @Comment("Enables or disables the entire plugin")
    private Boolean pluginEnabled = true;
    @Comment("Notify about plugin updates?")
    private Boolean notifyUpdates = true;

    @Comment({" ","automatically save data every so often to protect from crashes?"})
    private Boolean autoSaveEnabled = true;
    @Comment("amount of time to wait between autosaves, in seconds")
    private Integer autoSaveTimer = 300;

    @Comment(" ")
    private Boolean blacklistEnabled = true;
    @Comment("Should the blacklist actually work as a whitelist?")
    private Boolean blacklistIsWhitelist = false;
    private List<String> blacklistedItems = List.of(
            "BEDROCK",
            "END_PORTAL_FRAME"
    );

    public Boolean getPluginEnabled() {
        return pluginEnabled;
    }
    public Boolean getNotifyUpdates() {
        return notifyUpdates;
    }

    public Boolean getAutoSaveEnabled() {
        return autoSaveEnabled;
    }
    public Integer getAutoSaveTimer() {
        return autoSaveTimer;
    }

    public Boolean getBlacklistEnabled() {
        return blacklistEnabled;
    }
    public Boolean getBlacklistIsWhitelist() {
        return blacklistIsWhitelist;
    }
    public List<String> getBlacklistedItems() {
        return blacklistedItems;
    }
}
