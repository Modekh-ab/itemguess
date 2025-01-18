package net.modekh.itemguess.utils;

import net.modekh.itemguess.commands.ItemGuessCommand;
import net.modekh.itemguess.utils.messages.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

public class EventUtils {
    public static void addScore(Player player, boolean feedback) {
        if (!ItemGuessCommand.getActivePlayers().contains(player.getUniqueId()))
            return;

        Objective objective = player.getScoreboard().getObjective("itemguess");

        if (objective == null)
            return;

        // scoreboard
        int currentScore = objective.getScore(player.getName()).getScore();
        int newScore = currentScore + Reference.ITEMGUESS_REWARD;

        objective.getScore(player.getDisplayName()).setScore(newScore);

        if (feedback)
            MessageUtils.sendFeedback(player);
    }

    public static void addScore(Player player) {
        addScore(player, true);
    }
}
