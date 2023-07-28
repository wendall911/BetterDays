/*
 * Derived from sereeneseasonfix mod
 * https://github.com/123oro321/sereneseasonsfix
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Or_OS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package betterdays.compat.sereneseasons.mixin;

import java.util.HashMap;

import betterdays.BetterDays;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import sereneseasons.api.SSGameRules;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

@Mixin(SeasonHandler.class)
public abstract class MixinSeasonHandler implements SeasonHelper.ISeasonDataProvider {

    private static final HashMap<Level, Long> lastDayTimes = new HashMap<>();
    private static final HashMap<Level, Integer> tickSinceLastUpdate = new HashMap<>();

    /**
     * @author Or_OS && wendall911
     * @reason Merged with TimeSkipHandler and modified the logic to be more consistent
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;

        if (event.phase == TickEvent.Phase.END && !level.isClientSide && ServerConfig.isDimensionWhitelisted(level.dimension())) {
            if (!ServerConfig.progressSeasonWhileOffline.get()) {
                MinecraftServer server = level.getServer();

                if (server != null && server.getPlayerList().getPlayerCount() == 0) {
                    return;
                }
            }

            if (!level.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE)) {
                return;
            }

            long dayTime = level.getLevelData().getDayTime();
            long lastDayTime = lastDayTimes.get(level);
            SeasonSavedData savedData = SeasonHandler.getSeasonSavedData(level);
            long difference = dayTime - lastDayTime;

            // Skip if there is no difference
            if (difference == 0) {
                return;
            }

            int cycleDuration = SeasonTime.ZERO.getCycleDuration();
            savedData.seasonCycleTicks = (int) (((savedData.seasonCycleTicks + difference) % cycleDuration + cycleDuration) % cycleDuration);

            Integer tick = tickSinceLastUpdate.get(level);
            if (tick >= 20) {
                SeasonHandler.sendSeasonUpdate(level);
                tick %= 20;
            }
            tickSinceLastUpdate.put(level, tick + 1);
            savedData.setDirty();
        }
    }
    @SubscribeEvent
    public void onWorldLoaded(LevelEvent.Load event) {
        Level level = (Level) event.getLevel();

        if (!level.isClientSide && ServerConfig.isDimensionWhitelisted(level.dimension())) {
            BetterDays.LOGGER.info("Setting cached parameters.");

            lastDayTimes.put(level, level.getLevelData().getDayTime());
            tickSinceLastUpdate.put(level, 0);
        }
    }

}
