package com.siliqon.siliqonmail.gui.menus;

import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.data.Mail;
import com.siliqon.siliqonmail.data.Mailbox;
import com.siliqon.siliqonmail.gui.InventoryButton;
import com.siliqon.siliqonmail.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;
import static com.siliqon.siliqonmail.helper.InventoryToBase64.deserializeItemsArray;

public class MailMenu extends InventoryGUI {
    private final Material backgroundMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    int wasOnPage, id, item_amount = 0;
    ItemStack[] items;
    String contents;
    Long sent_at;
    OfflinePlayer sender;
    public MailMenu(Integer wasOnPage, Integer id, Mail mail) {
        this.wasOnPage = wasOnPage;
        this.id = id;
        this.contents = mail.getContents();
        this.sent_at = mail.getSentAt();
        this.sender = mail.getSender();

        createInventory();
    }

    @Override
    protected void createInventory() {
        this.inventory = Bukkit.createInventory(null, 36, getStringFromLang("mail-menu-title").replace("{sender}", sender.getName()));
    }

    @Override
    public void decorate(Player player) {
        // set mail contents if any
        if (contents != null) {
            try {
                items = deserializeItemsArray(contents);
                for (ItemStack item : items) {
                    if (item != null) {
                        item_amount++;
                    }
                }

                getInventory().setContents(items);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // set background
        setMenuBackground(getInventory(), backgroundMaterial, 27, 36, " ");

        // buttons
        addButton(27, backButton());
        addButton(32, claimButton());
        addButton(30, deleteButton());

        // sender info
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (sent_at));
        ItemStack skull = makeSimpleItem(Material.PLAYER_HEAD, getStringFromLang("mail-menu-sender-name").replace("{sender}", sender.getName()), 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(sender);
        List<String> lore = getStringListFromLang("mail-menu-sender-lore");
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&',line
                    .replace("{sent_on}", date)
            ));
        }

        meta.setLore(newLore);
        skull.setItemMeta(meta);
        this.getInventory().setItem(31, skull);

        decorateButtons(player);
    }

    private InventoryButton backButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.ARROW, getStringFromLang("back-button"), 1))
                .consumer(event -> {
                    MailsMenu mailsMenu = new MailsMenu(player);
                    plugin.guiManager.openGUI(mailsMenu, player);
                    mailsMenu.setPage(wasOnPage);
                });
    }
    private InventoryButton claimButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.GREEN_BANNER, getStringFromLang("claim-button"), 1))
                .consumer(event -> {
                    int emptySlots = 0;
                    for (int i = 0; i <= 35; i++) {
                        ItemStack item = player.getInventory().getItem(i);
                        if (item == null || item.getType() == Material.AIR) {
                            emptySlots++;
                        }
                    }

                    // TODO: play sounds for both fail and success
                    if (emptySlots < item_amount) {
                        sendMessage(player, getStringFromLang("not-enough-space"));
                    } else {
                        for (ItemStack item : items) {
                            if (item != null) {
                                player.getInventory().addItem(item);
                            }
                        }

                        // remove mail from cache
                        Mailbox mailbox = plugin.playerMail.get(player);
                        mailbox.deleteMail(id);
                        plugin.playerMail.put(player, mailbox);

                        // reopen mails menu
                        MailsMenu mailsMenu = new MailsMenu(player);
                        plugin.guiManager.openGUI(mailsMenu, player);

                        sendMessage(player, getStringFromLang("accepted-mail"));
                    }

                });
    }

    private InventoryButton deleteButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.BARRIER, getStringFromLang("delete-button"), 1))
                .consumer(event -> {
                    DeleteConfirmation deleteConfirmation = new DeleteConfirmation(id);
                    plugin.guiManager.openGUI(deleteConfirmation, player);
                });
    }
}
