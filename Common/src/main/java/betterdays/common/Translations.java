package betterdays.common;

import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import org.slf4j.helpers.MessageFormatter;

public class Translations {

    private static final Joiner LINE_JOINER = Joiner.on("\n");
    private static final Map<String, String> translations = Maps.newHashMap();

    static {
        translations.put("gui", "This section defines settings for visual elements in the client.");
        translations.put("clockalignment", "Sets the screen alignment of the bed clock.");
        translations.put("clockscale", "Sets the scale of the bed clock.");
        translations.put("clockmargin", joiner(
            "Sets the distance between the clock and the edge of the screen.",
            "Unused if clockAlignment is CENTER_CENTER."
        ));
        translations.put("preventclockwobble", "This setting prevents clock wobble when getting in bed by updating the clock's position every tick.");
        translations.put("preventclockwobble.comment", joiner(
            "This setting prevents clock wobble when getting in bed by updating the clock's position every tick.",
            "As a side-effect, the clock won't wobble when first viewed as it does in vanilla. This setting is",
            "unused if displayBedClock is false."
        ));
        translations.put("blacklistdimensions", joiner(
            "This setting blacklists the sky rendering for specific dimensions. Like in the Aether if using ",
            "/time set command, the sky jitters. Adding to the blacklist will prevent this behavior."
        ));
        translations.put("time", joiner(
        "This section defines settings for time manipulation.",
            "This is where you can set day and night speed independently.",
            "You can also set the time speed to change dynamically throughout the day/night cycle."
        ));
        translations.put("speedmethod", joiner(
            "Determines which method is used to set day and night speed.",
            "RATIO: Uses daySpeed and nightSpeed settings.",
            "MINUTES: Uses daySpeedMinutes and nightSpeedMinutes settings.",
            "REALTIME: Sets day and night to 12 real-world hours each."
        ));
        translations.put("dayspeed", joiner(
            "The speed at which time passes during the day.",
            "Day is defined as any time between dayStart (see below) and nightStart (see below) the next day.",
            "This is the RATIO of time passing relative to vanilla.",
            "Vanilla speed: 1.0"
        ));
        translations.put("nightspeed", joiner(
            "The speed at which time passes during the night.",
            "Night is defined as any time between dayStart (see below) and nightStart (see below).",
            "This is the RATIO of time passing relative to vanilla.",
            "Vanilla speed: 1.0"
        ));
        translations.put("dayspeedminutes", joiner(
            "An alternative way to set day speed. This setting is mutually exclusive with daySpeed.",
            "If both are set, speedMethod (see above) determines which setting is used.",
            "This setting defines the length of the day in real-world minutes.",
            "Vanilla length: 10.0 minutes"
        ));
        translations.put("nightspeedminutes", joiner(
            "An alternative way to set night speed. This setting is mutually exclusive with nightSpeed.",
            "If both are set, speedMethod (see above) determines which setting is used.",
            "This setting defines the length of the night in real-world minutes.",
            "Vanilla length: 10.0 minutes"
        ));
        translations.put("daystart", joiner(
            "The time to start day. This is configurable within the time the sun appears and day starts.",
            "Default: 23500"
        ));
        translations.put("nightstart", joiner(
            "The time to start night. This is configurable within the time sunset starts and night starts.",
            "Default: 12500"
        ));
        translations.put("enableinterpolatedtime", joiner(
            "Enabling this will allow the setting of an infinite amount of time speeds defined as pairs in the form:",
            "(currentTick, desiredSpeed)",
            "where between each value bezier spline interpolation is performed for smooth transitioning",
            "The two default pairs at 0 and 24000 are necessary as they act as bounds. However ",
            "The time speed value for each of them can be freely modified"
        ));
        translations.put("interpolatedtimesmoothingfactor", joiner(
            "The value that determines the smoothing factor in the interpolation algorithm.",
            "A high value will make the interpolation softer and curvier",
            "A low value will make the interpolation closer to a piecewise function"
        ));
        translations.put("interpolatedtimelist", joiner(
            "These are the pairs that define what speed time should run at, at the specified day/night tick",
            "The two default pairs need to exist, but their time speed values can be modified"
        ));
        translations.put("effects", joiner(
            "This section defines settings for time effects.",
            "These effects modify various game mechanics to match the current speed of time.",
            "These effects can have a significant impact on performance, especially on servers with many players."
        ));
        translations.put("weathereffect", "When applied, this effect syncs the passage of weather with the current speed of time.");
        translations.put("weathereffect.comment", joiner(
            "I.e., as time moves faster, rain stops faster. Clear weather is not affected.",
            "When set to SLEEPING, this effect only applies when at least one player is sleeping in a dimension.",
            "Note: On NeoForge 1.21.1+ this is already handled by the platform. SLEEPING will still work as intended.",
            "Note: This setting is not applicable if game rule doWeatherCycle is false."
        ));
        translations.put("randomtickeffect", joiner(
            "When applied, this effect syncs the random tick speed with the current speed of time, forcing",
            "crop, tree, and grass growth to occur at baseRandomTickSpeed multiplied by the current time-speed."
        ));
        translations.put("randomtickeffect.comment", joiner(
            "When set to SLEEPING, randomTickSpeed is set to baseRandomTickSpeed unless at least one player is sleeping in a dimension.",
            "More information on the effects of random tick speed can be found here: https://minecraft.wiki/w/Tick#Random_tick",
            "WARNING: This setting overwrites the randomTickSpeed game rule. To modify the base random tick speed,",
            "use the baseRandomTickSpeed setting instead of changing the game rule directly.",
            "Note: On NeoForge 1.21.1+ this is already handled by the platform. SLEEPING will still work as intended.",
            "This effect has a minimum randomTickSpeed of 1 if time speed is less than 1.0."
        ));
        translations.put("baserandomtickspeed", "The base random tick speed used by the randomTickEffect time effect.");
        translations.put("potioneffect", "When applied, this effect progresses potion effects to match the rate of the current time-speed.");
        translations.put("potioneffect.comment", joiner(
            "This effect does not apply if time speed is 1.0 or less.",
            "THIS MAY HAVE A NEGATIVE IMPACT ON PERFORMANCE IN SERVERS WITH MANY PLAYERS.",
            "When set to ALWAYS, this effect applies to all players in the dimension, day or night.",
            "When set to SLEEPING, this effect only applies to players who are sleeping.",
            "Note: On NeoForge 1.21.1+ this is already handled by the platform. SLEEPING will still work as intended."
        ));
        translations.put("hungereffect", "When applied, this effect progresses player hunger effects to match the rate of the current time-speed.");
        translations.put("hungereffect.comment", joiner(
            "This results in faster healing when food level is full, and faster harm when food level is too low.",
            "This effect does not apply if time speed is 1.0 or less.",
            "When set to ALWAYS, this effect applies to all players in the dimension, day or night. Not recommended on higher difficulty settings",
            "When set to SLEEPING, this effect only applies to players who are sleeping."
        ));
        translations.put("blockentityeffect", "When applied, this effect progresses block entities like furnaces, hoppers, and spawners to match the rate of the current time-speed.");
        translations.put("blockentityeffect.comment", joiner(
            "WARNING: This time-effect has a significant impact on performance.",
            "This effect does not apply if time speed is 1.0 or less.",
            "When set to SLEEPING, this effect only applies when at least one player is sleeping in a dimension.",
            "Note: On NeoForge 1.21.1+ this is already handled by the platform. SLEEPING will still work as intended."
        ));
        translations.put("sleep", joiner(
            "This section defines settings for sleep feature functionality.",
            "If another sleep mod is installed, it is recommended to disable the sleep feature of Better Days"
        ));
        translations.put("enablesleepfeature", joiner(
            "Enables or disables the sleep feature of this mod. Enabling this setting will modify the vanilla sleep functionality",
            "and may conflict with other sleep mods. If disabled, all settings in the sleep section will not apply."
        ));
        translations.put("sleepspeedmax", joiner(
            "## THIS SETTING DEFINES THE SLEEP TIME-SPEED IN SINGLE-PLAYER GAMES ###",
            "The maximum speed at which time passes when all players are sleeping.",
            "A value of 110 is nearly equal to the time it takes to sleep in vanilla."
        ));
        translations.put("sleepspeedmin", "The minimum speed at which time passes when only 1 player is sleeping in a full server.");
        translations.put("sleepspeedall", joiner(
            "The speed at which time passes when all players are sleeping.",
            "Set to -1 to disable this feature (sleepSpeedMax will be used when all players are sleeping)."
        ));
        translations.put("sleepspeedcurve", "This parameter defines the curvature of the interpolation function that translates the sleeping player percentage into time-speed.");
        translations.put("sleepspeedcurve.comment", joiner(
            "The function used is a Normalized Tunable Sigmoid Function.",
            "A value of 0.5 represents a linear relationship.",
            "Smaller values bend the curve toward the X axis, while greater values bend it toward the Y axis.",
            "This graph may be used as a reference for tuning the curve: https://www.desmos.com/calculator/w8gntxzfow",
            "Credit to Dino Dini for the function: https://dinodini.wordpress.com/2010/04/05/normalized-tunable-sigmoid-functions/",
            "Credit to SmoothSleep for the idea: https://www.spigotmc.org/resources/smoothsleep.32043/"
        ));
        translations.put("clearweatheronwake", joiner(
            "Set to 'true' for the weather to clear when players wake up in the morning as it does in vanilla.",
            "Set to 'false' to force weather to pass naturally. Adds realism when accelerateWeather is enabled.",
            "Note: This setting is ignored if game rule doWeatherCycle is false."
        ));
        translations.put("displaybedclock", "When true, a clock is displayed in the sleep interface.");
        translations.put("ratioplayersforsleep", joiner(
            "The ratio of players in a dimension that must be sleeping to skip to morning.",
            "A value of 1 means all players must be sleeping, 0.5 means half the players must be sleeping, etc.",
            "A value of 0 effectively disables this feature."
        ));
        translations.put("messages", joiner(
            "This section defines settings for notification messages."
        ));
        translations.put("messages.comment", joiner(
            "All messages support Minecraft formatting codes (https://minecraft.wiki/w/Formatting_codes).",
            "All messages have variables that can be inserted using the following format: ${variableName}",
            "Any message can be disabled by setting it to an empty string.",
            "The type option controls where the message appears:",
            "    SYSTEM: Appears as a message in the chat. (e.g., \"Respawn point set\")",
            "    GAME_INFO: Game information that appears above the hotbar (e.g., \"You may not rest now, the bed is too far away\").",
            "The target option controls to whom the message is sent:",
            "    ALL: Sends the message to all players on the server.",
            "    DIMENSION: Sends the message to all players in the current dimension.",
            "    SLEEPING: Sends the message to all players in the current dimension who are sleeping."
        ));
        translations.put("gui.message", "Formatted message to be sent. Look at documentation in config file for details.");
        translations.put("message", joiner(
            "Available variables:",
            "{}",
            "sleepingPlayers -> the number of players in the current dimension who {} sleeping.",
            "totalPlayers -> the number of players in the current dimension (spectators are not counted).",
            "sleepingPercentage -> the percentage of players in the current dimension who are sleeping (does not include % symbol)."
        ));
        translations.put("gui.target", "Sets to whom this message is sent.");
        translations.put("target", joiner(
                "Sets to whom this message is sent.",
                "{}"
        ));
        translations.put("type", "Sets where this message appears.");
        translations.put("morning", "This message is sent after a sleep cycle has completed.");
        translations.put("target.morning", "A target of 'SLEEPING' will send the message to all players who just woke up.");
        translations.put("enterbed", "This message is sent when a player enters their bed.");
        translations.put("message.enterbed", "player -> the player who started sleeping.");
        translations.put("leavebed", "This message is sent when a player leaves their bed (without being woken up naturally by morning).");
        translations.put("message.leavebed", "player -> the player who left their bed.");
    }

    public static String get(String key) {
        return translations.getOrDefault(key, key);
    }

    public static String get(String key, String... values) {
        return MessageFormatter.arrayFormat(translations.getOrDefault(key, key), values).getMessage();
    }

    private static String joiner(String... string) {
        return LINE_JOINER.join(string);
    }

}
