package ikeyler.mlmod.Util;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextUtil {
    public static final List<String> colors = new ArrayList<>(
            Arrays.asList("§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§c", "§e", "§b", "§a", "§d", "§f", "§l", "§r", "§o", "§k", "§n", "§m", "§r")
    );
    public static String removeColors(String s) {
        return colors.stream().reduce(s, (str, color) -> str.replace(color, ""));
    }
    public static String replaceColors(String s) {
        return colors.stream().reduce(s, (str, color) -> str.replace(color.replace("§", "&"), color));
    }
    public static Style clickToViewStyle(String clickText) {
        return new StringTextComponent("").getStyle()
                .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new TranslationTextComponent("mlmod.messages.edit.lmb_to_view_text")
                ))
                .withClickEvent(new ClickEvent(
                        ClickEvent.Action.SUGGEST_COMMAND,
                        clickText
                ));
    }
}
