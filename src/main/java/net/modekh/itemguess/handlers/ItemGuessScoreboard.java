package net.modekh.itemguess.handlers;

import net.modekh.itemguess.ItemGuess;
import net.modekh.itemguess.utils.Reference;
import net.modekh.itemguess.utils.messages.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.SQLException;

public class ItemGuessScoreboard {
    private final ItemGuess main;
    private final Scoreboard board;

    public ItemGuessScoreboard(ItemGuess main) {
        this.main = main;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = board.registerNewObjective("itemguess", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void addPlayerToScoreboard(Player player) throws SQLException {
        Objective objective = board.getObjective("itemguess");

        if (objective == null)
            return;

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.setDisplayName(ChatUtils.formatMessage("&l&a", "Score"));
        player.setScoreboard(board);
//        updateScore(player);
        objective.getScore(player.getName()).setScore(0);
    }
    
    public void updateScore(Player player) {
        Objective objective = board.getObjective("itemguess");

        if (objective == null)
            return;

        int newScore = main.getDatabase().getPlayerScore(player) + Reference.ITEMGUESS_REWARD;
        Score score = objective.getScore(player.getName());
        score.setScore(newScore);
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
