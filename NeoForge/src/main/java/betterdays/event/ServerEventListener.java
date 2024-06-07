package betterdays.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;

import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class ServerEventListener {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onDaySleepCheck(CanContinueSleepingEvent event) {
        if (TimeServiceManager.onDaySleepCheck(event.getEntity().level())) {
            event.setContinueSleeping(true);
        }
    }

    @SubscribeEvent
    public void onSleepingCheckEvent(CanContinueSleepingEvent event) {
        if (event.getEntity() instanceof Player player) {
            BetterDaysMessages.onSleepingCheckEvent(player);

            if (TimeServiceManager.onSleepingCheckEvent(event.getEntity().level())) {
                event.setContinueSleeping(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
        if (event.updateLevel()) {
            BetterDaysMessages.onPlayerWakeUpEvent(event.getEntity());
        }

    }

    @SubscribeEvent
    public void onSleepFinishedEvent(SleepFinishedTimeEvent event) {
        BetterDaysMessages.onSleepFinishedEvent(event.getLevel());
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        TimeServiceManager.onWorldLoad(event.getLevel());
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        TimeServiceManager.onWorldUnload(event.getLevel());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldTick(LevelTickEvent.Pre event) {
        TimeServiceManager.onWorldTick(event.getLevel());
    }

}
