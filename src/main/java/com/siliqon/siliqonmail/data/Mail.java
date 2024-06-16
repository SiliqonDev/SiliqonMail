package com.siliqon.siliqonmail.data;

import org.bukkit.OfflinePlayer;

public class Mail {


    OfflinePlayer sender;
    Long sent_at;
    String contents;
    // TODO: note attached

    public Mail(String contents, OfflinePlayer sender, Long sent_at) {
        this.contents = contents;
        this.sender = sender;
        this.sent_at = sent_at;
    }

    public OfflinePlayer getSender() {
        return sender;
    }
    public Long getSentAt() {
        return sent_at;
    }
    public String getContents() {
        return contents;
    }
}
