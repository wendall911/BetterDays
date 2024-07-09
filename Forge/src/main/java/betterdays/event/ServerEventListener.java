package betterdays.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;

public class ServerEventListener {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDaySleepCheck(SleepingTimeCheckEvent event) {
        if (TimeServiceManager.onDaySleepCheck(event.getEntity().level)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSleepingCheckEvent(SleepingTimeCheckEvent event) {
        BetterDaysMessages.onSleepingCheckEvent(event.getPlayer());

        if (TimeServiceManager.onSleepingCheckEvent(event.getEntity().level)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
        if (event.updateWorld()) {
            BetterDaysMessages.onPlayerWakeUpEvent(event.getPlayer());
        }

    }

    @SubscribeEvent
    public static void onSleepFinishedEvent(SleepFinishedTimeEvent event) {
        BetterDaysMessages.onSleepFinishedEvent(event.getWorld());
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        TimeServiceManager.onWorldLoad(event.getWorld());
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        TimeServiceManager.onWorldUnload(event.getWorld());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            TimeServiceManager.onWorldTick(event.world);
        }
    }

}
