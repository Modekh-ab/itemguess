package net.modekh.itemguess;

import net.modekh.itemguess.commands.ItemGuessCommand;
import net.modekh.itemguess.commands.ItemGuessCompleter;
import net.modekh.itemguess.events.EventListener;
import net.modekh.itemguess.handlers.ItemGuessDatabase;
import net.modekh.itemguess.handlers.ItemGuessScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class ItemGuess extends JavaPlugin {
    private ItemGuessDatabase itemguessDatabase;
    private ItemGuessScoreboard itemguessScoreboard;
    private EventListener eventListener;

    @Override
    public void onEnable() {
        // main classes init
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            itemguessDatabase = new ItemGuessDatabase(getDataFolder().getAbsolutePath() + "/itemguess.db");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        itemguessScoreboard = new ItemGuessScoreboard(this);
        eventListener = new EventListener(this);

        final String pluginCommand = "itemguess";

        // commands init
//        getServer().getPluginCommand(pluginCommand).setExecutor(new ItemGuessCommand(this));
        getCommand(pluginCommand).setExecutor(new ItemGuessCommand(this));
        getCommand(pluginCommand).setTabCompleter(new ItemGuessCompleter());

        // base listener
        getServer().getPluginManager().registerEvents(eventListener, this);

        getConfig().set("feedback", true);
        getConfig().set("scoreboard", true);
        getConfig().set("reward", 1);

        this.saveConfig();
    }

    @Override
    public void onDisable() {
        try {
            itemguessDatabase.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ItemGuessDatabase getDatabase() {
        return itemguessDatabase;
    }

    public ItemGuessScoreboard getScoreboard() {
        return itemguessScoreboard;
    }

    public EventListener getListener() {
        return eventListener;
    }

    public boolean isFeedbackEnabled() {
        return this.getConfig().getBoolean("feedback", true);
    }

    public boolean isScoreboardEnabled() {
        return this.getConfig().getBoolean("scoreboard", true);
    }

    public int getReward() {
        return this.getConfig().getInt("reward", 1);
    }
}
