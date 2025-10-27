package ikeyler.mlmod.cfg;

import ikeyler.mlmod.messages.Messages;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClothConfig {
    public static Screen buildConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslationTextComponent("mlmod.config"));
        ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.general"));
        ConfigCategory general_messages = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.messages"));
        ConfigCategory patterns = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.patterns"));
        ConfigCategory creative_messages = builder.getOrCreateCategory(new TranslationTextComponent("mlmod.config.category.messages.creative"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // general
        TextListEntry general_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.general.desc")).build();
        List<String> ignoredPlayers = new ArrayList<>(Config.IGNORED_PLAYERS.get());
        StringListListEntry ignored_players = entryBuilder.startStrList(new TranslationTextComponent("mlmod.config.option.ignored_players"),
                ignoredPlayers)
                .setTooltip(new TranslationTextComponent("mlmod.config.option.ignored_players.desc"))
                .setDefaultValue(Collections.emptyList())
                .setSaveConsumer(Config.IGNORED_PLAYERS::set)
                .build();
        BooleanListEntry chat_player_interact = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.chat_player_interact"), Config.CHAT_PLAYER_INTERACT.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.chat_player_interact.desc"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.CHAT_PLAYER_INTERACT::set)
                .build();
        BooleanListEntry ads = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.ads"), Config.ADS.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.ads.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.ADS::set)
                .build();
        BooleanListEntry pm_notification = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.pm_notification"), Config.PM_NOTIFICATION.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.pm_notification.desc"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.PM_NOTIFICATION::set)
                .build();
        BooleanListEntry message_collector = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.message_collector"), Config.MESSAGE_COLLECTOR.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.message_collector.desc"))
                .setDefaultValue(false)
                .setSaveConsumer(Config.MESSAGE_COLLECTOR::set)
                .build();

        // main messages
        TextListEntry general_messages_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.messages.desc")).build();
        BooleanListEntry reward_storage = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.reward_storage"), Config.REWARD_STORAGE.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.REWARD_STORAGE::set)
                .build();
        BooleanListEntry welcome_to_mineland = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.welcome_to_mineland"), Config.WELCOME_TO_MINELAND.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.WELCOME_TO_MINELAND::set)
                .build();
        BooleanListEntry unanswered_asks = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.unanswered_asks"), Config.UNANSWERED_ASKS.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.unanswered_asks.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.UNANSWERED_ASKS::set)
                .build();
        BooleanListEntry unread_mail = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.unread_mail"), Config.UNREAD_MAIL.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.unread_mail.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.UNREAD_MAIL::set)
                .build();
        BooleanListEntry new_video = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.new_video"), Config.NEW_VIDEO.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.new_video.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.NEW_VIDEO::set)
                .build();
        BooleanListEntry punishment_broadcast = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.punishment_broadcast"), Config.PUNISHMENT_BROADCAST.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.punishment_broadcast.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.PUNISHMENT_BROADCAST::set)
                .build();
        BooleanListEntry donation = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.donation"), Config.DONATION.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.donation.desc"))
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

        // creative
        TextListEntry creative_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.messages.creative.desc")).build();
        BooleanListEntry world_invite = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.world_invite"), Config.WORLD_INVITE.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.WORLD_INVITE::set)
                .build();
        BooleanListEntry dev_mode_join = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.dev_mode_join"), Config.DEV_MODE_JOIN.get())
                .setDefaultValue(true)
                .setSaveConsumer(Config.DEV_MODE_JOIN::set)
                .build();
        BooleanListEntry show_world_id = entryBuilder.startBooleanToggle(new TranslationTextComponent("mlmod.config.option.show_world_id"), Config.SHOW_WORLD_ID.get())
                .setTooltip(new TranslationTextComponent("mlmod.config.option.show_world_id.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(Config.SHOW_WORLD_ID::set)
                .build();

        // patterns
        TextListEntry patterns_description = entryBuilder.startTextDescription(new TranslationTextComponent("mlmod.config.category.patterns.desc")).build();
        StringListEntry reward_storage_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.reward_storage"), Config.REWARD_STORAGE_PATTERN.get())
                .setDefaultValue(Messages.REWARD_STORAGE.getFixedTemplate())
                .setSaveConsumer(value -> {Config.REWARD_STORAGE_PATTERN.set(value); Messages.REWARD_STORAGE.setNewTemplate(value);})
                .build();
        StringListEntry welcome_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.welcome_to_mineland"), Config.WELCOME_PATTERN.get())
                .setDefaultValue(Messages.WELCOME_TO_MINELAND.getFixedTemplate())
                .setSaveConsumer(value -> {Config.WELCOME_PATTERN.set(value); Messages.WELCOME_TO_MINELAND.setNewTemplate(value);})
                .build();
        StringListEntry dev_mode_join_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.dev_mode_join"), Config.DEV_MODE_JOIN_PATTERN.get())
                .setDefaultValue(Messages.DEV_MODE_JOIN.getFixedTemplate())
                .setSaveConsumer(value -> {Config.DEV_MODE_JOIN_PATTERN.set(value); Messages.DEV_MODE_JOIN.setNewTemplate(value);})
                .build();
        StringListEntry unanswered_asks_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.unanswered_asks"), Config.UNANSWERED_ASKS_PATTERN.get())
                .setDefaultValue(Messages.UNANSWERED_ASKS.getFixedTemplate())
                .setSaveConsumer(value -> {Config.UNANSWERED_ASKS_PATTERN.set(value); Messages.UNANSWERED_ASKS.setNewTemplate(value);})
                .build();
        StringListEntry unread_mail_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.unread_mail"), Config.UNREAD_MAIL_PATTERN.get())
                .setDefaultValue(Messages.UNREAD_MAIL.getFixedTemplate())
                .setSaveConsumer(value -> {Config.UNREAD_MAIL_PATTERN.set(value); Messages.UNREAD_MAIL.setNewTemplate(value);})
                .build();
        StringListEntry world_invite_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.world_invite"), Config.WORLD_INVITE_PATTERN.get())
                .setDefaultValue(Messages.WORLD_INVITE.getFixedTemplate())
                .setSaveConsumer(value -> {Config.WORLD_INVITE_PATTERN.set(value); Messages.WORLD_INVITE.setNewTemplate(value);})
                .build();
        StringListEntry new_video_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.new_video"), Config.NEW_VIDEO_PATTERN.get())
                .setDefaultValue(Messages.NEW_VIDEO.getFixedTemplate())
                .setSaveConsumer(value -> {Config.NEW_VIDEO_PATTERN.set(value); Messages.NEW_VIDEO.setNewTemplate(value);})
                .build();
        StringListEntry punishment_broadcast_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.punishment_broadcast"), Config.PUNISHMENT_BROADCAST_PATTERN.get())
                .setDefaultValue(Messages.PUNISHMENT_BROADCAST.getFixedTemplate())
                .setSaveConsumer(value -> {Config.PUNISHMENT_BROADCAST_PATTERN.set(value); Messages.PUNISHMENT_BROADCAST.setNewTemplate(value);})
                .build();
        StringListEntry donation_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.donation"), Config.DONATION_PATTERN.get())
                .setDefaultValue(Messages.DONATION.getFixedTemplate())
                .setSaveConsumer(value -> {Config.DONATION_PATTERN.set(value); Messages.DONATION.setNewTemplate(value);})
                .build();
        StringListEntry player_voted_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.player_voted"), Config.PLAYER_VOTED_PATTERN.get())
                .setDefaultValue(Messages.PLAYER_VOTED.getFixedTemplate())
                .setSaveConsumer(value -> {Config.PLAYER_VOTED_PATTERN.set(value); Messages.PLAYER_VOTED.setNewTemplate(value);})
                .build();
        StringListEntry stream_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.stream"), Config.STREAM_PATTERN.get())
                .setDefaultValue(Messages.STREAM.getFixedTemplate())
                .setSaveConsumer(value -> {Config.STREAM_PATTERN.set(value); Messages.STREAM.setNewTemplate(value);})
                .build();
        StringListEntry new_ask_pattern = entryBuilder.startStrField(new TranslationTextComponent("mlmod.config.option.new_ask"), Config.NEW_ASK_PATTERN.get())
                .setDefaultValue(Messages.NEW_ASK.getFixedTemplate())
                .setSaveConsumer(value -> {Config.NEW_ASK_PATTERN.set(value); Messages.NEW_ASK.setNewTemplate(value);})
                .build();

        general.addEntry(general_description).addEntry(ignored_players).addEntry(chat_player_interact).addEntry(ads).addEntry(pm_notification).addEntry(message_collector);

        general_messages.addEntry(general_messages_description).addEntry(reward_storage).addEntry(welcome_to_mineland).addEntry(unanswered_asks).addEntry(unread_mail)
                .addEntry(new_video).addEntry(punishment_broadcast).addEntry(donation).addEntry(player_voted)
                .addEntry(stream).addEntry(new_ask);

        creative_messages.addEntry(creative_description).addEntry(world_invite).addEntry(show_world_id).addEntry(dev_mode_join);
        patterns.addEntry(patterns_description).addEntry(welcome_pattern).addEntry(dev_mode_join_pattern)
                .addEntry(reward_storage_pattern).addEntry(unanswered_asks_pattern)
                .addEntry(unread_mail_pattern).addEntry(world_invite_pattern)
                .addEntry(new_video_pattern).addEntry(punishment_broadcast_pattern).addEntry(donation_pattern)
                .addEntry(player_voted_pattern).addEntry(stream_pattern).addEntry(new_ask_pattern);

        builder.setSavingRunnable(() -> {Config.spec.save(); Messages.updateMessages();});
        return builder.build();
    }
}