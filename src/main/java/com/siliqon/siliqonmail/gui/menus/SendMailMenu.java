package com.siliqon.siliqonmail.gui.menus;

import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.data.Mail;
import com.siliqon.siliqonmail.data.Mailbox;
import com.siliqon.siliqonmail.gui.InventoryButton;
import com.siliqon.siliqonmail.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

import static com.siliqon.siliqonmail.SiliqonMail.loadPlayerData;
import static com.siliqon.siliqonmail.SiliqonMail.savePlayerData;
import static com.siliqon.siliqonmail.helper.GeneralUtils.*;
import static com.siliqon.siliqonmail.helper.InventoryToBase64.serializeItemsArray;

public class SendMailMenu extends InventoryGUI {
    private final Material backgroundMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    OfflinePlayer recipient;
    boolean success = false;
    public SendMailMenu(OfflinePlayer recipient) {
        this.recipient = recipient;

        createInventory();
    }

    @Override
    protected void createInventory() {
        this.inventory = Bukkit.createInventory(null, 36, plugin.lang.getSendMailMenuTitle().replace("{recipient}", recipient.getName()));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        int slot = event.getSlot();

        // is it a button
        if (item.getItemMeta().getPersistentDataContainer().has(plugin.customItemKey, PersistentDataType.BOOLEAN)) {
            event.setCancelled(true);
            InventoryButton button = this.buttonMap.get(slot);
            if (button != null) {
                button.getEventConsumer().accept(event);
            }
            return;
        }

        // is it blacklisted
        if (plugin.config.getBlacklistEnabled()) {
            if (plugin.config.getBlacklistIsWhitelist() && (!plugin.itemBlacklist.contains(item.getType()))) {
                sendMessage(player, plugin.lang.getItemBlacklisted());
                event.setCancelled(true);
            } else if (plugin.itemBlacklist.contains(item.getType())) {
                sendMessage(player, plugin.lang.getItemBlacklisted());
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (success) return;

        // clear button items
        for (int i = 27; i < 36; i++) {
            getInventory().clear(i);
        }

        ArrayList<ItemStack> toGive = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack item = player.getOpenInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                toGive.add(item);
            }
        }
        // give back all items
        for (ItemStack giveBack : toGive) {
            if (player.getInventory().firstEmpty() >= 0) {
                player.getInventory().addItem(giveBack);
            } else {
                player.getWorld().dropItem(player.getLocation(), giveBack);
            }
        }
    }

    @Override
    public void decorate(Player player) {
        // set background
        setMenuBackground(getInventory(), backgroundMaterial, 27, 36, " ");

        addButton(30, cancelButton());
        addButton(32, sendButton());

        decorateButtons(player);
    }

    private InventoryButton sendButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.GREEN_BANNER, plugin.lang.getSendMail(), 1))
                .consumer(event -> {
                    int itemCount = 0;
                    for (int i = 0; i < 27; i++) {
                        ItemStack item = getInventory().getItem(i);
                        if (item != null) {
                            itemCount++;
                        }
                    }
                    if (itemCount == 0) {
                        sendMessage(player, plugin.lang.getCantSendEmptyMail());
                        return;
                    }

                    for (int i = 27; i < 36; i++) {
                        getInventory().clear(i);
                    }
                    String finalContents = serializeItemsArray(getInventory().getContents());

                    if (!recipient.isOnline()) {
                        loadPlayerData(recipient);
                    }
                    Mail mail = new Mail(finalContents, player, System.currentTimeMillis());

                    Mailbox newMail = plugin.playerMail.get(recipient);
                    newMail.sendMail(mail);
                    plugin.playerMail.put(recipient, newMail);

                    if (!recipient.isOnline()) {
                        savePlayerData(recipient);
                    } else {
                        sendMessage((Player) recipient, plugin.lang.getRecievedMail()
                                .replace("{sender}", player.getDisplayName()));
                    }

                    success = true;
                    player.closeInventory();
                    sendMessage(player, plugin.lang.getMailSent().replace("{recipient}", recipient.getName()));
                });
    }

    private InventoryButton cancelButton() {
        return new InventoryButton()
                .creator(player -> makeSimpleItem(Material.BARRIER, plugin.lang.getCancelSendingMail(), 1))
                .consumer(event -> {
                   player.closeInventory();
                   sendMessage(player, plugin.lang.getCancelledSendingMail());
                });
    }
}
