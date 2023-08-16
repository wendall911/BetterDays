package betterdays.compat.sereneseasons.mixin;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import sereneseasons.api.SSGameRules;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

import static sereneseasons.handler.season.SeasonHandler.getSeasonSavedData;
import static sereneseasons.handler.season.SeasonHandler.sendSeasonUpdate;

import betterdays.config.ConfigHandler;
import betterdays.event.ServerEventListener;

@Mixin(SeasonHandler.class)
public abstract class MixinSeasonHandler implements SeasonHelper.ISeasonDataProvider {

    @Shadow @Final public static HashMap<ResourceKey<Level>, Integer> clientSeasonCycleTicks;
    private static final HashMap<Level, Long> lastDayTimes = new HashMap<>();
    private static final HashMap<Level, Long> clientLastDayTimes = new HashMap<>();
    private boolean wasSetDirty = false;

    @Inject(method = "onWorldTick", at = @At("HEAD"), cancellable = true)
    private void betterdays$onWorldTick(TickEvent.WorldTickEvent event, CallbackInfo ci) {
        Level level = event.world;

        /*
         * Check if timeDirty is set in the ServerEventListener. If so, sync ticks to whatever random time / season was set.
         *
         * Serene Seasons has a not very well implemented behavior where if you set the season, it starts the timer at the
         * exact time of day that you set the season. For this, we want to sync with the daytime.
         */
        if (ConfigHandler.Common.sereneSeasonsFix() && ServerEventListener.isTimeDirty()
                && event.phase == TickEvent.Phase.START
                && !level.isClientSide && ServerConfig.isDimensionWhitelisted(level.dimension())) {
            long dayTime = level.getLevelData().getDayTime();
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(level);
            int seasonCycleTicks = seasonData.seasonCycleTicks;
            SeasonTime time = new SeasonTime(seasonData.seasonCycleTicks);
            int numDays = seasonCycleTicks / time.getDayDuration();

            seasonData.seasonCycleTicks = (int) ((numDays * 24000L) + dayTime);
            SeasonHandler.sendSeasonUpdate(level);

            seasonData.setDirty();
            // Set this so we can skip the Phase.END handler, picking up on the next worldTick event.
            wasSetDirty = true;

            ci.cancel();

            return;
        }

        if (ConfigHandler.Common.sereneSeasonsFix()
                && !ServerEventListener.isTimeDirty()
                && event.phase == TickEvent.Phase.END
                && !level.isClientSide && ServerConfig.isDimensionWhitelisted(level.dimension())) {

            if (!ServerConfig.progressSeasonWhileOffline.get()) {
                MinecraftServer server = level.getServer();
                if (server != null && server.getPlayerList().getPlayerCount() == 0)
                    return;
            }

            // Only tick seasons if the game rule is enabled
            if (!level.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE)) {
                return;
            }

            // Skip this cycle if we handled a time/season set command
            if (wasSetDirty) {
                wasSetDirty = false;
                ServerEventListener.setTimeDirty(false);
                ci.cancel();
                return;
            }

            long dayTime = level.getLevelData().getDayTime();
            long lastDayTime = lastDayTimes.getOrDefault(level, 0L);
            long difference = dayTime - lastDayTime;

            lastDayTimes.put(level, dayTime);

            // Skip if there is no difference
            if (difference <= 0) {
                ci.cancel();
            }
            // If greater than 1, speed is much faster. We need to update the season ticks to match.
            else if (difference > 1) {
                SeasonSavedData savedData = getSeasonSavedData(level);

                savedData.seasonCycleTicks += difference - 1;
                sendSeasonUpdate(level);

                savedData.setDirty();
            }
        }
    }

    @Inject(method = "onClientTick", at = @At("HEAD"), cancellable = true)
    @OnlyIn(Dist.CLIENT)
    private void betterdays$onClientTick(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        if (ConfigHandler.Common.sereneSeasonsFix() && Minecraft.getInstance().player != null) {
            Level level = Minecraft.getInstance().player.level;
            ResourceKey<Level> dimension = level.dimension();

            if (event.phase == TickEvent.Phase.END && ServerConfig.isDimensionWhitelisted(dimension)) {
                long dayTime = level.getLevelData().getDayTime();
                long lastDayTime = clientLastDayTimes.getOrDefault(level, 0L);
                clientLastDayTimes.put(level, dayTime);
                long difference = dayTime - lastDayTime;

                // Skip if there is no difference
                if (difference <= 0) {
                    ci.cancel();
                }
                else if (difference > 1) {
                    clientSeasonCycleTicks.compute(dimension, (k, v) -> {
                        int drift = (int) difference - 1;

                        return v == null ? drift : v + drift;
                    });
                }
            }
        }
    }

}
