/*
 * Derived from Hourglass
 * https://github.com/DuckyCrayfish/hourglass
 * Copyright (C) 2021 Nick Iacullo
 *
 * This file is part of Better Days.
 *
 * Better Days is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Better Days is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Better Days.  If not, see <https://www.gnu.org/licenses/>.
 */

package betterdays.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.minecraft.resources.Identifier;

import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import technology.roughness.whitenoise.config.WhiteNoiseConfigSpec;
import technology.roughness.whitenoise.platform.Services;

import betterdays.client.gui.ScreenAlignment;
import betterdays.common.Translations;
import betterdays.message.ChatTypeOptions;
import betterdays.message.TemplateMessage;
import betterdays.time.effects.EffectCondition;
import betterdays.time.Time;
import betterdays.utils.SeasonHelper;

import static net.minecraft.resources.Identifier.tryParse;

public class ConfigHandler {

    public static final WhiteNoiseConfigSpec CLIENT_SPEC;
    public static final WhiteNoiseConfigSpec COMMON_SPEC;

    private static final Client CLIENT;
    private static final Common COMMON;

    static {
        final Pair<Client, WhiteNoiseConfigSpec> specPairClient = new WhiteNoiseConfigSpec.Builder().configure(Client::new);
        final Pair<Common, WhiteNoiseConfigSpec> specPairCommon = new WhiteNoiseConfigSpec.Builder().configure(Common::new);

        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();
        COMMON_SPEC = specPairCommon.getRight();
        COMMON = specPairCommon.getLeft();
    }

    public static void init() {
        for (String dimensionKey : CLIENT.blacklistDimensions.get()) {
            Client.blacklistDimensionsSet.add(tryParse(dimensionKey));
        }
    }

    public static class Client {

        private final WhiteNoiseConfigSpec.EnumValue<ScreenAlignment> clockAlignment;
        private final WhiteNoiseConfigSpec.IntValue clockScale;
        private final WhiteNoiseConfigSpec.IntValue clockMargin;
        private final WhiteNoiseConfigSpec.BooleanValue preventClockWobble;
        private final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> blacklistDimensions;
        private static final List<String> blacklistDimensionsList = List.of("blacklistDimensions");
        private static final String[] defaultBlacklistDimensions = new String[] {
            "aether:the_aether"
        };
        private static final Set<Identifier> blacklistDimensionsSet = Sets.newHashSet();
        private static final Predicate<Object> resourceLocationValidator = s -> s instanceof String
            && ((String) s).matches("[a-z]+[:]{1}[a-z_]+");

        public Client(WhiteNoiseConfigSpec.Builder builder) {
            builder.push("gui").comment(getTranslation("gui")); // gui

                clockAlignment = builder.comment(getTranslation("clockalignment"))
                    .defineEnum("clockAlignment", ScreenAlignment.TOP_RIGHT);

                clockScale = builder.comment(getTranslation("clockscale"))
                    .defineInRange("clockScale", 64, 1, Integer.MAX_VALUE);

                clockMargin = builder.comment(getTranslation("clockmargin"))
                    .defineInRange("clockMargin", 16, 0, Integer.MAX_VALUE);

                preventClockWobble = builder.comment(
                        getTranslation("preventclockwobble"),
                        getTranslation("preventclockwobble.comment")
                    )
                    .define("preventClockWobble", true);

                blacklistDimensions = builder.comment(getTranslation("blacklistdimensions"))
                    .defineListAllowEmpty(blacklistDimensionsList, getBlacklistDimensionsList(), resourceLocationValidator);

            builder.pop(); // gui

        }

        public static ScreenAlignment clockAlignment() {
            return CLIENT.clockAlignment.get();
        }

        public static int clockScale() {
            return CLIENT.clockScale.get();
        }

        public static int clockMargin() {
            return CLIENT.clockMargin.get();
        }

        public static boolean preventClockWobble() {
            return CLIENT.preventClockWobble.get();
        }

        public static Set<Identifier> getBlacklistDimensions() {
            return blacklistDimensionsSet;
        }

