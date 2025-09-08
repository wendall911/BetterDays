package betterdays;

import com.illusivesoulworks.spectrelib.config.SpectreConfig;
import com.illusivesoulworks.spectrelib.config.SpectreConfigLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import betterdays.registry.TimeEffectsRegistry;
import betterdays.config.ConfigHandler;
import betterdays.platform.Services;

public class BetterDays {

    public static final String MODID = "betterdays";
    public static final String MOD_NAME = "Better Days";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        TimeEffectsRegistry.init();
    }
   
    public static void initConfig() {
        if (Services.PLATFORM.isPhysicalClient()) {
            SpectreConfig clientConfig = SpectreConfigLoader.add(SpectreConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC, MODID);
            clientConfig.addLoadListener((config, flag) -> ConfigHandler.init());
        }

        SpectreConfigLoader.add(SpectreConfig.Type.COMMON, ConfigHandler.COMMON_SPEC, MODID);
    }

}
