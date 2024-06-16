package com.siliqon.siliqonmail.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class InventoryGUI implements InventoryHandler {

    protected Inventory inventory;
    protected Player player;
    protected final Map<Integer, InventoryButton> buttonMap = new HashMap<>();

    public Inventory getInventory() {
        return this.inventory;
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }
    public void clearButtons(boolean clearItems) {
        if(clearItems) this.buttonMap.forEach((slot, button) -> this.inventory.clear(slot));
        this.buttonMap.clear();
    }

    public void decorate(Player player) throws IOException {
        this.buttonMap.forEach((slot, button) -> {
            ItemStack icon = button.getIconCreator().apply(player);
            this.inventory.setItem(slot, icon);
        });
    }
    public void decorateButtons(Player player) {
        this.buttonMap.forEach((slot, button) -> {
            ItemStack icon = button.getIconCreator().apply(player);
            this.inventory.setItem(slot, icon);
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if (button != null) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) throws IOException {
        this.player = (Player) event.getPlayer();
        this.decorate(this.player);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {}

    protected abstract void createInventory();
}
