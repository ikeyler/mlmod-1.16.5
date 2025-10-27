package ikeyler.mlmod.messages;

import ikeyler.mlmod.cfg.Config;
import ikeyler.mlmod.main;
import ikeyler.mlmod.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ikeyler.mlmod.main.messageCollector;

public class Manager {
    private final List<Message> messageList = new ArrayList<>();
    private final Pattern adPattern = Pattern.compile("/?(ad|ад|id|айди)\\s+(\\S+)");
    private final Minecraft mc = Minecraft.getInstance();

    public void addMessages(List<Message> messages) {
        messageList.addAll(messages);
    }
    public Message getMessage(String message) {
        return messageList.stream()
                .filter(msg -> msg.matches(message)).findFirst().orElse(null);
    }
    public void processMessages(Message message, ClientChatReceivedEvent event) {
        if (message == null) return;
        if (!message.isActive()) {
            event.setCanceled(true);
            return;
        }
        if (!Config.ADS.get() && Messages.AD_MESSAGES.contains(message)) {
            event.setCanceled(true);
            return;
        }
        ITextComponent messageComponent = event.getMessage();
        Matcher matcher = message.getMatcher();

        if (message == Messages.UNANSWERED_ASKS || message == Messages.UNREAD_MAIL) {
            String cmd = message == Messages.UNANSWERED_ASKS ? "/q" : "/mailgui";
            TranslationTextComponent component = new TranslationTextComponent("mlmod.messages.open_component");
            component.withStyle(component.getStyle()
                    .withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            cmd
                    )));
            event.setMessage(messageComponent.copy().append(" ").append(component));
            return;
        }

        if (message == Messages.CREATIVE_CHAT || message == Messages.DONATE_CHAT) {
            boolean hideMessage = false;
            String[] split = matcher.group(1).split(" ");
            String player = split[split.length-1];
            String msg = StringUtils.removeEnd(matcher.group(2), "[Перевести]").trim();
            String reply = message == Messages.CREATIVE_CHAT ? "/cc "+player+", " : "/dc "+player+", ";
            if (Config.IGNORED_PLAYERS.get().contains(player.toLowerCase())) {
                event.setCanceled(true); player = player+" (blocked)"; hideMessage=true;
            }

            if (message == Messages.CREATIVE_CHAT) messageCollector.addEntry(MessageType.CREATIVE_CHAT, player, msg);
            else messageCollector.addEntry(MessageType.DONATE_CHAT, player, msg);

            if (Config.CHAT_PLAYER_INTERACT.get() && !hideMessage) {
                IFormattableTextComponent component = messageComponent.copy();
                Style style = messageComponent.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodplayerinteract " + player + ":::" + msg + ":::" + reply));
                component.setStyle(style);

                Matcher adMatcher = adPattern.matcher(msg.toLowerCase());
                List<String> adList = new ArrayList<>();
                while (adMatcher.find()) {String[] spl = adMatcher.group(0).split(" "); adList.add("/ad "+spl[spl.length-1].replace(",", ""));}
                if (!adList.isEmpty()) {
                    Style adStyle = TextUtil.newStyle().
                            withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodshowmessageads " + String.join(",", adList))).
                            withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.show_world_ads")));
                    StringTextComponent adComponent = new StringTextComponent(" ⬈");
                    adComponent.setStyle(adStyle);
                    component.append(adComponent);
                }
                event.setMessage(component);
            }
            return;
        }

        if (message == Messages.PM || message == Messages.PM_REPLY) {
            boolean hideMessage = false;
            String you = new TranslationTextComponent("mlmod.you").getString();
            String player = message == Messages.PM ? matcher.group(1) : you;
            String msg = StringUtils.removeEnd(matcher.group(2), "[Перевести]").trim();

            if (Config.IGNORED_PLAYERS.get().contains(player.toLowerCase()) && !player.equalsIgnoreCase(you)) {
                event.setCanceled(true); player = player+" (blocked)"; hideMessage=true;
            }
            if (message == Messages.PM) messageCollector.addEntry(MessageType.PRIVATE_MESSAGE, player, msg);
            else messageCollector.addEntry(MessageType.PM_REPLY, player, matcher.group(1)+" -> "+msg);

            if (hideMessage) return;
            if (Config.PM_NOTIFICATION.get() && GLFW.glfwGetWindowAttrib(mc.getWindow().getWindow(), GLFW.GLFW_FOCUSED) == 0) {
                mc.level.playSound(mc.player, mc.player.blockPosition(), new SoundEvent(new ResourceLocation("entity.experience_orb.pickup")), SoundCategory.MASTER, 0.5F, 0.7F);
            }
            return;
        }

        if (message == Messages.WORLD_INVITE && Config.SHOW_WORLD_ID.get()) {
            try {
                String[] split = messageComponent.getSiblings().get(0).getStyle().getClickEvent().getValue().split(" ");
                String worldID = split[split.length-1];
                event.setMessage(messageComponent.copy().append("§8(ID: "+worldID+")"));
            } catch (Exception e) {
                main.logger.error("error while reformatting world invite:");e.printStackTrace();}
        }
    }
}
