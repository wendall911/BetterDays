package betterdays.event;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;

import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;

public class ServerEventListener {

    public static void setup() {
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
            if (TimeServiceManager.onDaySleepCheck(player.level())) {
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }));

        EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
            BetterDaysMessages.onSleepingCheckEvent(player);

            if (TimeServiceManager.onSleepingCheckEvent(player.level())) {
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }));

        EntitySleepEvents.STOP_SLEEPING.register(((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                BetterDaysMessages.onPlayerWakeUpEvent(player);
            }
        }));

        ServerWorldEvents.LOAD.register((server, level) -> {
            TimeServiceManager.onWorldLoad(level);
        });

        ServerWorldEvents.UNLOAD.register((server, level) -> {
            TimeServiceManager.onWorldUnload(level);
        });

        ServerTickEvents.START_WORLD_TICK.register(TimeServiceManager::onWorldTick);
    }

}