        private static Supplier<List<? extends String>> getBlacklistDimensionsList() {
            return () -> Arrays.asList(Client.defaultBlacklistDimensions);
        }

    }

    public static class Common {
        private final WhiteNoiseConfigSpec.EnumValue<SpeedMethod> speedMethod;
        private final WhiteNoiseConfigSpec.DoubleValue daySpeed;
        private final WhiteNoiseConfigSpec.DoubleValue nightSpeed;
        private final WhiteNoiseConfigSpec.DoubleValue daySpeedMinutes;
        private final WhiteNoiseConfigSpec.DoubleValue nightSpeedMinutes;
        private final WhiteNoiseConfigSpec.DoubleValue seasonDayMinutes;
        private final WhiteNoiseConfigSpec.DoubleValue seasonLatitude;
        private final WhiteNoiseConfigSpec.DoubleValue dayStart;
        private final WhiteNoiseConfigSpec.DoubleValue nightStart;

        private final WhiteNoiseConfigSpec.BooleanValue enableInterpolatedTime;
        private final WhiteNoiseConfigSpec.DoubleValue interpolatedTimeSmoothingFactor;
        private final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> interpolatedTimePairs;
        private static final List<String> defaultInterpolatedTimePairs = List.of(new String[]{
            "0,1.0",
            "24000,1.0"
        });
        private static final Predicate<Object> valuePairValidator = s -> s instanceof String
            && ((String) s).matches("\\d+,\\d*\\.?\\d+");

        private final WhiteNoiseConfigSpec.EnumValue<EffectCondition> weatherEffect;
        private final WhiteNoiseConfigSpec.EnumValue<EffectCondition> randomTickEffect;
        private final WhiteNoiseConfigSpec.IntValue baseRandomTickSpeed;
        private final WhiteNoiseConfigSpec.EnumValue<EffectCondition> potionEffect;
        private final WhiteNoiseConfigSpec.EnumValue<EffectCondition> hungerEffect;
        private final WhiteNoiseConfigSpec.EnumValue<EffectCondition> blockEntityEffect;

        private final WhiteNoiseConfigSpec.BooleanValue enableSleepFeature;
        private final WhiteNoiseConfigSpec.DoubleValue sleepSpeedMin;
        private final WhiteNoiseConfigSpec.DoubleValue sleepSpeedMax;
        private final WhiteNoiseConfigSpec.DoubleValue sleepSpeedAll;
        private final WhiteNoiseConfigSpec.DoubleValue sleepSpeedCurve;
        private final WhiteNoiseConfigSpec.BooleanValue clearWeatherOnWake;
        private final WhiteNoiseConfigSpec.BooleanValue displayBedClock;
        private final WhiteNoiseConfigSpec.DoubleValue ratioPlayersForSleep;

        private final WhiteNoiseConfigSpec.ConfigValue<String> morningMessage;
        private final WhiteNoiseConfigSpec.EnumValue<ChatTypeOptions> morningMessageType;
        private final WhiteNoiseConfigSpec.EnumValue<TemplateMessage.MessageTarget> morningMessageTarget;

        private final WhiteNoiseConfigSpec.ConfigValue<String> enterBedMessage;
        private final WhiteNoiseConfigSpec.EnumValue<ChatTypeOptions> enterBedMessageType;
        private final WhiteNoiseConfigSpec.EnumValue<TemplateMessage.MessageTarget> enterBedMessageTarget;

        private final WhiteNoiseConfigSpec.ConfigValue<String> leaveBedMessage;
        private final WhiteNoiseConfigSpec.EnumValue<ChatTypeOptions> leaveBedMessageType;
        private final WhiteNoiseConfigSpec.EnumValue<TemplateMessage.MessageTarget> leaveBedMessageTarget;

