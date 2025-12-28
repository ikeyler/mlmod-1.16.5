package ikeyler.mlmod.variables;

import ikeyler.mlmod.Main;
import ikeyler.mlmod.itemeditor.ItemEditor;
import ikeyler.mlmod.util.ItemUtil;
import ikeyler.mlmod.util.TextUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;

import java.util.Arrays;

import static ikeyler.mlmod.util.ModUtils.VAR_SEPARATOR;

public class Variable {

    private final VariableType type;
    private final String name;
    private final String nbt;
    public Variable(VariableType type, String name, String nbt) {
        this.type = type;
        this.name = name.replace(VAR_SEPARATOR, ":\\:");
        this.nbt = nbt;
    }

    public VariableType getType() {
        return this.type;
    }
    public String getName() {
        return this.name;
    }
    public String getFixedName() {
        return this.name.replace(":\\:", VAR_SEPARATOR);
    }
    public String getNbt() {
        return this.nbt;
    }

    public static Variable fromString(String s) {
        String[] split = s.split(VAR_SEPARATOR, 3);
        if (!s.contains(VAR_SEPARATOR) || split.length < 3) return null;
        VariableType type = Arrays.stream(VariableType.values()).filter(t -> split[0].toUpperCase().equals(t.name())).findFirst().orElse(null);
        if (type == null) return null;
        return new Variable(type, split[1].replace(":\\:", VAR_SEPARATOR), split[2]);
    }

    public static Variable fromItem(ItemStack item) {
        VariableType type = Arrays.stream(VariableType.values()).filter
                (t -> t.getItemStack().getItem() == item.getItem()).findFirst().orElse(null);
        if (type == null || !item.hasTag()) return null;

        if (type == VariableType.VAR_UNSAVED || type == VariableType.ARRAY_CONST) {
            if (item.sameItemStackIgnoreDurability(Items.MAGMA_CREAM.getDefaultInstance())) {
                String itemLore = ItemEditor.getLore(item).toString();
                type = (itemLore.contains("§dСОХРАНЕНО") || itemLore.contains("§dSAVED")) ? VariableType.VAR_SAVED : VariableType.VAR_UNSAVED;
            }
            else type = item.getDisplayName().getString().contains("⎘") ? VariableType.ARRAY_CONST : VariableType.ARRAY_TEMP;
        }
        return new Variable(type, TextUtil.getFormattedText(item.getHoverName()), item.getTag().toString());
    }

    public static ItemStack itemFromVariable(Variable variable) {
        ItemStack item = variable.getType().getItemStack();
        try {
            item.setTag(JsonToNBT.parseTag(variable.nbt));
            return item;
        }
        catch (Exception e) {
            Main.logger.error("failed to parse nbt:", e);
            return null;
        }
    }

    public enum VariableType {
        TEXT(Items.BOOK),
        NUMBER(Items.SLIME_BALL),
        LOCATION(Items.PAPER),
        VAR_UNSAVED(ItemUtil.getDynamicVar(false).getItem()),
        VAR_SAVED(ItemUtil.getDynamicVar(true).getItem()),
        ARRAY_CONST(Items.ITEM_FRAME),
        ARRAY_TEMP(Items.ITEM_FRAME),
        COMPONENT(Items.SHULKER_SHELL),
        VECTOR(Items.PRISMARINE_SHARD),
        GAME_VALUE(Items.APPLE),
        PARTICLE(Items.NETHER_STAR),
        POTION(Items.POTION);
        private final Item item;
        VariableType(Item item) {
            this.item = item;
        }
        public ItemStack getItemStack() {
            return item.getDefaultInstance();
        }
    }
}
