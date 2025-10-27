package ikeyler.mlmod;

import ikeyler.mlmod.cfg.ClothConfig;
import ikeyler.mlmod.cfg.Config;
import ikeyler.mlmod.messages.MessageType;
import ikeyler.mlmod.messages.Messages;
import ikeyler.mlmod.util.ItemUtil;
import ikeyler.mlmod.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.List;

import static ikeyler.mlmod.main.*;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EventListener {
    private final Minecraft mc = Minecraft.getInstance();
    private boolean messages_updated = false;
    @SubscribeEvent
    public void onChatReceivedEvent(ClientChatReceivedEvent event) {
        if (!messages_updated) {Messages.updateMessages();messages_updated=true;}
        messageManager.processMessages(messageManager.getMessage(event.getMessage().getString()), event);
    }

    @SubscribeEvent
    public void onChatEvent(ClientChatEvent event) {
        String message = event.getMessage();
        String[] split = message.split(" ");
        String start = split.length > 0 ? split[0] : "";
        List<String> commands = new ArrayList<>(Arrays.asList("/edit", "/mlc", "/var", "/text", "/num", "/msgs"));
        if (commands.contains(start.toLowerCase())) {
            event.setCanceled(true);
            mc.gui.getChat().addRecentChat(message);
        }

        switch (start.toLowerCase()) {
            case "/edit":
                if (split.length == 1) {
                    String name = mc.player.getMainHandItem().getHoverName().getString();
                    Style style = TextUtil.clickToViewStyle(name.replace("§", "&"));
                    mc.player.sendMessage(new StringTextComponent(prefix + name).withStyle(style), null);
                    return;
                }
                if (!mc.player.isCreative()) {
                    mc.player.sendMessage(new TranslationTextComponent("mlmod.messages.creative_mode_needed"), null);
                    return;
                }
                String prevName = mc.player.getMainHandItem().getHoverName().getString();
                Style edit = TextUtil.clickToViewStyle("/edit " + prevName.replace("§", "&"));
                ItemStack stack = mc.player.getMainHandItem();
                stack.setHoverName(new StringTextComponent(TextUtil.replaceColors(message.replaceFirst(start, "").trim())));
                ItemUtil.updateInventory(mc.player.inventory.selected + 36, stack);
                mc.player.sendMessage(new StringTextComponent(prefix).append(new TranslationTextComponent("mlmod.messages.edit.old_name", prevName)).withStyle(edit), null);
                break;

            case "/mlc":
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mc.execute(() -> {
                            Screen parent = mc.screen;
                            Screen build = ClothConfig.buildConfigScreen(parent);
                            mc.setScreen(build);
                        });
                    }
                }, 100);
                break;

            case "/mlmodplayerinteract":
                event.setCanceled(true);
                if (split.length > 1) {
                    String[] spl = message.replaceFirst("/mlmodplayerinteract ", "").split(":::");
                    String player = spl[0];
                    String msg = spl.length > 1 ? spl[1] : "";
                    String chat = spl.length > 2 ? spl[2] : "/m " + player + " ";
                    TranslationTextComponent menu = new TranslationTextComponent("mlmod.messages.chat_player_interact", player);
                    Style write = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, chat));
                    Style copy = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(msg)));
                    Style report = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/report " + player));
                    Style block = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlignore " + player));
                    menu.append("\n").append(new TranslationTextComponent("mlmod.messages.reply").setStyle(write)).append(" ")
                            .append(new TranslationTextComponent("mlmod.messages.copy_message").setStyle(copy)).append(" ")
                            .append(new TranslationTextComponent("mlmod.messages.report").setStyle(report)).append(" ")
                            .append(new TranslationTextComponent("mlmod.messages.block").setStyle(block));
                    menu.setStyle(write);
                    mc.player.sendMessage(new StringTextComponent(prefix).append(menu), null);
                }
                break;

            case "/mlignore":
                event.setCanceled(true);
                if (split.length < 1) return;
                String player = split[1];
                List<String> players = new ArrayList<>(Config.IGNORED_PLAYERS.get());
                Style add_on_mineland = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ignore add " + player + " "));
                Style remove_on_mineland = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ignore remove " + player + " "));
                if (!players.contains(player)) {
                    players.add(player);
                    mc.player.sendMessage(new StringTextComponent(prefix).append(new TranslationTextComponent("mlmod.messages.ignore.player_added", player).append(". ").append(
                            new TranslationTextComponent("mlmod.messages.ignore.add_on_mineland", new TranslationTextComponent("mlmod.mineland"))).withStyle(add_on_mineland)), null);
                } else {
                    players.remove(player);
                    mc.player.sendMessage(new StringTextComponent(prefix).append(new TranslationTextComponent("mlmod.messages.ignore.player_removed", player).append(". ").append(
                            new TranslationTextComponent("mlmod.messages.ignore.remove_on_mineland", new TranslationTextComponent("mlmod.mineland"))).withStyle(remove_on_mineland)), null);
                }
                Config.IGNORED_PLAYERS.set(players);
                Config.spec.save();
                break;

            case "/var":
            case "/text":
            case "/num":
                event.setCanceled(true);
                if (!mc.player.isCreative()) {
                    mc.player.sendMessage(new TranslationTextComponent("mlmod.messages.creative_mode_needed"), null);
                    return;
                }
                ItemStack item = null;
                String[] spl = message.split(" ", 2);
                String name = spl.length > 1 ? spl[1] : "";
                switch (start.toLowerCase()) {
                    case "/var":
                        item = Items.MAGMA_CREAM.getDefaultInstance();
                        String json = "{display:{Lore:['{\"italic\":false,\"color\":\"gray\",\"text\":\"Тип переменной, который может хранить что угодно.\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"Значение динамической переменной\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"может быть задано с помощью блока\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"\\\\u0027Установить переменную\\\\u0027, а динамические\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"переменные с тем же именем сохраняют\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"одно и то же значение.\"}','{\"text\":\"\"}','{\"italic\":false,\"color\":\"light_purple\",\"text\":\"Значения:\"}','{\"extra\":[{\"italic\":false,\"color\":\"aqua\",\"text\":\"\\\\u003e \"},{\"italic\":false,\"color\":\"gray\",\"text\":\"Имя (введите в чат, держа предмет в руке.\"}],\"text\":\"\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"Используй имена, такие как %player%\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"для переменных, зависящих от игрока)\"}','{\"extra\":[{\"italic\":false,\"color\":\"aqua\",\"text\":\"\\\\u003e \"},{\"italic\":false,\"color\":\"gray\",\"text\":\"Сохранение переменной (Присесть + Правый клик,\"}],\"text\":\"\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"держа предмет, тег \\\\u0027СОХРАНИТЬ\\\\u0027 позволяет сохранить\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\" переменную навсегда)\"}'],Name:'{\"italic\":false,\"color\":\"red\",\"text\":\"Динамическая переменная\"}'}}";
                        try {
                            CompoundNBT nbt = JsonToNBT.parseTag(json);
                            item.setTag(nbt);
                        } catch (Exception e) {
                            logger.error("error while parsing tag: ", e);
                        }
                        break;
                    case "/text":
                        item = Items.BOOK.getDefaultInstance();
                        break;
                    case "/num":
                        item = Items.SLIME_BALL.getDefaultInstance();
                        break;
                }
                if (item != null) {
                    item.setHoverName(new StringTextComponent(TextUtil.replaceColors(name)));
                    // не апдейтает инвентарь
                    mc.player.addItem(item);
                }
                break;

            case "/msgs":
                if (split.length == 1) {
                    int totalMessages = messageCollector.readAll().size();
                    StringTextComponent component = new StringTextComponent(prefix);
                    component.append(new TranslationTextComponent("mlmod.messages.collector.total", totalMessages));
                    component.append("\n").append(new TranslationTextComponent("mlmod.messages.collector.search_guide"));
                    Style msgsStyle = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "mlmodData.txt"));
                    component.append("\n").append(new TranslationTextComponent("mlmod.messages.collector.info").setStyle(msgsStyle)).append("\n");
                    String state = Config.MESSAGE_COLLECTOR.get() ? new TranslationTextComponent("mlmod.messages.collector.state_enabled").getString() : new TranslationTextComponent("mlmod.messages.collector.state_disabled").getString();
                    msgsStyle = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodtogglemsgcollector"));
                    component.append(new StringTextComponent(state).setStyle(msgsStyle));
                    mc.player.sendMessage(component, null);
                    return;
                }
                if (split.length > 2 && split[1].equalsIgnoreCase("find")) {
                    String query = message.toLowerCase().replaceFirst("/msgs find ", "");
                    MessageType type = Arrays.stream(MessageType.values())
                            .filter(t -> t.getName().equalsIgnoreCase(split[split.length - 1]))
                            .findFirst()
                            .orElse(null);
                    if (type != null)
                        query = query.split(" ").length == 1 ? null : query.substring(0, query.lastIndexOf(" ")).trim();
                    messageCollector.findAsync(query, type, 50, "mc");
                }
                break;

            case "/mlmodtogglemsgcollector":
                event.setCanceled(true);
                Config.MESSAGE_COLLECTOR.set(!Config.MESSAGE_COLLECTOR.get());
                Config.spec.save();
                mc.player.sendMessage(new TranslationTextComponent("mlmod.success"), null);
                break;
            case "/mlmodshowmessageads":
                event.setCanceled(true);
                if (split.length < 2) return;
                StringTextComponent component = new StringTextComponent(prefix);
                component.append(new TranslationTextComponent("mlmod.messages.world_list"));
                component.append("\n");
                for (String c:message.replaceFirst("/mlmodshowmessageads ", "").split(",")) {
                    StringTextComponent ad = new StringTextComponent("§8- §7"+c);
                    ad.append("\n");
                    ad.setStyle(TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, c)));
                    component.append(ad);
                }
                mc.player.sendMessage(component, null);
                break;
            default:
                break;
        }
    }
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (event.getAction() == 1) {
            if (Keybinds.hub.isDown()) mc.player.chat("/hub");
            else if (Keybinds.play.isDown()) mc.player.chat("/play");
            else if (Keybinds.build.isDown()) mc.player.chat("/build");
            else if (Keybinds.dev.isDown()) mc.player.chat("/dev");
        }
    }
}


