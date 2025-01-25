package com.siliqon.siliqonmail.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.siliqon.siliqonmail.SiliqonMail;
import com.siliqon.siliqonmail.data.YMLStorage;
import com.siliqon.siliqonmail.gui.menus.MainMenu;
import com.siliqon.siliqonmail.gui.menus.SendMailMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static com.siliqon.siliqonmail.helper.GeneralUtils.*;

@CommandAlias("mail|siliqonmail|mails")
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
            sendMessage(player, plugin.lang.getCantMailSelf());
            return;
        }

        SendMailMenu sendMailMenu = new SendMailMenu(target);
        plugin.guiManager.openGUI(sendMailMenu, player);
    }

    @Subcommand("save")
    @CommandPermission("siliqonmail.save")
    public static void forceSave(Player player) {
        sendMessage(player, plugin.lang.getForceSavingData());
        YMLStorage.saveAllData(true);
        sendMessage(player, plugin.lang.getForceSavedData());
    }

    @Subcommand("version")
    @CommandPermission("siliqonmail.version")
    public static void showVersion(Player player) {
        sendMessage(player, plugin.lang.getPluginVersion().replace("{version}", "SiliqonMail | v"+plugin.PLUGIN_VERSION));
    }
}