        public Common(WhiteNoiseConfigSpec.Builder builder) {
            builder.push("time").comment(getTranslation("time")); // time

            speedMethod = builder.comment(getTranslation("speedmethod"))
                    .defineEnum("speedMethod", SpeedMethod.RATIO);

            daySpeed = builder.comment(getTranslation("dayspeed"))
                .defineInRange("daySpeed", 1D, 0D, Time.DAY_LENGTH.doubleValue());

            nightSpeed = builder.comment(getTranslation("nightspeed"))
                .defineInRange("nightSpeed", 1D, 0D, Time.DAY_LENGTH.doubleValue());

            daySpeedMinutes = builder.comment(getTranslation("dayspeedminutes"))
                .defineInRange("daySpeedMinutes", 10D, 0.1D, 10000D);

            nightSpeedMinutes = builder.comment(getTranslation("nightspeedminutes"))
                .defineInRange("nightSpeedMinutes", 10D, 0.1D, 10000D);

            seasonDayMinutes = builder.comment(getTranslation("seasondayminutes"))
                .defineInRange("seasonDayMinutes", 20D, 0.1D, 10000D);

            seasonLatitude = builder.comment(getTranslation("seasonlatitude"))
                .defineInRange("seasonLatitude", 48D, -90D, 90D);

            dayStart = builder.comment(getTranslation("daystart"))
                .defineInRange("dayStart", 23500D, 22300D, 24000D);

            nightStart = builder.comment(getTranslation("nightstart"))
                .defineInRange("nightStart", 12500D, 12000D, 13000D);

            enableInterpolatedTime = builder.comment(getTranslation("enableinterpolatedtime"))
                .define("enableInterpolatedTime", false);

            interpolatedTimeSmoothingFactor = builder.comment(getTranslation("interpolatedtimesmoothingfactor"))
                .defineInRange("interpolatedTimeSmoothingFactor", 0.25, 0, 10);

            interpolatedTimePairs = builder.comment(getTranslation("interpolatedtimelist"))
                .defineList("interpolatedTimeList", defaultInterpolatedTimePairs, valuePairValidator);

            builder.push("effects").comment(getTranslation("effects")); // time.effects

            weatherEffect = builder.comment(
                    getTranslation("weathereffect"),
                    getTranslation("weathereffect.comment")
                )
                .defineEnum("weatherEffect", EffectCondition.SLEEPING);

            randomTickEffect = builder.comment(
                    getTranslation("randomtickeffect"),
                    getTranslation("randomtickeffect.comment")
                )
                .defineEnum("randomTickEffect", EffectCondition.NEVER);

            baseRandomTickSpeed = builder.comment(getTranslation("baserandomtickspeed"))
                .defineInRange("baseRandomTickSpeed", 3, 0, Integer.MAX_VALUE);

            potionEffect = builder.comment(
                    getTranslation("potioneffect"),
                    getTranslation("potioneffect.comment")
                )
                .defineEnum("potionEffect", EffectCondition.NEVER);

            hungerEffect = builder.comment(
                    getTranslation("hungereffect"),
                    getTranslation("hungereffect.comment")
                )
                .defineEnum("hungerEffect", EffectCondition.NEVER);

            blockEntityEffect = builder.comment(
                    getTranslation("blockentityeffect"),
                    getTranslation("blockentityeffect.comment")
                )
                .defineEnum("blockEntityEffect", EffectCondition.NEVER);

            builder.pop(); // time.effects
            builder.pop(); // time

            builder.push("sleep"); // sleep

            enableSleepFeature = builder.comment(getTranslation("enablesleepfeature"))
                .define("enableSleepFeature", true);

            sleepSpeedMax = builder.comment(getTranslation("sleepspeedmax"))
                .defineInRange("sleepSpeedMax", 110D, 0D, Time.DAY_LENGTH.doubleValue());

            sleepSpeedMin = builder.comment(getTranslation("sleepspeedmin"))
                .defineInRange("sleepSpeedMin", 1D, 0D, Time.DAY_LENGTH.doubleValue());

            sleepSpeedAll = builder.comment(getTranslation("sleepspeedall"))
                .defineInRange("sleepSpeedAll", -1.0D, -1.0D, Time.DAY_LENGTH.doubleValue());

            sleepSpeedCurve = builder.comment(
                    getTranslation("sleepspeedcurve"),
                    getTranslation("sleepspeedcurve.comment")
                )
                .defineInRange("sleepSpeedCurve", 0.3D, 0D, 1D);

            clearWeatherOnWake = builder.comment(getTranslation("clearweatheronwake"))
                .define("clearWeatherOnWake", true);

            displayBedClock = builder.comment(getTranslation("displaybedclock"))
                .define("displayBedClock", true);

            ratioPlayersForSleep = builder.comment(getTranslation("ratioplayersforsleep"))
                .defineInRange("ratioPlayersForSleep", 0.0D, 0D, 1D);

            // sleep.messages
            builder.comment(
                getTranslation("messages"),
                getTranslation("messages.comment")
            ).push("messages");

            // sleep.messages.morning
            builder.comment(getTranslation("morning")).push("morning");

            morningMessage = builder
                .comment(getTranslation("message", "", "were", "were"))
                .define("message", "§e§oTempus fugit!");
            morningMessageType = builder.comment(getTranslation("type"))
                .defineEnum("type", ChatTypeOptions.GAME_INFO);
            morningMessageTarget = builder
                .comment(getTranslation("target", getTranslation("target.morning")))
                .defineEnum("target", TemplateMessage.MessageTarget.DIMENSION);

            builder.pop(); // sleep.messages.morning

            // sleep.messages.enterBed
            builder.comment(getTranslation("enterbed")).push("enterBed");

            enterBedMessage = builder.comment(getTranslation("message", getTranslation("message.enterbed"), "are", "are"))
                .define("message", "${player} is now sleeping. [${sleepingPlayers}/${totalPlayers}]");
            enterBedMessageType = builder.comment(getTranslation("type"))
                .defineEnum("type", ChatTypeOptions.GAME_INFO);
            enterBedMessageTarget = builder.comment(getTranslation("target", ""))
                .defineEnum("target", TemplateMessage.MessageTarget.DIMENSION);

            builder.pop(); // sleep.messages.enterBed

            // sleep.messages.leaveBed
            builder.comment(getTranslation("leavebed")).push("leaveBed");

            leaveBedMessage = builder.comment(getTranslation("message", getTranslation("message.leavebed"), "are", "are"))
                .define("message", "${player} has left their bed. [${sleepingPlayers}/${totalPlayers}]");
            leaveBedMessageType = builder.comment(getTranslation("type"))
                .defineEnum("type", ChatTypeOptions.GAME_INFO);
            leaveBedMessageTarget = builder.comment(getTranslation("target", ""))
                .defineEnum("target", TemplateMessage.MessageTarget.DIMENSION);

            builder.pop(); // sleep.messages.leaveBed
            builder.pop(); // sleep.messages
            builder.pop(); // sleep
        }

