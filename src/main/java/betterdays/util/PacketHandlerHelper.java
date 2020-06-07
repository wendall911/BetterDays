package betterdays.util;

import betterdays.BetterDays;
import betterdays.config.SyncedConfig;
import betterdays.config.TimeOption;
import betterdays.handler.message.ConfigUpdate;
import betterdays.handler.PacketHandler;

import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.fml.network.NetworkDirection;

public class PacketHandlerHelper {

    public static void sendServerConfigValues(ServerPlayerEntity player) {
        CompoundNBT data = new CompoundNBT();

        BetterDays.logger.info("Sending server config values to player.");

        data.putString(TimeOption.TIME_MODE.getName(),
                String.valueOf(SyncedConfig.getValue(TimeOption.TIME_MODE)));
        data.putString(TimeOption.SUNRISE_QUARTER_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.SUNRISE_QUARTER_LENGTH)));
        data.putString(TimeOption.DAY_QUARTER_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.DAY_QUARTER_LENGTH)));
        data.putString(TimeOption.SUNSET_QUARTER_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.SUNSET_QUARTER_LENGTH)));
        data.putString(TimeOption.NIGHT_QUARTER_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.NIGHT_QUARTER_LENGTH)));
        data.putString(TimeOption.DAY_HALF_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.DAY_HALF_LENGTH)));
        data.putString(TimeOption.NIGHT_HALF_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.NIGHT_HALF_LENGTH)));
        data.putString(TimeOption.DAY_FULL_LENGTH.getName(),
                String.valueOf(SyncedConfig.getDoubleValue(TimeOption.DAY_FULL_LENGTH)));

        PacketHandler.INSTANCE.sendTo(new ConfigUpdate(data), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

}

