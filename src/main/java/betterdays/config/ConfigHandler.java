package betterdays.config;

import net.minecraftforge.common.ForgeConfigSpec;

import betterdays.BetterDays;

public class ConfigHandler {

    public static final ForgeConfigSpec SERVER_SPECS;

    public static final String CATEGORY_SERVER = "Server Settings";

    public static ForgeConfigSpec.BooleanValue enableWelcomeMessage;

    static {
        ForgeConfigSpec.Builder serverConfig = new ForgeConfigSpec.Builder();

        {
            serverConfig.push(CATEGORY_SERVER);
            enableWelcomeMessage = serverConfig.comment("Show status message on first player login after server unpaused.").define("enableWelcomeMessage", true);
            serverConfig.pop();
        }

        SERVER_SPECS = serverConfig.build();
    }

}
