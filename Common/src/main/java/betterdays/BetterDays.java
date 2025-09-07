package betterdays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigLoader;
import technology.roughness.whitenoise.platform.Services;

import betterdays.registry.TimeEffectsRegistry;
import betterdays.config.ConfigHandler;

public class BetterDays {

    public static final String MODID = "betterdays";
    public static final String MOD_NAME = "Better Days";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        TimeEffectsRegistry.init();
    }

    public static void initConfig() {
        if (Services.PLATFORM.isPhysicalClient()) {
            WhiteNoiseConfig clientConfig = WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC, MODID);
            clientConfig.addLoadListener(config -> ConfigHandler.init());
        }

        WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.COMMON, ConfigHandler.COMMON_SPEC, MODID);
    }

}
