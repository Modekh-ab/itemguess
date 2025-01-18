package net.modekh.itemguess.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class EventListener implements Listener {
    @EventHandler
    private void onTabComplete(TabCompleteEvent event) {
        if (!event.getBuffer().contains(" ")) {
            event.getCompletions().removeIf((string) -> string.contains(":"));
        }
    }
}
