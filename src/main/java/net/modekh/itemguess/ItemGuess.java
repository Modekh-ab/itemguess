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
        EventListener eventListener = new EventListener();

        final String pluginCommand = "itemguess";

        // commands init
        getServer().getPluginCommand(pluginCommand).setExecutor(new ItemGuessCommand(this));
        getCommand(pluginCommand).setTabCompleter(new ItemGuessCompleter());

        // base listener
        getServer().getPluginManager().registerEvents(eventListener, this);
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
}
