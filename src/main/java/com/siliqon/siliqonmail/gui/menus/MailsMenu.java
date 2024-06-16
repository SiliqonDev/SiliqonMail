package com.siliqon.siliqonmail.gui.menus;

import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.data.Mail;
import com.siliqon.siliqonmail.data.Mailbox;
import com.siliqon.siliqonmail.gui.InventoryButton;
import com.siliqon.siliqonmail.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;

public class MailsMenu extends InventoryGUI {
    private final Material backgroundMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    Mailbox mailbox;
    List<Mail> playerMail;
    int page = 1;
    public MailsMenu(Player player) {
        this.mailbox = plugin.playerMail.get(player);
        this.playerMail = mailbox.getMail();
        createInventory();
    }

    public void setPage(int page) {
        this.page = page;
        decorate(player);
    }

    @Override
    protected void createInventory() {
        this.inventory = Bukkit.createInventory(null, 45, getStringFromLang("mails-menu-title"));
    }

    @Override
    public void decorate(Player player) {
        // set background
        setMenuBackground(getInventory(), backgroundMaterial, 0, 9, " ");
        setMenuBackground(getInventory(), backgroundMaterial, 36, 45, " ");

        // place mail buttons
        int start = (27*(page-1));
        int slot = 9;

        for (int i = start; i < Integer.min(playerMail.size(), 27*page); i++) {
            Mail mail = playerMail.get(i);
            this.addButton(slot, openMailButton(i, mail));
            slot++;
        }

        // no mail item
        if (slot == 9) {
            this.getInventory().setItem(22, makeSimpleItem(Material.REDSTONE_BLOCK, getStringFromLang("no-mail-item-name"), 1));
        }

        // next page button
        if (playerMail.size() > 27*page) {
            this.addButton(43, nextPageButton());
        }

        // previous page button
        if (page > 1) {
            this.addButton(37, prevPageButton());
        }

        // close menu button
        this.addButton(40, closeButton());

        // player info item
        ItemStack skull = makeSimpleItem(Material.PLAYER_HEAD, ChatColor.WHITE+player.getDisplayName(), 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        List<String> lore = getStringListFromLang("mails-menu-skull-lore");
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&',line
                    .replace("{amount}", String.valueOf(playerMail.size()))
                    ));
        }

        meta.setLore(newLore);
        skull.setItemMeta(meta);
        this.getInventory().setItem(4, skull);

        decorateButtons(player);
    }

    private InventoryButton openMailButton(Integer id, Mail mail) {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.CHEST, getStringFromLang("mail-item-name").replace("{sender}", mail.getSender().getName()), 1))
                .consumer(event -> {
                    MailMenu mailMenu = new MailMenu(page, id, mail);
                    plugin.guiManager.openGUI(mailMenu, player);
                });
    }

    private InventoryButton closeButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.BARRIER, getStringFromLang("close-button"), 1))
                .consumer(event -> player.closeInventory());
    }

    private InventoryButton nextPageButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.ARROW, getStringFromLang("next-page-button"), 1))
                .consumer(event -> setPage(page+1));
    }
    private InventoryButton prevPageButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.ARROW, getStringFromLang("previous-page-button"), 1))
                .consumer(event -> setPage(page-1));
    }
}
