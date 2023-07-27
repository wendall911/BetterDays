package betterdays;

import net.fabricmc.api.ClientModInitializer;

import betterdays.event.ClientEventListener;

public class BetterDaysClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientEventListener.setup();
    }

}
