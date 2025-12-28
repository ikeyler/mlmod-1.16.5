package ikeyler.mlmod.cfg;

import ikeyler.mlmod.messages.Messages;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static ikeyler.mlmod.Main.messageManager;

public class ClothConfig {
    public static Screen buildConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslationTextComponent("mlmod.config"));
        ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.general"));
        ConfigCategory general_messages = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.messages"));
        ConfigCategory creative = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.creative"));
        ConfigCategory chat_formatting = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.chat_formatting"));
        ConfigCategory misc = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.misc"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.transparentBackground();

        // general
        TextListEntry general_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.general.tooltip")).build();
        StringListListEntry ignored_players = entryBuilder.startStrList(new TranslationTextComponent("mlmod.config.option.ignored_players"),
                new ArrayList<>(Config.IGNORED_PLAYERS.get()))
                .setTooltip(new TranslationTextComponent("mlmod.config.option.ignored_players.tooltip"))
                .setDefaultValue(Collections.emptyList())
                .setSaveConsumer(Config.IGNORED_PLAYERS::set)
                .build();
        BooleanListEntry chat_player_interact = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.chat_player_interact"), Config.CHAT_PLAYER_INTERACT.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.chat_player_interact.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.CHAT_PLAYER_INTERACT::set)
                .build();
        BooleanListEntry ads = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.ads"), Config.ADS.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.ads.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.ADS::set)
                .build();
        BooleanListEntry pm_notification = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.pm_notification"), Config.PM_NOTIFICATION.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.pm_notification.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.PM_NOTIFICATION::set)
                .build();
        BooleanListEntry message_collector = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.message_collector"), Config.MESSAGE_COLLECTOR.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.message_collector.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.MESSAGE_COLLECTOR::set)
                .build();
        BooleanListEntry hide_translate = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.hide_translate"), Config.HIDE_TRANSLATE.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.hide_translate.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.HIDE_TRANSLATE::set)
                .build();
        EnumListEntry<Config.CHAT_MODE> excl_mark_to_chat = entryBuilder.startEnumSelector(new TranslationTextComponent("mlmod.config.option.excl_mark_to_chat"), Config.CHAT_MODE.class, Config.EXCL_MARK_TO_CHAT.get())
                .setDefaultValue(Config.CHAT_MODE.OFF)
                .setSaveConsumer(Config.EXCL_MARK_TO_CHAT::set)
                .build();
        BooleanListEntry messages_in_actionbar = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.messages_in_actionbar"), Config.MESSAGES_IN_ACTIONBAR.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.messages_in_actionbar.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.MESSAGES_IN_ACTIONBAR::set)
                .build();

        // main messages
        TextListEntry general_messages_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.messages.tooltip")).build();
        BooleanListEntry reward_storage = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.reward_storage"), Config.REWARD_STORAGE.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.REWARD_STORAGE::set)
                .build();
        BooleanListEntry welcome_to_mineland = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.welcome_to_mineland"), Config.WELCOME_TO_MINELAND.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.WELCOME_TO_MINELAND::set)
                .build();
        BooleanListEntry unanswered_asks = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.unanswered_asks"), Config.UNANSWERED_ASKS.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.unanswered_asks.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.UNANSWERED_ASKS::set)
                .build();
        BooleanListEntry unread_mail = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.unread_mail"), Config.UNREAD_MAIL.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.unread_mail.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.UNREAD_MAIL::set)
                .build();
        BooleanListEntry new_video = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.new_video"), Config.NEW_VIDEO.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.new_video.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.NEW_VIDEO::set)
                .build();
        BooleanListEntry punishment_broadcast = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.punishment_broadcast"), Config.PUNISHMENT_BROADCAST.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.punishment_broadcast.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.PUNISHMENT_BROADCAST::set)
                .build();
        BooleanListEntry donation = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.donation"), Config.DONATION.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.donation.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.DONATION::set)
                .build();
        BooleanListEntry player_voted = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.player_voted"), Config.PLAYER_VOTED.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.PLAYER_VOTED::set)
                .build();
        BooleanListEntry stream = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.stream"), Config.STREAM.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.STREAM::set)
                .build();
        BooleanListEntry new_ask = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.new_ask"), Config.NEW_ASK.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.NEW_ASK::set)
                .build();
        BooleanListEntry login_check = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.login_check"), Config.LOGIN_CHECK.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.login_check.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.LOGIN_CHECK::set)
                .build();

        // creative
        TextListEntry creative_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.creative.tooltip")).build();
        StringListListEntry ignored_worlds = entryBuilder.startStrList(new TranslationTextComponent("mlmod.config.option.ignored_worlds"),
                new ArrayList<>(Config.IGNORED_WORLDS.get()))
                .setTooltip(new TranslationTextComponent("mlmod.config.option.ignored_worlds.tooltip"))
                .setDefaultValue(Collections.emptyList())
                .setSaveConsumer(Config.IGNORED_WORLDS::set)
                .build();
        BooleanListEntry world_invite = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.world_invite"), Config.WORLD_INVITE.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.WORLD_INVITE::set)
                .build();
        BooleanListEntry dev_mode_join = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.dev_mode_join"), Config.DEV_MODE_JOIN.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.DEV_MODE_JOIN::set)
                .build();
        BooleanListEntry show_world_id = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.show_world_id"), Config.SHOW_WORLD_ID.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.show_world_id.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.SHOW_WORLD_ID::set)
                .build();
        BooleanListEntry play_sound = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.play_sound"), Config.PLAY_SOUND.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.play_sound.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.PLAY_SOUND::set)
                .build();
        BooleanListEntry sound_command = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.sound_command"), Config.SOUND_COMMAND.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.sound_command.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.SOUND_COMMAND::set)
                .build();
        BooleanListEntry dev_night_mode = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.dev_night_mode"), Config.DEV_NIGHT_MODE.get())
                .setDefaultValue(false)
                .setSaveConsumer(Config.DEV_NIGHT_MODE::set)
                .build();
        IntegerListEntry dev_night_mode_time = entryBuilder.startIntField(new TranslationTextComponent("mlmod.config.option.dev_night_mode_time"), Config.DEV_NIGHT_MODE_TIME.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.dev_night_mode_time.tooltip"))
                .setDefaultValue(18000)
                .setSaveConsumer(Config.DEV_NIGHT_MODE_TIME::set)
                .build();
        BooleanListEntry show_message_ads = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.show_message_ads"), Config.SHOW_MESSAGE_ADS.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.SHOW_MESSAGE_ADS::set)
                .build();

        // formatting
        BooleanListEntry formatting = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.chat_formatting"), Config.CHAT_FORMATTING.get())
                .setDefaultValue(false)
                .setSaveConsumer(Config.CHAT_FORMATTING::set)
                .build();
        StringListEntry formatting_cc = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.chat_formatting_cc"), Config.FORMATTING_CC.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.chat_formatting_cc.tooltip"))
                .setDefaultValue("&3CC &8|")
                .setSaveConsumer(Config.FORMATTING_CC::set)
                .build();
        StringListEntry formatting_dc = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.chat_formatting_dc"), Config.FORMATTING_DC.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.chat_formatting_dc.tooltip"))
                .setDefaultValue("&2DC &8|")
                .setSaveConsumer(Config.FORMATTING_DC::set)
                .build();

        // misc
        BooleanListEntry detect_mineland = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.detect_mineland"), Config.DETECT_MINELAND.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.detect_mineland.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.DETECT_MINELAND::set)
                .build();
        StringListEntry mineland_ips = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.mineland_ips"), Config.MINELAND_IPS.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.mineland_ips.tooltip"))
                .setDefaultValue("mineland.net, play-ml.ru")
                .setSaveConsumer(Config.MINELAND_IPS::set)
                .build();
        StringListListEntry command_aliases = entryBuilder.startStrList(new TranslationTextComponent("mlmod.config.option.command_aliases"),
                        new ArrayList<>(Config.COMMAND_ALIASES.get()))
                .setTooltip(new TranslationTextComponent("mlmod.config.option.command_aliases.tooltip"))
                .setDefaultValue(new ArrayList<>(Arrays.asList("кк:креативкоины", "з:золото")))
                .setSaveConsumer(Config.COMMAND_ALIASES::set)
                .build();

        misc.addEntry(detect_mineland).addEntry(mineland_ips).addEntry(command_aliases);

        general.addEntry(general_description).addEntry(ignored_players).addEntry(chat_player_interact).addEntry(ads)
                .addEntry(pm_notification).addEntry(message_collector).addEntry(hide_translate).addEntry(excl_mark_to_chat).addEntry(messages_in_actionbar);

        general_messages.addEntry(general_messages_description).addEntry(reward_storage).addEntry(welcome_to_mineland).addEntry(unanswered_asks).addEntry(unread_mail)
                .addEntry(new_video).addEntry(punishment_broadcast).addEntry(donation).addEntry(player_voted)
                .addEntry(stream).addEntry(new_ask).addEntry(login_check);

        creative.addEntry(creative_description).addEntry(ignored_worlds).addEntry(world_invite).addEntry(show_world_id).addEntry(dev_mode_join)
                .addEntry(play_sound).addEntry(sound_command).addEntry(dev_night_mode).addEntry(dev_night_mode_time).addEntry(show_message_ads);

        chat_formatting.addEntry(formatting).addEntry(formatting_cc).addEntry(formatting_dc);
        builder.setSavingRunnable(() -> {
            Config.spec.save();
            messageManager.update();
            Messages.updateMessages();
        });
        return builder.build();
    }
}