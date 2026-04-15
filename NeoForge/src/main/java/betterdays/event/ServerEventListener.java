package betterdays.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;

public class ServerEventListener {

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
