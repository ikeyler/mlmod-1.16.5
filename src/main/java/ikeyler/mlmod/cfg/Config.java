package ikeyler.mlmod.cfg;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;
import java.util.List;

public class Config {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec spec;

    // general
    public static final ForgeConfigSpec.ConfigValue<Boolean> DETECT_MINELAND;
    public static final ForgeConfigSpec.ConfigValue<String> MINELAND_IPS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORED_PLAYERS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CHAT_PLAYER_INTERACT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ADS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MESSAGE_COLLECTOR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PM_NOTIFICATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HIDE_TRANSLATE;
    public static final ForgeConfigSpec.ConfigValue<CHAT_MODE> EXCL_MARK_TO_CHAT;

    // main messages
    public static final ForgeConfigSpec.ConfigValue<Boolean> WELCOME_TO_MINELAND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> REWARD_STORAGE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UNANSWERED_ASKS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UNREAD_MAIL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_VIDEO;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PUNISHMENT_BROADCAST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DONATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PLAYER_VOTED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STREAM;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_ASK;

    // creative
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORED_WORLDS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WORLD_INVITE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DEV_MODE_JOIN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_WORLD_ID;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PLAY_SOUND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SOUND_COMMAND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DEV_NIGHT_MODE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DEV_NIGHT_MODE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_MESSAGE_ADS;

    // chat formatting
    public static final ForgeConfigSpec.ConfigValue<Boolean> CHAT_FORMATTING;
    public static final ForgeConfigSpec.ConfigValue<String> FORMATTING_CC;
    public static final ForgeConfigSpec.ConfigValue<String> FORMATTING_DC;

    public enum CHAT_MODE {
        DC("mlmod.config.option.excl_mark_to_chat.dc"),
        CC("mlmod.config.option.excl_mark_to_chat.cc"),
        OFF("mlmod.config.option.excl_mark_to_chat.off");
        private final String translation;
        CHAT_MODE(String translation) {
            this.translation = translation;
        }
        @Override
        public String toString() {
            return new TranslationTextComponent(this.translation).getString();
        }
    }

    static {
        builder.push("cfg");

        IGNORED_PLAYERS = builder.defineList("ignored_players", Collections.emptyList(), o->o instanceof String);
        IGNORED_WORLDS = builder.defineList("ignored_worlds", Collections.emptyList(), o->o instanceof String);
        CHAT_PLAYER_INTERACT = builder.define("chat_player_interact", false);
        ADS = builder.define("ads", true);
        PM_NOTIFICATION = builder.define("pm_notification", false);
        MESSAGE_COLLECTOR = builder.define("message_collector", false);
        HIDE_TRANSLATE = builder.define("hide_translate", false);
        SHOW_WORLD_ID = builder.define("show_world_id", true);
        EXCL_MARK_TO_CHAT = builder.defineEnum("excl_mark_to_chat", CHAT_MODE.OFF);
        PLAY_SOUND = builder.define("play_sound", false);
        SOUND_COMMAND = builder.define("sound_command", true);
        DEV_NIGHT_MODE = builder.define("dev_night_mode", false);
        DEV_NIGHT_MODE_TIME = builder.define("dev_night_mode_time", 18000);
        SHOW_MESSAGE_ADS = builder.define("show_message_ads", true);

        WELCOME_TO_MINELAND = builder.define("welcome_to_mineland", true);
        DEV_MODE_JOIN = builder.define("dev_mode_join", true);
        REWARD_STORAGE = builder.define("reward_storage", true);
        UNANSWERED_ASKS = builder.define("unanswered_asks", true);
        UNREAD_MAIL = builder.define("unread_mail", true);
        NEW_VIDEO = builder.define("new_video", true);
        WORLD_INVITE = builder.define("world_invite", true);
        PUNISHMENT_BROADCAST = builder.define("punishment_broadcast", true);
        DONATION = builder.define("donation", true);
        PLAYER_VOTED = builder.define("player_voted", true);
        STREAM = builder.define("stream", true);
        NEW_ASK = builder.define("new_ask", true);

        // chat formatting
        CHAT_FORMATTING = builder.define("chat_formatting", false);
        FORMATTING_CC = builder.define("formatting_cc", "&3CC &8|");
        FORMATTING_DC = builder.define("formatting_dc", "&2DC &8|");

        // misc
        DETECT_MINELAND = builder.define("detect_mineland", false);
        MINELAND_IPS = builder.define("mineland_ips", "mineland.net, play-ml.ru");

        builder.pop();
        spec = builder.build();
    }
}