package ikeyler.mlmod.Messages;

import ikeyler.mlmod.Configuration.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Manager {
    private final List<Message> messageList = new ArrayList<>();

    public void addMessage(Message message) {
        messageList.add(message);
    }
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
        if (Messages.AD_MESSAGES.contains(message) && !Config.ADS.get()) {
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
            event.setMessage(event.getMessage().copy().append(" ").append(component));
            return;
        }

        if (message == Messages.CREATIVE_CHAT || message == Messages.DONATE_CHAT) {
            String[] split = matcher.group(1).split(" ");
            String player = split[split.length-1];
            String msg = matcher.group(2);
            String reply = message == Messages.CREATIVE_CHAT ? "/cc "+player+", " : "/dc "+player+", ";
            if (Config.IGNORED_PLAYERS.get().contains(player)) {
                event.setCanceled(true);
                return;
            }
            if (Config.CHAT_PLAYER_INTERACT.get()) {
                Style style = messageComponent.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodplayerinteract " + player + ":::" + msg + ":::"+reply));
                event.setMessage(messageComponent.copy().setStyle(style));
            }
        }
    }
}
