package betterdays.event;

import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;

import betterdays.config.ConfigHandler;
import betterdays.BetterDays;

import sereneseasons.season.SeasonSavedData;
import sereneseasons.handler.season.SeasonHandler;

@Mod.EventBusSubscriber(modid = BetterDays.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class ServerEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        ServerWorld world = (ServerWorld)event.world.getWorld();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldEvent.Load event) {
        ServerWorld world = (ServerWorld)event.getWorld();
        //randomTickSpeed = world.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED);
    }

}
