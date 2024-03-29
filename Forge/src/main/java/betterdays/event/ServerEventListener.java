package betterdays.event;

import java.util.List;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;

import net.minecraft.commands.CommandSourceStack;

import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;

import betterdays.config.ConfigHandler;
import betterdays.message.BetterDaysMessages;
import betterdays.time.TimeServiceManager;

public class ServerEventListener {

    private static boolean timeDirty = false;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDaySleepCheck(SleepingTimeCheckEvent event) {
        if (TimeServiceManager.onDaySleepCheck(event.getEntity().level())) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSleepingCheckEvent(SleepingTimeCheckEvent event) {
        BetterDaysMessages.onSleepingCheckEvent(event.getEntity());

        if (TimeServiceManager.onSleepingCheckEvent(event.getEntity().level())) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
        if (event.updateLevel()) {
            BetterDaysMessages.onPlayerWakeUpEvent(event.getEntity());
        }

    }

    @SubscribeEvent
    public static void onSleepFinishedEvent(SleepFinishedTimeEvent event) {
        BetterDaysMessages.onSleepFinishedEvent(event.getLevel());
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        TimeServiceManager.onWorldLoad(event.getLevel());
    }

    @SubscribeEvent
    public static void onWorldUnload(LevelEvent.Unload event) {
        TimeServiceManager.onWorldUnload(event.getLevel());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            TimeServiceManager.onWorldTick(event.level);
        }
    }

    /*
     * Check if /time set OR /season set command is run
     * If true, set timeDirty so MixinSeasonHandler can do the right thing and set time for the day that is in
     * sync with the season time.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event) {
        if (ModList.get().isLoaded("sereneseasons") && ConfigHandler.Common.sereneSeasonsFix()) {
            CommandContextBuilder<CommandSourceStack> ctx = event.getParseResults().getContext();
            List<ParsedCommandNode<CommandSourceStack>> nodes = ctx.getNodes();

            if (nodes.size() < 3) return;

            String commandName = nodes.get(0).getNode().getName();
            String argument = nodes.get(1).getRange().get(event.getParseResults().getReader());

            if ("time".equals(commandName) && "set".equals(argument)) {
                timeDirty = true;
            }
        }
    }

    public static boolean isTimeDirty() {
        return timeDirty;
    }

    public static void setTimeDirty(boolean status) {
        timeDirty = status;
    }

}
