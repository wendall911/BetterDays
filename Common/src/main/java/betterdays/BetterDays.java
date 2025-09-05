package betterdays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import betterdays.registry.TimeEffectsRegistry;

public class BetterDays {

    public static final String MODID = "betterdays";
    public static final String MOD_NAME = "Better Days";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        TimeEffectsRegistry.init();
    }

}
