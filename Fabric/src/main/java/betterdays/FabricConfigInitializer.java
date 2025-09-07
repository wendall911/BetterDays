package betterdays;

import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;

public class FabricConfigInitializer implements WhiteNoiseConfigInitializer {

    @Override
    public void onInitialize() {
        BetterDays.initConfig();
    }

}
