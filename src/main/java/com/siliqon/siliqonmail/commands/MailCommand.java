package com.siliqon.siliqonmail.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.data.YMLStorage;
import com.siliqon.siliqonmail.gui.menus.MainMenu;
import com.siliqon.siliqonmail.gui.menus.SendMailMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static com.siliqon.siliqonmail.helper.GeneralUtils.getStringFromLang;
import static com.siliqon.siliqonmail.helper.GeneralUtils.sendMessage;

@CommandAlias("mail")
@CommandPermission("siliqonmail.main")
public class MailCommand extends BaseCommand {
    private static final SiliqonMail plugin = SiliqonMail.getInstance();

    @Default
    public static void openMailMenu(Player player) {
        MainMenu mailingMenu = new MainMenu();
        plugin.guiManager.openGUI(mailingMenu, player);
    }

    @Subcommand("send")
    @CommandPermission("siliqonmail.send")
    @Syntax("<player>")
    @CommandCompletion("@AllPlayers")
    public static void sendMail(Player player, OfflinePlayer target) {
        if (player == target) {
            sendMessage(player, getStringFromLang("cant-mail-self"));
            return;
        }

        SendMailMenu sendMailMenu = new SendMailMenu(target);
        plugin.guiManager.openGUI(sendMailMenu, player);
    }

    @Subcommand("save")
    @CommandPermission("siliqonmail.save")
    public static void forceSave(Player player) {
        sendMessage(player, getStringFromLang("force-saving-data"));
        YMLStorage.saveAllData(true);
        sendMessage(player, getStringFromLang("force-saved-data"));
    }
}
