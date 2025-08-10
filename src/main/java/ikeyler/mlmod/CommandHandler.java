package ikeyler.mlmod;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static ikeyler.mlmod.main.prefix;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class CommandHandler {
    //дерьмо не работающее на серверах suka

    static Minecraft mc = Minecraft.getInstance();
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("edit")
                        .executes(ctx -> {
                            String name = mc.player.getMainHandItem().getHoverName().getString();
                            ctx.getSource().sendSuccess(
                                    new StringTextComponent(prefix + name)
                                            .withStyle(style -> style
                                                    .withHoverEvent(new HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            new StringTextComponent("§7ЛКМ§a, чтобы вывести текст в чат")
                                                    ))
                                                    .withClickEvent(new ClickEvent(
                                                            ClickEvent.Action.SUGGEST_COMMAND,
                                                            name.replace("§", "&")
                                                    ))
                                            ),
                                    true
                            );
                            return 0;
                        })
                        .then(Commands.argument("value", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String newName = StringArgumentType.getString(ctx, "value");
                                    String prevName = mc.player.getMainHandItem().getHoverName().getString();
                                    ItemStack stack = mc.player.getMainHandItem();
                                    stack.setHoverName(new StringTextComponent(newName));
                                    mc.player.inventory.setItem(mc.player.inventory.selected, stack);
                                    mc.player.inventory.setChanged();
                                    int slot = mc.player.inventory.selected+36;
                                    mc.getConnection().send(new CCreativeInventoryActionPacket(slot, stack));
                                    ctx.getSource().sendSuccess(
                                            new StringTextComponent(prefix + "Старое название: §7[" + prevName + "§7]")
                                                    .withStyle(style -> style
                                                            .withHoverEvent(new HoverEvent(
                                                                    HoverEvent.Action.SHOW_TEXT,
                                                                    new StringTextComponent("§7ЛКМ§a, чтобы вывести текст в чат")
                                                            ))
                                                            .withClickEvent(new ClickEvent(
                                                                    ClickEvent.Action.SUGGEST_COMMAND,
                                                                    "/edit " + prevName.replace("§", "&")
                                                            ))
                                                    ),
                                            true
                                    );
                                    return 0;
                                })
                        )

        );
    }
}
