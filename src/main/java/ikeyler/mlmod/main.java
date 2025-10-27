package ikeyler.mlmod;

import ikeyler.mlmod.cfg.ClothConfig;
import ikeyler.mlmod.cfg.Config;
import ikeyler.mlmod.messages.MessageCollector;
import ikeyler.mlmod.messages.Manager;
import ikeyler.mlmod.messages.Messages;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mlmod")
public class main {
    public static final Logger logger = LogManager.getLogger();
    public static final String prefix = "§8» §f";
    public static final Manager messageManager = new Manager();
    public static final MessageCollector messageCollector = new MessageCollector();

    public main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.spec);
        MinecraftForge.EVENT_BUS.register(new EventListener());
        messageManager.addMessages(Messages.MESSAGES);
        messageManager.addMessages(Messages.AD_MESSAGES);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> ClothConfig.buildConfigScreen(Minecraft.getInstance().screen));
        Messages.updateMessages();
        Keybinds.register();
    }
}
