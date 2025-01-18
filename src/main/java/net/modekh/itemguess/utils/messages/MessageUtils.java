package net.modekh.itemguess.utils.messages;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MessageUtils {
    private static RewardMessage message = null;

    public static void sendFeedback(Player player) {
        // chat reward message
        message = RewardMessage.next(message);

        if (message != null)
            ChatUtils.sendServerMessage(player, message.get());

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0F);
    }

    public static RewardMessage getRandom(RewardMessage[] values, int messageOrd) {
        int nextOrd;

        do {
            nextOrd = (int) (Math.random() * (values.length - 1));
        } while (messageOrd == nextOrd);

        return values[nextOrd];
    }
}
