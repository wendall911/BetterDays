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

import betterdays.platform.Services;
import org.apache.commons.lang3.tuple.Pair;

import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;

import betterdays.client.gui.ScreenAlignment;
import betterdays.message.ChatTypeOptions;
import betterdays.message.TemplateMessage;
import betterdays.time.effects.EffectCondition;
import betterdays.time.Time;

public class ConfigHandler {

    public static final SpectreConfigSpec CLIENT_SPEC;
    public static final SpectreConfigSpec COMMON_SPEC;

    private static final Client CLIENT;
    private static final Common COMMON;

    static {
        final Pair<Client, SpectreConfigSpec> specPairClient = new SpectreConfigSpec.Builder().configure(Client::new);
        final Pair<Common, SpectreConfigSpec> specPairCommon = new SpectreConfigSpec.Builder().configure(Common::new);

        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();
        COMMON_SPEC = specPairCommon.getRight();
        COMMON = specPairCommon.getLeft();
    }

    public static void init() {}

    public static class Client {

        private final SpectreConfigSpec.EnumValue<ScreenAlignment> clockAlignment;
        private final SpectreConfigSpec.IntValue clockScale;
        private final SpectreConfigSpec.IntValue clockMargin;
        private final SpectreConfigSpec.BooleanValue preventClockWobble;

        public Client(SpectreConfigSpec.Builder builder) {
            builder.push("gui"); // gui

                clockAlignment = builder.comment("Sets the screen alignment of the bed clock.")
                    .defineEnum("clockAlignment", ScreenAlignment.TOP_RIGHT);

                clockScale = builder.comment("Sets the scale of the bed clock.")
                    .defineInRange("clockScale", 64, 1, Integer.MAX_VALUE);

                clockMargin = builder.comment(
                    "Sets the distance between the clock and the edge of the screen.",
                    "Unused if clockAlignment is CENTER_CENTER.")
                    .defineInRange("clockMargin", 16, 0, Integer.MAX_VALUE);

                preventClockWobble = builder.comment(
                    "This setting prevents clock wobble when getting in bed by updating the clock's position every tick.",
                    "As a side-effect, the clock won't wobble when first viewed as it does in vanilla. This setting is",
                    "unused if displayBedClock is false.")
                    .define("preventClockWobble", true);

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

    }

    public static class Common {
        private final SpectreConfigSpec.DoubleValue daySpeed;
        private final SpectreConfigSpec.DoubleValue nightSpeed;

        private final SpectreConfigSpec.EnumValue<EffectCondition> weatherEffect;
        private final SpectreConfigSpec.EnumValue<EffectCondition> randomTickEffect;
        private final SpectreConfigSpec.IntValue baseRandomTickSpeed;
        private final SpectreConfigSpec.EnumValue<EffectCondition> potionEffect;
        private final SpectreConfigSpec.EnumValue<EffectCondition> hungerEffect;
        private final SpectreConfigSpec.EnumValue<EffectCondition> blockEntityEffect;

        private final SpectreConfigSpec.BooleanValue enableSleepFeature;
        private final SpectreConfigSpec.DoubleValue sleepSpeedMin;
        private final SpectreConfigSpec.DoubleValue sleepSpeedMax;
        private final SpectreConfigSpec.DoubleValue sleepSpeedAll;
        private final SpectreConfigSpec.DoubleValue sleepSpeedCurve;
        private final SpectreConfigSpec.BooleanValue clearWeatherOnWake;
        private final SpectreConfigSpec.BooleanValue displayBedClock;
        private final SpectreConfigSpec.BooleanValue allowDaySleep;

        private final SpectreConfigSpec.ConfigValue<String> morningMessage;
        private final SpectreConfigSpec.EnumValue<ChatTypeOptions> morningMessageType;
        private final SpectreConfigSpec.EnumValue<TemplateMessage.MessageTarget> morningMessageTarget;

        private final SpectreConfigSpec.ConfigValue<String> enterBedMessage;
        private final SpectreConfigSpec.EnumValue<ChatTypeOptions> enterBedMessageType;
        private final SpectreConfigSpec.EnumValue<TemplateMessage.MessageTarget> enterBedMessageTarget;

        private final SpectreConfigSpec.ConfigValue<String> leaveBedMessage;
        private final SpectreConfigSpec.EnumValue<ChatTypeOptions> leaveBedMessageType;
        private final SpectreConfigSpec.EnumValue<TemplateMessage.MessageTarget> leaveBedMessageTarget;

