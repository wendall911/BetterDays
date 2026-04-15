package betterdays.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import betterdays.time.TimeServiceManager;

public class ServerEventListener {

    public static void setup() {
        ServerLevelEvents.LOAD.register((server, level) -> {
            TimeServiceManager.onWorldLoad(level);
        });

        ServerLevelEvents.UNLOAD.register((server, level) -> {
            TimeServiceManager.onWorldUnload(level);
        });

        ServerTickEvents.START_LEVEL_TICK.register(TimeServiceManager::onWorldTick);
    }

}
