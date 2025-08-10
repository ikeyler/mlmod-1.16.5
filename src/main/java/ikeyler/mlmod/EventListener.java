package ikeyler.mlmod;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ikeyler.mlmod.Configuration.ClothConfig;
import ikeyler.mlmod.Configuration.Config;
import ikeyler.mlmod.Messages.Messages;
import ikeyler.mlmod.Util.ItemUtil;
import ikeyler.mlmod.Util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import static ikeyler.mlmod.main.messageManager;
import static ikeyler.mlmod.main.prefix;

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
        String part = message.replaceFirst(start, "").trim();
        List<String> commands = new ArrayList<>(Arrays.asList("/edit", "/mlc", "/var", "/text", "/num"));
        if (commands.contains(start.toLowerCase())) {
            event.setCanceled(true);
            mc.gui.getChat().addRecentChat(message);
        }
        if (start.equalsIgnoreCase("/edit")) {
            if (split.length == 1) {
                String name = mc.player.getMainHandItem().getHoverName().getString();
                Style style = TextUtil.clickToViewStyle(name.replace("§", "&"));
                mc.player.sendMessage(new StringTextComponent(prefix + name).withStyle(style), null);
                return;
            }
            if (!mc.player.isCreative()) {mc.player.sendMessage(new TranslationTextComponent("mlmod.messages.creative_mode_needed"), null); return; }
            String prevName = mc.player.getMainHandItem().getHoverName().getString();
            Style style = TextUtil.clickToViewStyle("/edit " + prevName.replace("§", "&"));
            ItemStack stack = mc.player.getMainHandItem();
            stack.setHoverName(new StringTextComponent(TextUtil.replaceColors(part)));
            int slot = mc.player.inventory.selected+36;
            ItemUtil.updateInventory(slot, stack);
            mc.player.sendMessage(new StringTextComponent(prefix).append(new TranslationTextComponent("mlmod.messages.edit.old_name", prevName)).withStyle(style), null);
        }
        if (start.equalsIgnoreCase("/mlc")) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Minecraft.getInstance().execute(() -> {
                        Screen parent = Minecraft.getInstance().screen;
                        Screen build = ClothConfig.buildConfigScreen(parent);
                        Minecraft.getInstance().setScreen(build);
                    });
                }
            }, 100);
        }
        if (start.equals("/mlmodplayerinteract")) {
            event.setCanceled(true);
            if (split.length>1) {
                String[] spl = message.replaceFirst("/mlmodplayerinteract ", "").split(":::");
                System.out.println(Arrays.toString(spl));
                String player = spl[0];
                String msg = spl.length > 1 ? spl[1] : "";
                String chat = spl.length > 2 ? spl[2] : "/m "+player+" ";

                TranslationTextComponent menu = new TranslationTextComponent("mlmod.messages.chat_player_interact", player);

                Style write = new StringTextComponent("").getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, chat));
                Style copy = new StringTextComponent("").getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg));
                Style report = new StringTextComponent("").getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/report "+player));
                Style block =  new StringTextComponent("").getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlignore "+player));

                menu.append("\n").append(new TranslationTextComponent("mlmod.messages.reply").setStyle(write)).append(" ")
                        .append(new TranslationTextComponent("mlmod.messages.copy_message").setStyle(copy)).append(" ")
                        .append(new TranslationTextComponent("mlmod.messages.report").setStyle(report)).append(" ")
                        .append(new TranslationTextComponent("mlmod.messages.block").setStyle(block));
                menu.setStyle(write);

                Minecraft.getInstance().player.sendMessage(new StringTextComponent(prefix).append(menu), null);
            }
        }

        if (start.equalsIgnoreCase("/mlignore") && split.length>1) {
            event.setCanceled(true);
            String player = split[1];
            List<String> players = new ArrayList<>(Config.IGNORED_PLAYERS.get());
            Style add_on_mineland = new StringTextComponent("").getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ignore add "+player+" "));
            Style remove_on_mineland = new StringTextComponent("").getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ignore remove "+player+" "));
            if (!players.contains(player)) {
                players.add(player);
                mc.player.sendMessage(new StringTextComponent(prefix).append(new TranslationTextComponent("mlmod.messages.ignore.player_added", player).append(". ").append(
                        new TranslationTextComponent("mlmod.messages.ignore.add_on_mineland", new TranslationTextComponent("mlmod.mineland"))).withStyle(add_on_mineland)), null);
            }
            else {
                players.remove(player);
                mc.player.sendMessage(new StringTextComponent(prefix).append(new TranslationTextComponent("mlmod.messages.ignore.player_removed", player).append(". ").append(
                        new TranslationTextComponent("mlmod.messages.ignore.remove_on_mineland", new TranslationTextComponent("mlmod.mineland"))).withStyle(remove_on_mineland)), null);
            }
            Config.IGNORED_PLAYERS.set(players);
            Config.spec.save();
        }

        if (start.equalsIgnoreCase("/var") || start.equalsIgnoreCase("/text") || start.equalsIgnoreCase("/num")) {
            event.setCanceled(true);
            if (!mc.player.isCreative()) {mc.player.sendMessage(new TranslationTextComponent("mlmod.messages.creative_mode_needed"), null); return; }
            ItemStack item = null;
            String[] spl = message.split(" ", 2);
            String name = spl.length>1 ? spl[1] : "";
            switch (start.toLowerCase()) {
                case "/var":
                    item = Items.MAGMA_CREAM.getDefaultInstance();
                    String json = "{display:{Lore:['{\"italic\":false,\"color\":\"gray\",\"text\":\"Тип переменной, который может хранить что угодно.\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"Значение динамической переменной\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"может быть задано с помощью блока\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"\\\\u0027Установить переменную\\\\u0027, а динамические\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"переменные с тем же именем сохраняют\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"одно и то же значение.\"}','{\"text\":\"\"}','{\"italic\":false,\"color\":\"light_purple\",\"text\":\"Значения:\"}','{\"extra\":[{\"italic\":false,\"color\":\"aqua\",\"text\":\"\\\\u003e \"},{\"italic\":false,\"color\":\"gray\",\"text\":\"Имя (введите в чат, держа предмет в руке.\"}],\"text\":\"\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"Используй имена, такие как %player%\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"для переменных, зависящих от игрока)\"}','{\"extra\":[{\"italic\":false,\"color\":\"aqua\",\"text\":\"\\\\u003e \"},{\"italic\":false,\"color\":\"gray\",\"text\":\"Сохранение переменной (Присесть + Правый клик,\"}],\"text\":\"\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\"держа предмет, тег \\\\u0027СОХРАНИТЬ\\\\u0027 позволяет сохранить\"}','{\"italic\":false,\"color\":\"gray\",\"text\":\" переменную навсегда)\"}'],Name:'{\"italic\":false,\"color\":\"red\",\"text\":\"Динамическая переменная\"}'}}";
                    try {
                        CompoundNBT nbt = JsonToNBT.parseTag(json);
                        item.setTag(nbt);
                    }
                    catch (Exception ignore) {}
                    break;
                case "/text":
                    item = Items.BOOK.getDefaultInstance();
                    break;
                case "/num":
                    item = Items.SLIME_BALL.getDefaultInstance();
                    break;
            }
            if (item!=null) {
                item.setHoverName(new StringTextComponent(TextUtil.replaceColors(name)));
                // не апдейтает инвентарь
                mc.player.addItem(item);
            }
        }
    }
}

