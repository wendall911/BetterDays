package betterdays.handler.message;

import betterdays.BetterDays;
import betterdays.config.SyncedConfig;

import net.minecraft.nbt.CompoundNBT;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfigUpdate {

    private CompoundNBT data;

    public ConfigUpdate(CompoundNBT data) {
        this.data = data.copy();
    }

    public static void encode(ConfigUpdate packet, PacketBuffer buf) {
        buf.writeNbt(packet.data);
    }

    public static ConfigUpdate decode(PacketBuffer buf) {
        return new ConfigUpdate(buf.readNbt());
    }

    public static class Handler {

        public static void handle(final ConfigUpdate message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                BetterDays.logger.info("Applying config updates from server.");
                for (String key : message.data.getAllKeys()) {
                    SyncedConfig.SyncedConfigOption entry = SyncedConfig.getEntry(key);
                    entry.value = message.data.getString(key);
                }
            });
            ctx.get().setPacketHandled(true);
        }

    }

}