        public static double daySpeed(Level level) {
            if (COMMON.speedMethod.get() == SpeedMethod.MINUTES) {
                return (100F / COMMON.daySpeedMinutes.get()) / 10F;
            }
            else if (COMMON.speedMethod.get() == SpeedMethod.REALTIME) {
                return (100F / 720F) / 10F;
            }
            else if (COMMON.speedMethod.get() == SpeedMethod.SEASON) {
                return (100F / (COMMON.seasonDayMinutes.get() * SeasonHelper.getDayRatio(level, COMMON.seasonLatitude.get()))) / 10F;
            }

            return COMMON.daySpeed.get();
        }

        public static double nightSpeed(Level level) {
            if (COMMON.speedMethod.get() == SpeedMethod.MINUTES) {
                return (100F / COMMON.nightSpeedMinutes.get()) / 10F;
            }
            else if (COMMON.speedMethod.get() == SpeedMethod.REALTIME) {
                return (100F / 720F) / 10F;
            }
            else if (COMMON.speedMethod.get() == SpeedMethod.SEASON) {
                return (100F / (COMMON.seasonDayMinutes.get() * SeasonHelper.getNightRatio(level, COMMON.seasonLatitude.get()))) / 10F;
            }

            return COMMON.nightSpeed.get();
        }

