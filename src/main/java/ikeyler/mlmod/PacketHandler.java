package ikeyler.mlmod;

import ikeyler.mlmod.cfg.Config;
import ikeyler.mlmod.util.ModUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PacketHandler {
    private final Minecraft mc = Minecraft.getInstance();
    private final String packetName = "packet_interceptor";

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientConnected(ClientPlayerNetworkEvent.LoggedInEvent event) {
        NetworkManager manager = event.getNetworkManager();
        if (manager == null || !Config.DEV_NIGHT_MODE.get()) return;
        ChannelPipeline pipeline = manager.channel().pipeline();
        if (pipeline.get(packetName) == null) {
            pipeline.addBefore("packet_handler", packetName, new ChannelInboundHandlerAdapter() {
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    if (msg instanceof SUpdateTimePacket && ModUtils.NIGHT_DEV_MODE) {
                        if (mc.level != null)
                            mc.level.setDayTime(Config.DEV_NIGHT_MODE_TIME.get());
                        return;
                    }
                    super.channelRead(ctx, msg);
                }
            });
        }
    }
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientDisconnected(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        NetworkManager manager = event.getNetworkManager();
        if (manager != null) {
            ChannelPipeline pipeline = manager.channel().pipeline();
            if (pipeline.get(packetName) != null) {
                pipeline.remove(packetName);
            }
        }
    }
}
