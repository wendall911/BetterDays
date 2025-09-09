package betterdays;

import technology.roughness.whitenoise.config.WhiteNoiseInitializer;

public class FabricConfigInitializer implements WhiteNoiseInitializer {

    @Override
    public void onInitializeConfig() {
        BetterDays.initConfig();
    }

}