        public static double dayStart() {
            return COMMON.dayStart.get();
        }

        public static double nightStart() {
            return COMMON.nightStart.get();
        }

        public static boolean enableInterpolatedTime() {
            return COMMON.enableInterpolatedTime.get();
        }

        public static double interpolatedTimeSmoothingFactor() {
            return COMMON.interpolatedTimeSmoothingFactor.get();
        }

        public static List<Pair<Integer,Double>> interpolatedTimePairs() {
            return pairTransformer.apply(COMMON.interpolatedTimePairs.get());
        }

        public static EffectCondition weatherEffect() {
            return COMMON.weatherEffect.get();
        }

        public static EffectCondition randomTickEffect() {
            return COMMON.randomTickEffect.get();
        }

        public static int baseRandomTickSpeed() {
            return COMMON.baseRandomTickSpeed.get();
        }

        public static EffectCondition potionEffect() {
            return COMMON.potionEffect.get();
        }

        public static EffectCondition hungerEffect() {
            return COMMON.hungerEffect.get();
        }

        public static EffectCondition blockEntityEffect() {
            return COMMON.blockEntityEffect.get();
        }

        public static boolean enableSleepFeature() {
            boolean sleepModLoaded = Services.PLATFORM.isModLoaded("sleepwarp")
                || Services.PLATFORM.isModLoaded("sleep_tight");

            return !sleepModLoaded ? COMMON.enableSleepFeature.get() : false;
        }

        public static double sleepSpeedMin() {
            return COMMON.sleepSpeedMin.get();
        }

        public static double sleepSpeedMax() {
            return COMMON.sleepSpeedMax.get();
        }

        public static double sleepSpeedAll() {
            return COMMON.sleepSpeedAll.get();
        }

        public static double sleepSpeedCurve() {
            return COMMON.sleepSpeedCurve.get();
        }

        public static boolean clearWeatherOnWake() {
            return COMMON.clearWeatherOnWake.get();
        }

        public static boolean displayBedClock() {
            return COMMON.displayBedClock.get();
        }

        public static double percentPlayersForSleep() {
            return COMMON.ratioPlayersForSleep.get();
        }

        public static String morningMessage() {
            return COMMON.morningMessage.get();
        }

        public static ChatTypeOptions morningMessageType() {
            return COMMON.morningMessageType.get();
        }

        public static TemplateMessage.MessageTarget morningMessageTarget() {
            return COMMON.morningMessageTarget.get();
        }

        public static String enterBedMessage() {
            return COMMON.enterBedMessage.get();
        }

        public static ChatTypeOptions enterBedMessageType() {
            return COMMON.enterBedMessageType.get();
        }

        public static TemplateMessage.MessageTarget enterBedMessageTarget() {
            return COMMON.enterBedMessageTarget.get();
        }

        public static String leaveBedMessage() {
            return COMMON.leaveBedMessage.get();
        }

        public static ChatTypeOptions leaveBedMessageType() {
            return COMMON.leaveBedMessageType.get();
        }

        public static TemplateMessage.MessageTarget leaveBedMessageTarget() {
            return COMMON.leaveBedMessageTarget.get();
        }

        private static final Function<List<? extends String>, List<Pair<Integer, Double>>> pairTransformer =
            list -> list.stream()
                    .map(entry -> {
                        String[] parts = entry.split(",");
                        if (parts.length == 2) {
                            try {
                                int first = Integer.parseInt(parts[0].trim());
                                double second = Double.parseDouble(parts[1].trim());
                                return Pair.of(first, second);
                            } catch (NumberFormatException ignored) { }
                        }
                        return null;
                    })
                    .collect(Collectors.toList());
    }

    private static String getTranslation(String key) {
        return Translations.get(key);
    }

    private static String getTranslation(String key, String... values) {
        return Translations.get(key, values);
    }

}
