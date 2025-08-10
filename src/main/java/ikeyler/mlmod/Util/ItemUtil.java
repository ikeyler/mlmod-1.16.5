package ikeyler.mlmod.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    // todo item lore editor
    static Minecraft mc = Minecraft.getInstance();

    public static int findFreeSlot() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.inventory.getItem(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    public static void updateInventory(int slot, ItemStack item) {
        if (mc.hasSingleplayerServer()) {
            mc.getSingleplayerServer().getPlayerList().getPlayerByName(mc.player.getName().getString())
                    .inventoryMenu.setItem(slot, item);
        }
        else {
            try {mc.getConnection().send(new CCreativeInventoryActionPacket(slot, item));} catch (Exception ignore) {}
        }
    }
    public static List<ITextComponent> getItemLore(ItemStack stack) {
        List<ITextComponent> lore = new ArrayList<>();
        if (stack.hasTag() && stack.getTag().contains("display")) {
            CompoundNBT display = stack.getTag().getCompound("display");
            if (display.contains("Lore")) {
                ListNBT loreTag = display.getList("Lore", 8);
                for (int i = 0; i < loreTag.size(); i++) {
                    lore.add(ITextComponent.Serializer.fromJson(loreTag.getString(i)));
                }
            }
        }
        return lore;
    }
    public static void setItemLore(ItemStack stack, List<ITextComponent> lore) {
        CompoundNBT display = stack.getOrCreateTagElement("display");
        ListNBT loreTag = new ListNBT();
        for (ITextComponent line:lore) {
            loreTag.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(line)));
        }
        display.put("Lore", loreTag);
    }
    public static void addItemLore(ItemStack stack, ITextComponent lore) {
        CompoundNBT display = stack.getOrCreateTagElement("display");
        ListNBT loreTag = display.getList("Lore", 8);
        loreTag.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(lore)));
        display.put("Lore", loreTag);
    }
    public static void clearItemLore(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("display")) {
            stack.getTag().getCompound("display").remove("Lore");
        }
    }
}
