package betterdays.config;

import betterdays.util.PacketHandlerHelper;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TimeConfig {

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final String CATEGORY_TIME = "Time Settings";
    private static final String CATEGORY_FULL_DAY = "FULL_DAY";
    private static final String CATEGORY_HALF_DAY = "HALF_DAY";
    private static final String CATEGORY_QUARTER_DAY = "QUARTER_DAY";

    private static ForgeConfigSpec.ConfigValue<String> timeMode;
    private static final List<String> modes = Arrays.asList("FULL_DAY", "HALF_DAY", "QUARTER_DAY");

    private static ForgeConfigSpec.ConfigValue<Double> dayFullLength;

    private static ForgeConfigSpec.ConfigValue<Double> dayHalfLength;
    private static ForgeConfigSpec.ConfigValue<Double> nightHalfLength;

    private static ForgeConfigSpec.ConfigValue<Double> sunriseQuarterLength;
    private static ForgeConfigSpec.ConfigValue<Double> dayQuarterLength;
    private static ForgeConfigSpec.ConfigValue<Double> sunsetQuarterLength;
    private static ForgeConfigSpec.ConfigValue<Double> nightQuarterLength;

    static {
        BUILDER.push(CATEGORY_TIME);
        timeMode = BUILDER.comment("Time mode. See comments below for details. One of: " + modes.toString()).defineInList("timeMode", "QUARTER_DAY", modes);
        BUILDER.pop();

        BUILDER.comment("Sets time for an entire minecraft day.").push(CATEGORY_FULL_DAY);
        dayFullLength = BUILDER.comment("Length in minutes from 0 to 24000 ticks. 1200s").define("dayFullLength", 1200.0);
        BUILDER.pop();

        BUILDER.comment("Sets time for day and night separately.").push(CATEGORY_HALF_DAY);
        dayHalfLength = BUILDER.comment("Length in minutes from 0 to 12000 ticks. 600s").define("dayHalfLength", 600.0);
        nightHalfLength = BUILDER.comment("Length in minutes from 12000 to 24000 ticks. 600s").define("nightHalfLength", 600.0);
        BUILDER.pop();

        BUILDER.comment("Sets time for sunrise, day, sunset and night separately.\nSee https://minecraft.gamepedia.com/Day-night_cycle for more details.").push(CATEGORY_QUARTER_DAY);
        sunriseQuarterLength = BUILDER.comment("Length in minutes from 23000 to 1000 ticks. 100.0s").define("sunriseQuarterLength", 100.0);
        dayQuarterLength = BUILDER.comment("Length in minutes from 1000 to 12000 ticks. 550s").define("dayQuarterLength", 550.0);
        sunsetQuarterLength = BUILDER.comment("Length in minutes from 12000 to 12542 ticks. 27.1s").define("sunsetQuarterLength", 27.1);
        nightQuarterLength = BUILDER.comment("Length in minutes from 12542 to 23000 ticks. 522.9s").define("nightQuarterLength", 522.9);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        addOption(TimeOption.TIME_MODE, timeMode);
        addOption(TimeOption.DAY_FULL_LENGTH, dayFullLength);
        addOption(TimeOption.DAY_HALF_LENGTH, dayHalfLength);
        addOption(TimeOption.NIGHT_HALF_LENGTH, nightHalfLength);
        addOption(TimeOption.SUNRISE_QUARTER_LENGTH, sunriseQuarterLength);
        addOption(TimeOption.DAY_QUARTER_LENGTH, dayQuarterLength);
        addOption(TimeOption.SUNSET_QUARTER_LENGTH, sunsetQuarterLength);
        addOption(TimeOption.NIGHT_QUARTER_LENGTH, nightQuarterLength);
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event) {
        updateOption(TimeOption.TIME_MODE, timeMode);
        updateOption(TimeOption.DAY_FULL_LENGTH, dayFullLength);
        updateOption(TimeOption.DAY_HALF_LENGTH, dayHalfLength);
        updateOption(TimeOption.NIGHT_HALF_LENGTH, nightHalfLength);
        updateOption(TimeOption.SUNRISE_QUARTER_LENGTH, sunriseQuarterLength);
        updateOption(TimeOption.DAY_QUARTER_LENGTH, dayQuarterLength);
        updateOption(TimeOption.SUNSET_QUARTER_LENGTH, sunsetQuarterLength);
        updateOption(TimeOption.NIGHT_QUARTER_LENGTH, nightQuarterLength);

		MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
		if (currentServer == null) return;

		PlayerList players = currentServer.getPlayerList();
		for (PlayerEntity player : players.getPlayers()) {
            PacketHandlerHelper.sendServerConfigValues((ServerPlayerEntity)player);
		}
	}

    public static void addOption(ISyncedOption option, ForgeConfigSpec.ConfigValue<?> value) {
        SyncedConfig.addOption(option, String.valueOf(value.get()));
    }

    public static void updateOption(ISyncedOption option, ForgeConfigSpec.ConfigValue<?> value) {
        SyncedConfig.SyncedConfigOption entry = SyncedConfig.getEntry(option.getName());
        entry.value = String.valueOf(value.get());
    }

}
