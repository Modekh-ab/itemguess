package net.modekh.itemguess.utils.messages;

public enum RewardMessage {
    MSG_0("Yeah, take your award!"),
    MSG_3("Wha, more points? Well, ok.."),
    MSG_4("Ba-bakh haha! Oh, you're here... Just take ur points and get out, ask nothing."),
    MSG_5("Well-well-weell... You gonna win, aren't you?"),
    MSG_6("I’m not avoiding you, I’m just here on a higher plane of existence."),
    MSG_7("I’m not insane, my developer had me tested five times."),
    MSG_8("I’m not crying. It’s just liquid pride."),
    MSG_9("I’m not a fan of change. I feel like you just jumped a shark.");

    private final String message;

    RewardMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }

    public static RewardMessage next(RewardMessage rewardMessage) {
        return MessageUtils.getRandom(values(), rewardMessage != null ? rewardMessage.ordinal() : 0);
    }
}
