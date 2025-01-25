package com.siliqon.siliqonmail.helper;

import com.siliqon.siliqonmail.SiliqonMail;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class GeneralUtils {
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    public static void log(String message) {
        plugin.getLogger().info(message);
    }
    public static void logError(String message) {
        plugin.getLogger().severe(message);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(plugin.PREFIX + message);
    }

    public static void setMenuBackground(Inventory inv, Material material, int start, int stop, String displayName) {
        for (int i = start; i < stop; i++) {
            inv.setItem(i, makeSimpleItem(material, displayName, 1));
        }
    }

    public static ItemStack makeSimpleItem(Material material, String displayName, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName);
        meta.getPersistentDataContainer().set(plugin.customItemKey, PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeItemWithLore(Material material, String displayName, int amount, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(plugin.customItemKey, PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);

        return item;
    }
}
