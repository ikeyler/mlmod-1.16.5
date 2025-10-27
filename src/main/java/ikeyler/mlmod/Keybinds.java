package ikeyler.mlmod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {
    public static final KeyBinding hub = new KeyBinding("/hub", InputMappings.UNKNOWN.getValue(), "MLMod");
    public static final KeyBinding play = new KeyBinding("/play", InputMappings.UNKNOWN.getValue(), "MLMod");
    public static final KeyBinding build = new KeyBinding("/build", InputMappings.UNKNOWN.getValue(), "MLMod");
    public static final KeyBinding dev = new KeyBinding("/dev", InputMappings.UNKNOWN.getValue(), "MLMod");

    static final KeyBinding[] list = new KeyBinding[]{hub, play, build, dev};
    public static void register() {
        for (KeyBinding bind:list) {
            ClientRegistry.registerKeyBinding(bind);
        }
    }
}
