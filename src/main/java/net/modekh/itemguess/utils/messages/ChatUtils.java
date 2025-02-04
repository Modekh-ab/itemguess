package net.modekh.itemguess.utils.messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
    public static void sendInvalidMessageItem(Player player) {
        ChatUtils.sendServerMessage(player, ChatColor.GOLD + "Invalid command!");
        ChatUtils.sendServerMessage(player, "Usage: "
                + ChatUtils.formatMessage("&3", "\"/itemguess set ") + ChatUtils.aquaMessage("<item>")
                + ChatUtils.formatMessage("&3", "\""));
    }

    public static void sendInvalidMessageGuess(Player player) {
        ChatUtils.sendServerMessage(player, ChatColor.GOLD + "Invalid command!");
        ChatUtils.sendServerMessage(player, "Usage: "
                + ChatUtils.formatMessage("&3", "\"/itemguess guess ")
                + ChatUtils.aquaMessage("<opponent> <item>")
                + ChatUtils.formatMessage("&3", "\""));
    }

    public static void sendServerMessage(Player player, String message) {
        player.sendMessage(serverMessage(message));
    }

    public static String serverMessage(String message) {
        return messageSender() + message;
    }

    public static String aquaMessage(String message) {
        return ChatColor.AQUA + message;
    }

    private static String messageSender() {
        return ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "ItemGuess Server" + ChatColor.DARK_GREEN + "] " + ChatColor.RESET;
    }

    public static String formatMessage(String formatChar, String message) {
        return format(formatChar) + message;
    }

    private static String format(String formatChar) {
        return ChatColor.translateAlternateColorCodes('&', formatChar);
    }

    public static String reset(String message) {
        return ChatColor.RESET + message;
    }
}
