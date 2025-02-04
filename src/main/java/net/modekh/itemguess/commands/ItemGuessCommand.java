package net.modekh.itemguess.commands;

import net.modekh.itemguess.ItemGuess;
import net.modekh.itemguess.utils.messages.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ItemGuessCommand implements CommandExecutor {
    private static final Set<UUID> activePlayers = new HashSet<>();
    private final ItemGuess main;

    public ItemGuessCommand(ItemGuess main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            return false;

        if (args.length == 0) {
            ChatUtils.sendServerMessage(player, ChatColor.GOLD + "Invalid command!");
            return false;
        }

        // commands without op requirement

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 2) {
                ChatUtils.sendInvalidMessageItem(player);
                return false;
            }

            try {
                String itemId = args[1];

                if (main.getDatabase().addItemToGuess(player, itemId)) {
//                    unsolvedPlayers.add(player.getUniqueId());
                    ChatUtils.sendServerMessage(player, "Your item now is "
                            + ChatUtils.aquaMessage(itemId) + ChatUtils.reset("."));
                    player.getServer().broadcastMessage(
                            ChatUtils.serverMessage(getAquaName(player) + " had just chosen an item to guess!"));

                    return true;
                }

                ChatUtils.sendServerMessage(player, ChatUtils.formatMessage("&6", "Invalid item!"));

                return false;
            } catch (Exception e) {
                ChatUtils.sendInvalidMessageItem(player);
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("guess")) {
            if (args.length != 3) {
                ChatUtils.sendInvalidMessageGuess(player);
                return false;
            }

            try {
                Player opponent = Bukkit.getPlayer(args[1]);
                String itemId = args[2];

                if (opponent == null)
                    return false;

                if (opponent.equals(player)) {
                    ChatUtils.sendServerMessage(player, ChatUtils.formatMessage("&6",
                            "You may have split personality, but for Minecraft "
                                    + getAquaName(player) + " is a single player!"));

                    return false;
                }


                if (main.getDatabase().guessItem(opponent, itemId)) {
                    boolean guessed = main.getDatabase().isItemGuessed(player);

                    if (guessed) {
                        ChatUtils.sendServerMessage(player, "You already guessed " + getAquaName(opponent) + "'s item.");
                        return false;
                    }

                    if (main.isScoreboardEnabled()) {
                        main.getListener().addScore(player);
                    }

                    ChatUtils.sendServerMessage(player, "You guessed "
                            + ChatUtils.aquaMessage(itemId) + ChatUtils.reset("!"));

                    ChatUtils.sendServerMessage(opponent,
                            "Yo, " + getAquaName(player) + " just guessed your item... But shh!");

                    return true;
                }

                ChatUtils.sendServerMessage(player,ChatUtils.formatMessage("&6", "Nope!"));

                return false;
            } catch (Exception e) {
                ChatUtils.sendInvalidMessageGuess(player);
                return false;
            }
        }

        // op commands

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.GOLD + "You must have permission to use this command!");
            return false;
        }

        if (args[0].equalsIgnoreCase("start")) {
            try {
                start();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("stop")) {
            try {
                stop();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("help")) {
            help(player);
            return true;
        }

        return false;
    }

    private void start() throws SQLException {
        Bukkit.getServer().broadcastMessage(
                ChatUtils.serverMessage(ChatUtils.formatMessage("&d", "Tasks game started!")));

        for (Player player : Bukkit.getOnlinePlayers()) {
            main.getDatabase().addPlayerData(player);
            activePlayers.add(player.getUniqueId());

            if (this.main.isScoreboardEnabled()) {
                main.getScoreboard().addPlayerToScoreboard(player);
            }

            player.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        }
    }

    private void stop() throws SQLException {
        main.getDatabase().resetDatabase();
        main.getScoreboard().resetScoreboard();

        activePlayers.clear();

        main.getServer().broadcastMessage(ChatUtils.serverMessage(
                ChatUtils.formatMessage("&d", "Tasks game stopped.")));

        for (Player player : Bukkit.getOnlinePlayers()) {
            // feedback to players
            player.playSound(player, Sound.ENCHANT_THORNS_HIT, 1.0f, 1.0f);
        }
    }

    private void help(Player player) {
        ChatUtils.sendServerMessage(player, ChatUtils.formatMessage("&d", "Commands usage:"));
        sendHelpString(player, "start", "start ItemGuess game");
        sendHelpString(player, "stop", "stop and reset ItemGuess game");
        sendHelpString(player, "set <item>", "set item to guess");
        sendHelpString(player, "guess <player> <item>", "guess someone's item");
    }

    public static Set<UUID> getActivePlayers() {
        return activePlayers;
    }

    private static String getAquaName(Player player) {
        return ChatUtils.aquaMessage(player.getDisplayName()) + ChatColor.RESET;
    }

    private static void sendHelpString(Player player, String command, String explanation) {
        player.sendMessage(ChatUtils.formatMessage("&9", "/" + command) + ChatUtils.formatMessage("&7", " : ")
                + ChatColor.RESET + explanation);
    }
}
