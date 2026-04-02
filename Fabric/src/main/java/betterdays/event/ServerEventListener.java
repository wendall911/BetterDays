package betterdays.event;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.world.entity.player.Player;

import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;

public class ServerEventListener {

    public static void setup() {
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(((player) -> {
            if (TimeServiceManager.onDaySleepCheck(player.level())) {
                return true;
            }

            return false;
        }));

        EntitySleepEvents.ALLOW_RESETTING_TIME.register(((player) -> {
            BetterDaysMessages.onSleepingCheckEvent(player);

            if (TimeServiceManager.onSleepingCheckEvent(player.level())) {
                return true;
            }

            return false;
        }));

        EntitySleepEvents.STOP_SLEEPING.register(((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                BetterDaysMessages.onPlayerWakeUpEvent(player);
            }
        }));

        ServerLevelEvents.LOAD.register((server, level) -> {
            TimeServiceManager.onWorldLoad(level);
        });

        ServerLevelEvents.UNLOAD.register((server, level) -> {
            TimeServiceManager.onWorldUnload(level);
        });

        ServerTickEvents.START_LEVEL_TICK.register(TimeServiceManager::onWorldTick);
    }

}
