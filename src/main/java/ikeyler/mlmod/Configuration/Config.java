package ikeyler.mlmod.Configuration;

import ikeyler.mlmod.Messages.Messages;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec spec;

    // general
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORED_PLAYERS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CHAT_PLAYER_INTERACT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ADS;

    // main messages
    public static final ForgeConfigSpec.ConfigValue<Boolean> UNANSWERED_ASKS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UNREAD_MAIL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_VIDEO;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PUNISHMENT_BROADCAST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DONATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PLAYER_VOTED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STREAM;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_ASK;

    // creative messages
    public static final ForgeConfigSpec.ConfigValue<Boolean> WORLD_INVITE;

    // message patterns
    public static final ForgeConfigSpec.ConfigValue<String> UNANSWERED_ASKS_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> UNREAD_MAIL_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> WORLD_INVITE_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> NEW_VIDEO_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> PUNISHMENT_BROADCAST_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> DONATION_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> PLAYER_VOTED_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> STREAM_PATTERN;
    public static final ForgeConfigSpec.ConfigValue<String> NEW_ASK_PATTERN;

    static {
        builder.push("cfg");

        IGNORED_PLAYERS = builder.defineList("ignored_players", Collections.singletonList(""), o->true);
        CHAT_PLAYER_INTERACT = builder.define("chat_player_interact", false);
        ADS = builder.define("ads", true);
        UNANSWERED_ASKS = builder.define("unanswered_asks", true);
        UNREAD_MAIL = builder.define("unread_mail", true);
        NEW_VIDEO = builder.define("new_video", true);
        WORLD_INVITE = builder.define("world_invite", true);
        PUNISHMENT_BROADCAST = builder.define("punishment_broadcast", true);
        DONATION = builder.define("donation", true);
        PLAYER_VOTED = builder.define("player_voted", true);
        STREAM = builder.define("stream", true);
        NEW_ASK = builder.define("new_ask", true);

        // patterns
        UNANSWERED_ASKS_PATTERN = builder.define("unanswered_asks_pattern", Messages.UNANSWERED_ASKS.getTemplate());
        UNREAD_MAIL_PATTERN = builder.define("unread_mail_pattern", Messages.UNREAD_MAIL.getTemplate());
        WORLD_INVITE_PATTERN = builder.define("world_invite_pattern", Messages.WORLD_INVITE.getTemplate());
        NEW_VIDEO_PATTERN = builder.define("new_video_pattern", Messages.NEW_VIDEO.getTemplate());
        PUNISHMENT_BROADCAST_PATTERN = builder.define("punishment_broadcast_pattern", Messages.PUNISHMENT_BROADCAST.getTemplate());
        DONATION_PATTERN = builder.define("donation_pattern", Messages.DONATION.getTemplate());
        PLAYER_VOTED_PATTERN = builder.define("player_voted_pattern", Messages.PLAYER_VOTED.getTemplate());
        STREAM_PATTERN = builder.define("stream_pattern", Messages.STREAM.getTemplate());
        NEW_ASK_PATTERN = builder.define("new_ask_pattern", Messages.NEW_ASK.getTemplate());

        builder.pop();
        spec = builder.build();
    }
}