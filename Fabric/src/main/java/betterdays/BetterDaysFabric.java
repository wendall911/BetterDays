package betterdays;

import net.fabricmc.api.ModInitializer;

import betterdays.event.ServerEventListener;

public class BetterDaysFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BetterDays.init();
        ServerEventListener.setup();
    }

}
