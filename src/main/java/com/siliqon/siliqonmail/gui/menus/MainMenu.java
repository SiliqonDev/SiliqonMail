package com.siliqon.siliqonmail.gui.menus;

import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.gui.InventoryButton;
import com.siliqon.siliqonmail.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;

public class MainMenu extends InventoryGUI {
    private final Material backgroundMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    public MainMenu() {
        createInventory();
    }

    @Override
    protected void createInventory() {
        this.inventory = Bukkit.createInventory(null, 27, plugin.lang.getMainMenuTitle());
    }

    @Override
    public void decorate(Player player) {
        // set background
        setMenuBackground(getInventory(), backgroundMaterial, 0, 27, " ");

        // buttons
        addButton(11, checkMailButton());
        addButton(15, sendMailButton());

        // player skull
        ItemStack skull = makeSimpleItem(Material.PLAYER_HEAD, player.getDisplayName(), 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        this.getInventory().setItem(4, skull);

        decorateButtons(player);
    }

    private InventoryButton checkMailButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.CHEST, plugin.lang.getCheckMailButton(), 1))
                .consumer(event -> {
                    MailMenu mailsMenu = new MailMenu(player);
                    plugin.guiManager.openGUI(mailsMenu, player);
                });
    }

    private InventoryButton sendMailButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.PAPER, plugin.lang.getSendMailButton(), 1))
                .consumer(event -> {
                    player.closeInventory();
                    sendMessage(player, plugin.lang.getSendMailUsage());
                });
    }
}
