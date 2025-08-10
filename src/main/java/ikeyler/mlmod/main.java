package ikeyler.mlmod;

import ikeyler.mlmod.Configuration.ClothConfig;
import ikeyler.mlmod.Configuration.Config;
import ikeyler.mlmod.Messages.Manager;
import ikeyler.mlmod.Messages.Messages;
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
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String prefix = "§8» §f";
    public static final String MOD_ID = "mlmod";
    public static final String NAME = "MLMod";
    public static final String VERSION = "0.7.1";
    public static final Manager messageManager = new Manager();

    public main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.spec);
        MinecraftForge.EVENT_BUS.register(new EventListener());
        messageManager.addMessages(Messages.MESSAGES);
        messageManager.addMessages(Messages.AD_MESSAGES);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> ClothConfig.buildConfigScreen(Minecraft.getInstance().screen));
        Messages.updateMessages();
    }
}
