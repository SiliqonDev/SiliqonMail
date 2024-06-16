package com.siliqon.siliqonmail.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.io.IOException;

public interface InventoryHandler {

    void onClick(InventoryClickEvent event);

    void onOpen(InventoryOpenEvent event) throws IOException;

    void onClose(InventoryCloseEvent event);
}
