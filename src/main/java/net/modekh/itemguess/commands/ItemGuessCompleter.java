package net.modekh.itemguess.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemGuessCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("start");
            commands.add("stop");
            commands.add("set");
            commands.add("guess");
            commands.add("help");

            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                for (Material item : Material.values()) {
                    commands.add(item.name().toLowerCase());
                }
            }

            if (args[0].equalsIgnoreCase("guess")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.getDisplayName().equals(sender.getName())) {
                        commands.add(player.getName());
                    }
                }
            }

            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length == 3) {
            for (Material item : Material.values()) {
                commands.add(item.name().toLowerCase());
            }

            StringUtil.copyPartialMatches(args[2], commands, completions);
        }

//        Collections.sort(completions);
        return completions;
    }
}
