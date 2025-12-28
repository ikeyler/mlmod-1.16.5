package ikeyler.mlmod;

import ikeyler.mlmod.cfg.ClothConfig;
import ikeyler.mlmod.cfg.Config;
import ikeyler.mlmod.itemeditor.ChatEditor;
import ikeyler.mlmod.itemeditor.ItemEditor;
import ikeyler.mlmod.messages.MessageType;
import ikeyler.mlmod.util.ItemUtil;
import ikeyler.mlmod.util.ModUtils;
import ikeyler.mlmod.util.SoundUtil;
import ikeyler.mlmod.util.TextUtil;
import ikeyler.mlmod.variables.Variable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ikeyler.mlmod.Main.*;
import static ikeyler.mlmod.util.ModUtils.MOD_PREFIX;
import static ikeyler.mlmod.util.ModUtils.VAR_SEPARATOR;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ChatListener {
    private final Minecraft mc = Minecraft.getInstance();
    private final List<String> commands = new ArrayList<>(
            Arrays.asList("/mlc", "/item", "/var", "/text", "/num", "/msgs", "/ignorelist", "/head", "/nightmode", "/vars", "/varsave"));

    @SubscribeEvent
    public void onChatReceivedEvent(ClientChatReceivedEvent event) {
        if (Config.DETECT_MINELAND.get() && !ModUtils.isOnMineland())
            return;
        messageManager.processMessages(messageManager.getMessage(event.getMessage().getString()), event);
    }

    @SubscribeEvent
    public void onChatEvent(ClientChatEvent event) {
        String message = event.getMessage();
        String[] split = message.split(" ");
        String start = split.length > 0 ? split[0] : "";
        if (commands.contains(start.toLowerCase())) {
            event.setCanceled(true);
            mc.gui.getChat().addRecentChat(message);
        }

        if (message.startsWith("/") && !Config.COMMAND_ALIASES.get().isEmpty()) {
            String command = start.replaceFirst("/", "");
            String args = message.split(" ", 2).length > 1 ? message.split(" ", 2)[1] : "";
            if (command.isEmpty()) return;
            for (String entry:Config.COMMAND_ALIASES.get()) {
                String[] spl = entry.split(":", 2);
                if (spl.length < 2) continue;
                if (command.equalsIgnoreCase(spl[0].trim())) {
                    event.setCanceled(true);
                    String cmd = spl[1].trim();
                    String output = "/"+cmd+" "+args;
                    if (commands.contains("/"+cmd.toLowerCase()))
                        MinecraftForge.EVENT_BUS.post(new ClientChatEvent(output));
                    else {
                        mc.player.chat(output);
                        mc.gui.getChat().addRecentChat(message);
                    }
                    break;
                }
            }
        }

        if (message.startsWith("!") && Config.EXCL_MARK_TO_CHAT.get() != Config.CHAT_MODE.OFF) {
            if (Config.DETECT_MINELAND.get() && !ModUtils.isOnMineland()) return;
            String newMessage = message.replaceFirst("!", "").trim();
            if (newMessage.isEmpty()) return;
            event.setCanceled(true);
            mc.gui.getChat().addRecentChat(message);
            String chatType = Config.EXCL_MARK_TO_CHAT.get() == Config.CHAT_MODE.CC ? "/cc" : "/dc";
            mc.player.chat(chatType+" "+newMessage);
            return;
        }

        switch (start.toLowerCase()) {
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
                if (split.length < 2) return;
                String[] msgSplit = message.replaceFirst("/mlmodplayerinteract ", "").split("§§");
                String player = msgSplit[0];
                String msg = msgSplit.length > 1 ? msgSplit[1] : null;
                String chat = msgSplit.length > 2 ? msgSplit[2] : null;

                StringTextComponent playerComp = new StringTextComponent("§7§o"+player);
                playerComp.append(new TranslationTextComponent("mlmod.copy"));
                playerComp.setStyle(TextUtil.clickToCopyStyle(player, "name", false));
                TranslationTextComponent menu = new TranslationTextComponent("mlmod.messages.chat_player_interact", playerComp);

                menu.append("\n");
                if (msg != null && chat != null)
                    menu.append(new TranslationTextComponent("mlmod.messages.reply").setStyle(
                            TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, chat))
                    )).append(" ");
                if (msg != null)
                    menu.append(new TranslationTextComponent("mlmod.messages.copy_message").setStyle(
                            TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(msg)))
                    )).append(" ");

                menu.append(new TranslationTextComponent("mlmod.messages.report").setStyle(
                        TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report " + player))
                )).append(" ");
                menu.append(new TranslationTextComponent("mlmod.messages.block").setStyle(
                        TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlignore " + player))
                )).append(" ");
                menu.append(new TranslationTextComponent("mlmod.messages.find_messages").setStyle(
                        TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msgs find " + player + " "))
                )).append(" ");
                menu.append(new TranslationTextComponent("mlmod.messages.find_who").setStyle(
                        TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/who " + player))
                ));

                ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX).append(menu));
                break;

            case "/mlignore":
                event.setCanceled(true);
                if (split.length < 2) return;
                String ignorePlayer = split[1].toLowerCase();
                List<String> players = new ArrayList<>(Config.IGNORED_PLAYERS.get()).stream().map(String::toLowerCase).collect(Collectors.toList());
                boolean containsPlayer = players.contains(ignorePlayer);
                String ignoreAction = (containsPlayer ? "/ignore remove " : "/ignore add ") + ignorePlayer + " ";
                String ignoreMessage = containsPlayer ? "mlmod.messages.ignore.player_removed" : "mlmod.messages.ignore.player_added";
                Style ignoreStyle = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ignoreAction));
                if (!containsPlayer) players.add(ignorePlayer);
                else players.remove(ignorePlayer);
                ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX).append(new TranslationTextComponent(ignoreMessage, ignorePlayer).withStyle(ignoreStyle)));
                Config.IGNORED_PLAYERS.set(players);
                Config.spec.save();
                messageManager.updateIgnoredPlayers();
                break;

            case "/var":
            case "/text":
            case "/num":
                if (!mc.player.isCreative()) {
                    ModUtils.sendCreativeModeNeeded();
                    return;
                }
                ItemStack item = null;
                String[] spl = message.split(" ", 2);
                String name = spl.length > 1 ? spl[1] : "";
                String varDesc = null;
                switch (start.toLowerCase()) {
                    case "/var":
                        item = ItemUtil.getDynamicVar(false);
                        break;
                    case "/text":
                        item = Items.BOOK.getDefaultInstance();
                        varDesc = "mlmod.var.text.desc";
                        break;
                    case "/num":
                        item = Items.SLIME_BALL.getDefaultInstance();
                        varDesc = "mlmod.var.number.desc";
                        break;
                }
                if (item != null) {
                    item.setHoverName(new StringTextComponent(TextUtil.replaceColorCodes(name)));
                    if (varDesc != null)
                        ItemEditor.setLore(item, Arrays.asList(new TranslationTextComponent(varDesc).getString().split("\n")));
                    int slotId = mc.player.inventory.getFreeSlot();
                    mc.player.inventory.setItem(slotId, item);
                    ItemUtil.updateSlot(36+slotId, item);
                    mc.gui.setOverlayMessage(new TranslationTextComponent("mlmod.messages.var.var_given"), false);
                }
                break;

            case "/msgs":
                if (split.length == 1) {
                    int totalMessages = messageCollector.readAll().size();
                    Style fileStyle = TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "mlmodData.txt"));
                    StringTextComponent component = new StringTextComponent(MOD_PREFIX);
                    component.append(new TranslationTextComponent("mlmod.messages.collector.total", totalMessages));
                    component.append("\n");
                    component.append(new TranslationTextComponent("mlmod.messages.collector.search_guide"));
                    component.append("\n");
                    component.append(new TranslationTextComponent("mlmod.messages.collector.info").setStyle(fileStyle));
                    component.append("\n");
                    String state = Config.MESSAGE_COLLECTOR.get()
                            ? "mlmod.messages.collector.state_enabled" : "mlmod.messages.collector.state_disabled";
                    component.append(new TranslationTextComponent(state).setStyle(
                            TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodtogglemsgcollector"))));
                    ModUtils.sendMessage(component);
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

            case "/ignorelist":
                List<String> ignoredPlayers = new ArrayList<>(Config.IGNORED_PLAYERS.get());
                StringTextComponent ignoreComponent = new StringTextComponent(MOD_PREFIX);
                ignoreComponent.append(new TranslationTextComponent("mlmod.messages.ignorelist.ignore_list", ignoredPlayers.size()));
                ignoreComponent.append("\n");
                for (String ignoredPlayer : ignoredPlayers) {
                    ignoreComponent.append("§8- §7").append(new StringTextComponent(ignoredPlayer)
                            .setStyle(TextUtil.newStyle()
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlignore "+ignoredPlayer))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.ignorelist.click_to_remove")))));
                    ignoreComponent.append("\n");
                }
                ignoreComponent.append(new TranslationTextComponent("mlmod.messages.ignorelist.info")
                        .setStyle(TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mlignore "))));
                ModUtils.sendMessage(ignoreComponent);
                break;

            case "/sound":
                if (!Config.SOUND_COMMAND.get()) return;

                event.setCanceled(true);
                mc.gui.getChat().addRecentChat(message);
                if (split.length == 1) {
                    ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX).
                            append(new TranslationTextComponent("mlmod.messages.sound.usage")).setStyle(
                                    TextUtil.newStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.sound.usage_info")))
                            )
                            .append("\n").append(new TranslationTextComponent("mlmod.messages.sound.search_guide"))
                            .append("\n").append(new TranslationTextComponent("mlmod.messages.sound.info")));
                    return;
                }
                else if (split.length > 1 && !split[1].equalsIgnoreCase("find")) {
                    String sound = split[1].toLowerCase();
                    if (!SoundUtil.getSoundIds().contains(sound)) {
                        ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.sound.sounds_not_found"));
                        return;
                    }
                    float pitch = 1;
                    float volume = 1;
                    try {
                        pitch = split.length > 2 ? Float.parseFloat(split[2]) : pitch;
                        volume = split.length > 3 ? Float.parseFloat(split[3]) : volume;
                    } catch (Exception ignore) {ModUtils.sendIncorrectArguments(); return;}
                    mc.getSoundManager().stop();
                    mc.gui.setOverlayMessage(new TranslationTextComponent("mlmod.messages.sound.playing_sound", sound), true);
                    SoundUtil.playSound(sound, volume, pitch);
                    return;
                }

                String query = message.toLowerCase().replaceFirst("/sound find ", "");
                List<String> sounds = SoundUtil.findSoundIds(query);
                if (sounds.isEmpty()) {
                    ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.sound.sounds_not_found"));
                    return;
                }
                StringTextComponent soundComponent = new StringTextComponent(MOD_PREFIX);
                soundComponent.append(new TranslationTextComponent("mlmod.messages.sound.sounds_found", sounds.size()));
                soundComponent.append("\n");
                boolean switchSoundColor = false;
                for (int i = 0; i < sounds.size(); i++) {
                    String sound = sounds.get(i);
                    String color = switchSoundColor ? "§f" : "§7";
                    soundComponent.append(new StringTextComponent(color+sound)
                            .setStyle(TextUtil.newStyle()
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sound "+sound))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.sound.click_to_play_sound")))));
                    if (i < sounds.size()-1) {soundComponent.append(", ");}
                    switchSoundColor = !switchSoundColor;
                }
                ModUtils.sendMessage(soundComponent);
                break;

            case "/head":
                if (split.length == 1) {
                    ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX).append(new TranslationTextComponent("mlmod.messages.head.usage")));
                    return;
                }
                if (!mc.player.isCreative()) {
                    ModUtils.sendCreativeModeNeeded();
                    return;
                }
                try {
                    String headName = split[1].toLowerCase();
                    mc.player.addItem(ItemUtil.getPlayerHead(headName));
                    ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX).append(new TranslationTextComponent("mlmod.messages.head.head_given", "§7"+headName)));
                } catch (Exception e) {ModUtils.sendCommandError(); logger.error(e);}
                break;

            case "/nightmode":
                ModUtils.nightModeCommand();
                break;

            case "/item":
                if (split.length == 1) {
                    ChatEditor chatEditor = new ChatEditor(mc.player.getMainHandItem());
                    chatEditor.printChatEditor();
                    return;
                }
                List<String> actionList = new ArrayList<>(
                        Arrays.asList("name", "addlore", "removelore", "editlore", "enchant", "unenchant", "nbt", "enchlist", "break", "unbreak",
                                "dur", "durability"));
                String action = split[1].toLowerCase();
                ItemStack itemStack = mc.player.getMainHandItem();

                if (!actionList.contains(action)) {
                    ModUtils.sendIncorrectArguments();
                    return;
                }
                String arg = message.indexOf(" ", 6) != -1 ? message.substring(message.indexOf(" ", 6)).trim() : "";
                String[] args = arg.split(" ");
                switch (action) {
                    case "name":
                        String oldName = itemStack.getHoverName().getString();
                        ItemEditor.renameItem(itemStack, TextUtil.replaceColorCodes(arg));
                        ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX).append(new TranslationTextComponent("mlmod.messages.itemeditor.old_name", oldName)
                                .setStyle(TextUtil.clickToViewStyle(oldName.replace("§", "&")))));
                        break;
                    case "addlore":
                        ItemEditor.addLore(itemStack, TextUtil.replaceColorCodes(arg));
                        break;
                    case "editlore":
                        try {
                            int loreIndex = Integer.parseInt(args[0]);
                            ItemEditor.editLore(itemStack, loreIndex, TextUtil.replaceColorCodes(arg.substring(arg.indexOf(" ")+1)));
                        } catch (Exception ignore) {
                            ModUtils.sendIncorrectArguments();
                            return;
                        }
                        break;
                    case "removelore":
                        if (arg.isEmpty()) {
                            ItemEditor.clearLore(itemStack);
                        }
                        else {
                            try {
                                int loreIndex = Integer.parseInt(args[0]);
                                ItemEditor.removeLore(itemStack, loreIndex);
                            } catch (Exception ignore) {
                                ModUtils.sendCommandError();
                                return;
                            }
                        }
                        break;
                    case "nbt":
                        String nbt = itemStack.hasTag() ? itemStack.getTag().toString() : "{}";
                        ModUtils.sendMessage(new StringTextComponent(MOD_PREFIX+nbt).setStyle(TextUtil.clickToCopyStyle(nbt, "text", false)));
                        return;
                    case "enchlist":
                        List<String> enchantments = ForgeRegistries.ENCHANTMENTS.getKeys().stream().map(ResourceLocation::getPath).collect(Collectors.toList());
                        StringTextComponent enchComp = new StringTextComponent(MOD_PREFIX);
                        boolean switchEnchColor = false;
                        for (int i = 0; i < enchantments.size(); i++) {
                            String ench = enchantments.get(i);
                            String color = switchEnchColor ? "§f" : "§7";
                            enchComp.append(new StringTextComponent(color+ench+" ("+
                                    ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(ench)).getFullname(1).getString()+color+")")
                                    .setStyle(TextUtil.newStyle()
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/item enchant "+ench+" "))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.itemeditor.click_to_enchant")))));
                            if (i < enchantments.size()-1) {enchComp.append(", ");}
                            switchEnchColor = !switchEnchColor;
                        }
                        ModUtils.sendMessage(enchComp);
                        return;
                    case "enchant":
                        try {
                            Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation((args[0].toLowerCase())));
                            int level = Integer.parseInt(args[1]);
                            ItemEditor.addEnchantment(itemStack, ench, level);
                        } catch (Exception ignore) {
                            ModUtils.sendIncorrectArguments();
                            return;
                        }
                        break;
                    case "unenchant":
                        if (arg.isEmpty()) {
                            ItemEditor.removeEnchantment(itemStack, null);
                        }
                        else {
                            try {
                                Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation((args[0].toLowerCase())));
                                if (ench == null) {
                                    ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.itemeditor.no_ench_on_item"));
                                    return;
                                }
                                ItemEditor.removeEnchantment(itemStack, ench);
                            } catch (Exception ignore) {
                                ModUtils.sendCommandError();
                                return;
                            }
                        }
                        break;
                    case "break":
                    case "unbreak":
                        ItemEditor.setUnbreakable(itemStack, !ItemEditor.isUnbreakable(itemStack));
                        break;
                    case "dur":
                    case "durability":
                        try {
                            itemStack.setDamageValue(Integer.parseInt(args[0]));
                        } catch (Exception ignore) {
                            ModUtils.sendIncorrectArguments();
                            return;
                        }
                        break;
                }
                ModUtils.sendBarSuccess();
                ItemUtil.updateSlot(36+mc.player.inventory.selected, itemStack);
                break;

            case "/vars":
                List<Variable> vars = varCollector.readVariables();
                StringTextComponent varComponent = new StringTextComponent(MOD_PREFIX);
                varComponent.append(new TranslationTextComponent("mlmod.messages.vars.var_list", vars.size()));
                varComponent.append("\n");
                for (Variable variable:vars) {
                    String stringVar = variable.getType()+VAR_SEPARATOR+variable.getName()+VAR_SEPARATOR+variable.getNbt();
                    varComponent.append(new StringTextComponent("§c- §7")
                                    .setStyle(TextUtil.newStyle()
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodremovevar "+stringVar))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.vars.click_to_remove")))))
                            .append(new TranslationTextComponent("mlmod.var."+variable.getType().name().toLowerCase())
                                    .append("§7: "+variable.getFixedName())
                                    .setStyle(TextUtil.newStyle()
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlmodgetvar "+stringVar))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.vars.click_to_get")))));
                    varComponent.append("\n");
                }
                varComponent.append(new TranslationTextComponent("mlmod.messages.vars.info")
                        .setStyle(TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/varsave"))));
                ModUtils.sendMessage(varComponent);
                break;

            case "/varsave":
                Variable variable = Variable.fromItem(mc.player.getMainHandItem());
                if (variable == null) {
                    ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.vars.var_not_saved"));
                    return;
                }
                varCollector.addVariable(variable);
                ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.vars.var_saved", variable.getType().name()));
                break;

            case "/mlmodremovevar":
                event.setCanceled(true);
                Variable parsedVar = Variable.fromString(message.replaceFirst("/mlmodremovevar ", ""));
                if (parsedVar != null && varCollector.removeVariable(parsedVar)) {
                    ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.vars.var_removed", parsedVar.getName()));
                    return;
                }
                ModUtils.sendMessage(new TranslationTextComponent("mlmod.messages.vars.var_not_removed"));
                break;

            case "/mlmodgetvar":
                event.setCanceled(true);
                if (!mc.player.isCreative()) {
                    ModUtils.sendCreativeModeNeeded();
                    return;
                }
                Variable parsedVar2 = Variable.fromString(message.replaceFirst("/mlmodgetvar ", ""));
                ItemStack itemVar;
                if (parsedVar2 != null && (itemVar = Variable.itemFromVariable(parsedVar2)) != null) {
                    int slotId = mc.player.inventory.getFreeSlot();
                    // who else would think that u should use
                    // setItem instead of addItem?
                    mc.player.inventory.setItem(slotId, itemVar);
                    ItemUtil.updateSlot(36 + slotId, itemVar);
                }
                break;

            case "/mlmodtogglemsgcollector":
                event.setCanceled(true);
                Config.MESSAGE_COLLECTOR.set(!Config.MESSAGE_COLLECTOR.get());
                Config.spec.save();
                ModUtils.sendSuccess();
                break;
            case "/mlmodshowmessageads":
                event.setCanceled(true);
                if (split.length < 2) return;
                StringTextComponent adsComponent = new StringTextComponent(MOD_PREFIX);
                adsComponent.append(new TranslationTextComponent("mlmod.messages.world_list"));
                adsComponent.append("\n");
                for (String adCmd:message.replaceFirst("/mlmodshowmessageads ", "").split(",")) {
                    StringTextComponent ad = new StringTextComponent("§8- §7"+adCmd);
                    ad.setStyle(TextUtil.newStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, adCmd))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("mlmod.messages.world_list.join"))));
                    ad.append(new StringTextComponent(" §a⧉").setStyle(TextUtil.clickToCopyStyle(adCmd, "id", false)));
                    ad.append("\n");
                    adsComponent.append(ad);
                }
                ModUtils.sendMessage(adsComponent);
                break;
            default:
                break;
        }
    }
}
