package com.siliqon.siliqonmail.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LangFile {

    @Comment("Main menu")
    private String mainMenuTitle = "&aMail System";
    private String checkMailButton = "&aCheck Your Mail";
    private String sendMailButton = "&aSend Someone Mail";
    private String sendMailUsage = "&aUsage: /mail send <player>";

    @Comment({" ","Mail menu"})
    private String mailMenuTitle = "&aYour mail";
    private String noMailItemName = "&cYou don't have any mail!";
    private String mailItemName = "&fMail from {sender}";
    private List<String> mailMenuSkullLore = List.of(" ", "&7You have {amount} mails.");
    private String closeButton = "&cClose Menu";
    private String nextPageButton = "&aNext Page";
    private String previousPageButton = "&aPrevious Page";

    @Comment({" ", "Mails menu"})
    private String mailsMenuTitle = "&fMail From {sender}";
    private String mailsMenuSenderName = "&fSent by {sender}";
    private List<String> mailsMenuSenderLore = List.of(" ", "&7Sent at: {sent_at}");
    private String claimButton = "&aAccept";
    private String deleteButton = "&cDelete";
    private String backButton = "&cGo Back";
    private String notEnoughSpace = "&cYour inventory does not have enough space!";
    private String acceptedMail = "&aMail accepted.";

    @Comment({" ", "Send mail menu"})
    private String sendMailMenuTitle = "&2Send to {recipient}";
    private String sendMail = "&aSend";
    private String mailSent = "&aYou have sent mail to {recipient} successfully!";
    private String cancelSendingMail = "&cCancel";
    private String cancelledSendingMail = "&cCancelled sending mail.";
    private String itemBlacklisted = "&cThis item cannot be sent through mail!";
    private String cantSendEmptyMail = "&cYou cannot send empty mail!";

    @Comment({" ", "Delete mail menu"})
    private String deleteConfirmationMenuTitle = "&4Confirm Deletion";
    private String deleteConfirmationItemName = "&4Confirm?";
    private List<String> deleteConfirmationItemLore = List.of(" ", "&7THIS IS IRREVERSIBLE.");
    private String mailDeleted = "&cDeleted mail successfully.";

    @Comment({" ","Miscellaneous"})
    private String forceSavingData = "&8Force saving all data...";
    private String forceSavedData = "&aSuccessfully saved all data.";
    private String cantMailSelf = "&cYou cannot send mail to yourself!";
    private String recievedMail = "&aYou have recieved some mail from {sender}!";
    private String pendingMail = "&eYou have {amount} unclaimed mail(s).";
    private String pluginVersion = "&aYou are currently running &a{version}";

    public String getMainMenuTitle() {
        return ChatColor.translateAlternateColorCodes('&', mainMenuTitle);
    }

    public String getCheckMailButton() {
        return ChatColor.translateAlternateColorCodes('&', checkMailButton);
    }

    public String getSendMailButton() {
        return ChatColor.translateAlternateColorCodes('&', sendMailButton);
    }

    public String getSendMailUsage() {
        return ChatColor.translateAlternateColorCodes('&', sendMailUsage);
    }

    public String getMailMenuTitle() {
        return ChatColor.translateAlternateColorCodes('&', mailMenuTitle);
    }

    public String getNoMailItemName() {
        return ChatColor.translateAlternateColorCodes('&', noMailItemName);
    }

    public String getMailItemName() {
        return ChatColor.translateAlternateColorCodes('&', mailItemName);
    }

    public List<String> getMailMenuSkullLore() {
        List<String> newList = new ArrayList<>();
        for (String line : mailMenuSkullLore) {
            newList.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return newList;
    }

    public String getCloseButton() {
        return ChatColor.translateAlternateColorCodes('&', closeButton);
    }

    public String getNextPageButton() {
        return ChatColor.translateAlternateColorCodes('&', nextPageButton);
    }

    public String getPreviousPageButton() {
        return ChatColor.translateAlternateColorCodes('&', previousPageButton);
    }

    public String getMailsMenuTitle() {
        return ChatColor.translateAlternateColorCodes('&', mailsMenuTitle);
    }

    public String getMailsMenuSenderName() {
        return ChatColor.translateAlternateColorCodes('&', mailsMenuSenderName);
    }

    public List<String> getMailsMenuSenderLore() {
        List<String> newList = new ArrayList<>();
        for (String line : mailsMenuSenderLore) {
            newList.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return newList;
    }

    public String getClaimButton() {
        return ChatColor.translateAlternateColorCodes('&', claimButton);
    }

    public String getDeleteButton() {
        return ChatColor.translateAlternateColorCodes('&', deleteButton);
    }

    public String getBackButton() {
        return ChatColor.translateAlternateColorCodes('&', backButton);
    }

    public String getNotEnoughSpace() {
        return ChatColor.translateAlternateColorCodes('&', notEnoughSpace);
    }

    public String getAcceptedMail() {
        return ChatColor.translateAlternateColorCodes('&', acceptedMail);
    }

    public String getSendMailMenuTitle() {
        return ChatColor.translateAlternateColorCodes('&', sendMailMenuTitle);
    }

    public String getSendMail() {
        return ChatColor.translateAlternateColorCodes('&', sendMail);
    }

    public String getMailSent() {
        return ChatColor.translateAlternateColorCodes('&', mailSent);
    }

    public String getCancelSendingMail() {
        return ChatColor.translateAlternateColorCodes('&', cancelSendingMail);
    }

    public String getCancelledSendingMail() {
        return ChatColor.translateAlternateColorCodes('&', cancelledSendingMail);
    }

    public String getItemBlacklisted() {
        return ChatColor.translateAlternateColorCodes('&', itemBlacklisted);
    }

    public String getCantSendEmptyMail() {
        return ChatColor.translateAlternateColorCodes('&', cantSendEmptyMail);
    }

    public String getDeleteConfirmationMenuTitle() {
        return ChatColor.translateAlternateColorCodes('&', deleteConfirmationMenuTitle);
    }

    public String getDeleteConfirmationItemName() {
        return ChatColor.translateAlternateColorCodes('&', deleteConfirmationItemName);
    }

    public List<String> getDeleteConfirmationItemLore() {
        List<String> newList = new ArrayList<>();
        for (String line : deleteConfirmationItemLore) {
            newList.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return newList;
    }

    public String getMailDeleted() {
        return ChatColor.translateAlternateColorCodes('&', mailDeleted);
    }

    public String getForceSavingData() {
        return ChatColor.translateAlternateColorCodes('&', forceSavingData);
    }

    public String getForceSavedData() {
        return ChatColor.translateAlternateColorCodes('&', forceSavedData);
    }

    public String getCantMailSelf() {
        return ChatColor.translateAlternateColorCodes('&', cantMailSelf);
    }

    public String getRecievedMail() {
        return ChatColor.translateAlternateColorCodes('&', recievedMail);
    }

    public String getPendingMail() {
        return ChatColor.translateAlternateColorCodes('&', pendingMail);
    }

    public String getPluginVersion() {
        return ChatColor.translateAlternateColorCodes('&', pluginVersion);
    }
}
