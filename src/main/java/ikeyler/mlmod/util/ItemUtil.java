package ikeyler.mlmod.util;

import com.mojang.authlib.GameProfile;
import ikeyler.mlmod.Main;
import ikeyler.mlmod.itemeditor.ItemEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtil {

    static Minecraft mc = Minecraft.getInstance();
    public static void updateSlot(int slot, ItemStack item) {
        try {
            mc.getConnection().send(new CCreativeInventoryActionPacket(slot, item));
        } catch (Exception e) {
            Main.logger.error("failed to send inventory packet:", e);
        }
    }

    public static ItemStack getPlayerHead(String playerName) {
        // todo need to get player uuid from https://api.mojang.com/users/profiles/minecraft/
        // and write it to the itemstack
        ItemStack head = new ItemStack(Items.PLAYER_HEAD, 1);
        CompoundNBT tag = head.getOrCreateTag();
        head.setHoverName(new StringTextComponent(playerName));
        GameProfile profile = new GameProfile(null, playerName);
        mc.getMinecraftSessionService().fillProfileProperties(profile, true);
        tag.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), profile));
        return head;
    }
    public static ItemStack getDynamicVar(boolean saved) {
        ItemStack item = new ItemStack(Items.MAGMA_CREAM);
        List<String> lore = new ArrayList<>();
        if (saved) {
            CompoundNBT display = item.getOrCreateTagElement("display");
            display.putString("LocName", "save");
            lore.add(new TranslationTextComponent("mlmod.var_saved").getString());
        }
        lore.addAll(Arrays.asList(new TranslationTextComponent("mlmod.var.var.desc").getString().split("\n")));
        ItemEditor.setLore(item, lore);
        return item;
    }
}