        public Common(SpectreConfigSpec.Builder builder) {
            builder.push("time"); // time

            daySpeed = builder.comment(
                            "The speed at which time passes during the day.",
                            "Day is defined as any time between 23500 (middle of dawn) and 12500 (middle of dusk) the next day.",
                            "Vanilla speed: 1.0")
                    .defineInRange("daySpeed", 1D, 0D, Time.DAY_LENGTH.doubleValue());

            nightSpeed = builder.comment(
                            "The speed at which time passes during the night.",
                            "Night is defined as any time between 12500 (middle of dusk) and 23500 (middle of dawn).",
                            "Vanilla speed: 1.0")
                    .defineInRange("nightSpeed", 1D, 0D, Time.DAY_LENGTH.doubleValue());

            builder.push("effects"); // time.effects

            weatherEffect = builder.comment(
                            "When applied, this effect syncs the passage of weather with the current speed of time.",
                            "I.e., as time moves faster, rain stops faster. Clear weather is not affected.",
                            "When set to SLEEPING, this effect only applies when at least one player is sleeping in a dimension.",
                            "Note: This setting is not applicable if game rule doWeatherCycle is false.")
                    .defineEnum("weatherEffect", EffectCondition.SLEEPING);

            randomTickEffect = builder.comment(
                            "When applied, this effect syncs the random tick speed with the current speed of time, forcing",
                            "crop, tree, and grass growth to occur at baseRandomTickSpeed multiplied by the current time-speed.",
                            "When set to SLEEPING, randomTickSpeed is set to baseRandomTickSpeed unless at least one player is sleeping in a dimension.",
                            "More information on the effects of random tick speed can be found here: https://minecraft.fandom.com/wiki/Tick#Random_tick",
                            "WARNING: This setting overwrites the randomTickSpeed game rule. To modify the base random tick speed,",
                            "use the baseRandomTickSpeed setting instead of changing the game rule directly.")
                    .defineEnum("randomTickEffect", EffectCondition.NEVER);

            baseRandomTickSpeed = builder
                    .comment("The base random tick speed used by the randomTickEffect time effect.")
                    .defineInRange("baseRandomTickSpeed", 3, 0, Integer.MAX_VALUE);

            potionEffect = builder.comment(
                            "When applied, this effect progresses potion effects to match the rate of the current time-speed.",
                            "This effect does not apply if time speed is 1.0 or less.",
                            "THIS MAY HAVE A NEGATIVE IMPACT ON PERFORMANCE IN SERVERS WITH MANY PLAYERS.",
                            "When set to ALWAYS, this effect applies to all players in the dimension, day or night.",
                            "When set to SLEEPING, this effect only applies to players who are sleeping.")
                    .defineEnum("potionEffect", EffectCondition.NEVER);

            hungerEffect = builder.comment(
                            "When applied, this effect progresses player hunger effects to match the rate of the current time-speed.",
                            "This results in faster healing when food level is full, and faster harm when food level is too low.",
                            "This effect does not apply if time speed is 1.0 or less.",
                            "When set to ALWAYS, this effect applies to all players in the dimension, day or night. Not recommended on higher difficulty settings",
                            "When set to SLEEPING, this effect only applies to players who are sleeping.")
                    .defineEnum("hungerEffect", EffectCondition.NEVER);

            blockEntityEffect = builder.comment(
                            "When applied, this effect progresses block entities like furnaces, hoppers, and spawners to match the rate of the current time-speed.",
                            "WARNING: This time-effect has a significant impact on performance.",
                            "This effect does not apply if time speed is 1.0 or less.",
                            "When set to SLEEPING, this effect only applies when at least one player is sleeping in a dimension.")
                    .defineEnum("blockEntityEffect", EffectCondition.NEVER);

            builder.pop(); // time.effects
            builder.pop(); // time

            builder.push("sleep"); // sleep

            enableSleepFeature = builder.comment(
                            "Enables or disables the sleep feature of this mod. Enabling this setting will modify the vanilla sleep functionality",
                            "and may conflict with other sleep mods. If disabled, all settings in the sleep section will not apply.")
                    .define("enableSleepFeature", true);

            sleepSpeedMax = builder.comment(
                            "## THIS SETTING DEFINES THE SLEEP TIME-SPEED IN SINGLE-PLAYER GAMES ###",
                            "The maximum speed at which time passes when all players are sleeping.",
                            "A value of 110 is nearly equal to the time it takes to sleep in vanilla.")
                    .defineInRange("sleepSpeedMax", 110D, 0D, Time.DAY_LENGTH.doubleValue());

            sleepSpeedMin = builder
                    .comment("The minimum speed at which time passes when only 1 player is sleeping in a full server.")
                    .defineInRange("sleepSpeedMin", 1D, 0D, Time.DAY_LENGTH.doubleValue());

            sleepSpeedAll = builder.comment(
                            "The speed at which time passes when all players are sleeping.",
                            "Set to -1 to disable this feature (sleepSpeedMax will be used when all players are sleeping).")
                    .defineInRange("sleepSpeedAll", -1.0D, -1.0D, Time.DAY_LENGTH.doubleValue());

            sleepSpeedCurve = builder.comment(
                            "This parameter defines the curvature of the interpolation function that translates the sleeping player percentage into time-speed.",
                            "The function used is a Normalized Tunable Sigmoid Function.",
                            "A value of 0.5 represents a linear relationship.",
                            "Smaller values bend the curve toward the X axis, while greater values bend it toward the Y axis.",
                            "This graph may be used as a reference for tuning the curve: https://www.desmos.com/calculator/w8gntxzfow",
                            "Credit to Dino Dini for the function: https://dinodini.wordpress.com/2010/04/05/normalized-tunable-sigmoid-functions/",
                            "Credit to SmoothSleep for the idea: https://www.spigotmc.org/resources/smoothsleep.32043/")
                    .defineInRange("sleepSpeedCurve", 0.3D, 0D, 1D);

            clearWeatherOnWake = builder.comment(
                            "Set to 'true' for the weather to clear when players wake up in the morning as it does in vanilla.",
                            "Set to 'false' to force weather to pass naturally. Adds realism when accelerateWeather is enabled.",
                            "Note: This setting is ignored if game rule doWeatherCycle is false.")
                    .define("clearWeatherOnWake", true);

            allowDaySleep = builder.comment(
                            "When true, players are allowed to sleep at all times of day in dimensions controlled by Better Days.",
                            "Note: Other mods may override this ability.")
                    .define("allowDaySleep", false);

            displayBedClock = builder
                    .comment("When true, a clock is displayed in the sleep interface.")
                    .define("displayBedClock", true);

            // sleep.messages
            builder.comment(
                            "This section defines settings for notification messages.",
                            "All messages support Minecraft formatting codes (https://minecraft.fandom.com/wiki/Formatting_codes).",
                            "All messages have variables that can be inserted using the following format: ${variableName}",
                            "The type option controls where the message appears:",
                            "\tSYSTEM: Appears as a message in the chat. (e.g., \"Respawn point set\")",
                            "\tGAME_INFO: Game information that appears above the hotbar (e.g., \"You may not rest now, the bed is too far away\").",
                            "The target option controls to whom the message is sent:",
                            "\tALL: Sends the message to all players on the server.",
                            "\tDIMENSION: Sends the message to all players in the current dimension.",
                            "\tSLEEPING: Sends the message to all players in the current dimension who are sleeping.")
                    .push("messages");

            // sleep.messages.morning
            builder.comment("This message is sent after a sleep cycle has completed.").push("morning");
            morningMessage = builder.comment(
                            "Available variables:",
                            "sleepingPlayers -> the number of players in the current dimension who were sleeping.",
                            "totalPlayers -> the number of players in the current dimension (spectators are not counted).",
                            "sleepingPercentage -> the percentage of players in the current dimension who were sleeping (does not include % symbol).")
                    .define("message", "\u00A7e\u00A7oTempus fugit!");
            morningMessageType = builder.comment("Sets where this message appears.")
                    .defineEnum("type", ChatTypeOptions.GAME_INFO);
            morningMessageTarget = builder.comment(
                            "Sets to whom this message is sent.",
                            "A target of 'SLEEPING' will send the message to all players who just woke up.")
                    .defineEnum("target", TemplateMessage.MessageTarget.DIMENSION);
            builder.pop(); // sleep.messages.morning

            // sleep.messages.enterBed
            builder.comment("This message is sent when a player enters their bed.").push("enterBed");
            enterBedMessage = builder.comment(
                            "Available variables:",
                            "player -> the player who started sleeping.",
                            "sleepingPlayers -> the number of players in the current dimension who are sleeping.",
                            "totalPlayers -> the number of players in the current dimension (spectators are not counted).",
                            "sleepingPercentage -> the percentage of players in the current dimension who are sleeping (does not include % symbol).")
                    .define("message", "${player} is now sleeping. [${sleepingPlayers}/${totalPlayers}]");
            enterBedMessageType = builder.comment("Sets where this message appears.")
                    .defineEnum("type", ChatTypeOptions.GAME_INFO);
            enterBedMessageTarget = builder.comment("Sets to whom this message is sent.")
                    .defineEnum("target", TemplateMessage.MessageTarget.DIMENSION);
            builder.pop(); // sleep.messages.enterBed

            // sleep.messages.leaveBed
            builder.comment("This message is sent when a player leaves their bed (without being woken up naturally by morning).").push("leaveBed");
            leaveBedMessage = builder.comment(
                            "Available variables:",
                            "player -> the player who left their bed.",
                            "sleepingPlayers -> the number of players in the current dimension who are sleeping.",
                            "totalPlayers -> the number of players in the current dimension (spectators are not counted).",
                            "sleepingPercentage -> the percentage of players in the current dimension who are sleeping (does not include % symbol).")
                    .define("message", "${player} has left their bed. [${sleepingPlayers}/${totalPlayers}]");
            leaveBedMessageType = builder.comment("Sets where this message appears.")
                    .defineEnum("type", ChatTypeOptions.GAME_INFO);
            leaveBedMessageTarget = builder.comment("Sets to whom this message is sent. ")
                    .defineEnum("target", TemplateMessage.MessageTarget.DIMENSION);
            builder.pop(); // sleep.messages.leaveBed

            builder.pop(); // sleep.messages
            builder.pop(); // sleep
        }

        public static double daySpeed() {
            return COMMON.daySpeed.get();
        }

        public static double nightSpeed() {
            return COMMON.nightSpeed.get();
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
            return !Services.PLATFORM.isModLoaded("sleepwarp") ? COMMON.enableSleepFeature.get() : false;
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

        public static boolean allowDaySleep() {
            return COMMON.allowDaySleep.get();
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

    }

}
