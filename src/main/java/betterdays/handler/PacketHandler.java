package betterdays.handler;

import betterdays.BetterDays;
import betterdays.handler.message.ConfigUpdate;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    public static SimpleChannel INSTANCE;    
    private static String version = "1.0";
    private static int ID;

    public static void setup() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BetterDays.MODID, "main"), () -> version, s -> true, s -> true);
        INSTANCE.registerMessage(ID++, ConfigUpdate.class, ConfigUpdate::encode, ConfigUpdate::decode, ConfigUpdate.Handler::handle);
    }
}
