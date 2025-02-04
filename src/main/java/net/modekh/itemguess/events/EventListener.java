package net.modekh.itemguess.events;

import net.modekh.itemguess.ItemGuess;
import net.modekh.itemguess.commands.ItemGuessCommand;
import net.modekh.itemguess.utils.messages.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scoreboard.Objective;

public class EventListener implements Listener {
    private final ItemGuess main;

    public EventListener(ItemGuess main) {
        this.main = main;
    }

    @EventHandler
    private void onLevelJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    private void onTabComplete(TabCompleteEvent event) {
        if (!event.getBuffer().contains(" ")) {
            event.getCompletions().removeIf((string) -> string.contains(":"));
        }
    }

    public void addScore(Player player) {
        if (!ItemGuessCommand.getActivePlayers().contains(player.getUniqueId()))
            return;

        Objective objective = player.getScoreboard().getObjective("itemguess");

        if (objective == null)
            return;

        // scoreboard
        int currentScore = objective.getScore(player.getName()).getScore();
        int newScore = currentScore + this.main.getReward();

        objective.getScore(player.getDisplayName()).setScore(newScore);

        if (this.main.isFeedbackEnabled())
            MessageUtils.sendFeedback(player);
    }
}
