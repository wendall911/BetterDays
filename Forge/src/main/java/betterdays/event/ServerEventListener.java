package betterdays.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;

public class ServerEventListener {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onDaySleepCheck(SleepingTimeCheckEvent event) {
        if (TimeServiceManager.onDaySleepCheck(event.getEntity().level())) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public void onSleepingCheckEvent(SleepingTimeCheckEvent event) {
        BetterDaysMessages.onSleepingCheckEvent(event.getEntity());

        if (TimeServiceManager.onSleepingCheckEvent(event.getEntity().level())) {
            event.setResult(Event.Result.ALLOW);
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
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            TimeServiceManager.onWorldTick(event.level);
        }
    }

}
