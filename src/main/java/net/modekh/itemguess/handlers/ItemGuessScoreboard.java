package net.modekh.itemguess.handlers;

import net.modekh.itemguess.ItemGuess;
import net.modekh.itemguess.utils.messages.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.SQLException;

public class ItemGuessScoreboard {
    private Scoreboard board;

    public ItemGuessScoreboard(ItemGuess main) {
        if (main.isScoreboardEnabled()) {
            this.board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective("itemguess", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }

    public void addPlayerToScoreboard(Player player) throws SQLException {
        Objective objective = board.getObjective("itemguess");

        if (objective == null)
            return;

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.setDisplayName(ChatUtils.formatMessage("&l&a", "Score"));
        player.setScoreboard(board);
        objective.getScore(player.getName()).setScore(0);
    }

    public void resetScoreboard() {
        board.clearSlot(DisplaySlot.SIDEBAR);

        for (Objective objective : board.getObjectives()) {
            objective.setDisplayName("");
        }

        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }
    }
}
