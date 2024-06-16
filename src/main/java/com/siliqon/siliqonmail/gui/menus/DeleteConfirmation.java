package com.siliqon.siliqonmail.gui.menus;

import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.data.Mailbox;
import com.siliqon.siliqonmail.gui.InventoryButton;
import com.siliqon.siliqonmail.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;

public class DeleteConfirmation extends InventoryGUI {

    private static final SiliqonMail plugin = SiliqonMail.getInstance();
    private final Material backgroundMaterial = Material.GRAY_STAINED_GLASS_PANE;

    int id;
    public DeleteConfirmation(int id) {
        this.id = id;

        createInventory();
    }

    @Override
    protected void createInventory() {
        this.inventory = Bukkit.createInventory(null, 27, getStringFromLang("delete-confirmation-menu-title"));
    }

    @Override
    public void decorate(Player player) {
        setMenuBackground(getInventory(), backgroundMaterial, 0, 27, " ");

        addButton(13, deleteButton());

        decorateButtons(player);
    }

    private InventoryButton deleteButton() {
        return new InventoryButton()
                .creator(player -> makeItemWithLore(Material.REDSTONE_BLOCK, getStringFromLang("delete-confirmation-item-name"), 1, getStringListFromLang("delete-confirmation-item-lore")))
                .consumer(event -> {
                    // remove mail from cache
                    Mailbox mailbox = plugin.playerMail.get(player);
                    mailbox.deleteMail(id);
                    plugin.playerMail.put(player, mailbox);

                    // reopen mails menu
                    MailsMenu mailsMenu = new MailsMenu(player);
                    plugin.guiManager.openGUI(mailsMenu, player);

                    // send confirmation message
                    sendMessage(player, getStringFromLang("mail-deleted"));
                });
    }
}
