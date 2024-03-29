/*
 * Derived from Hourglass
 * https://github.com/DuckyCrayfish/hourglass
 * Copyright (C) 2021 Nick Iacullo
 *
 * This file is part of Better Days.
 *
 * Better Days is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Better Days is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Better Days.  If not, see <https://www.gnu.org/licenses/>.
 */

package betterdays.time.effects;

import com.google.common.primitives.Ints;

import betterdays.config.ConfigHandler;
import betterdays.time.TimeContext;
import betterdays.wrappers.ServerLevelWrapper;

import static betterdays.time.effects.EffectCondition.ALWAYS;
import static betterdays.time.effects.EffectCondition.SLEEPING;

/**
 * Time effect that increases the speed that weather passes at the same rate as the current speed of
 * time.
 */
public class WeatherSleepEffect extends AbstractTimeEffect {

    @Override
    public void onTimeTick(TimeContext context) {
        ServerLevelWrapper level = context.getLevel();
        EffectCondition condition = ConfigHandler.Common.weatherEffect();
        boolean allAwake = context.getTimeService().sleepStatus.allAwake();
        if (level.weatherCycleEnabled()
                && (condition == ALWAYS || (condition == SLEEPING && !allAwake))) {
            progressWeather(context);
        }
    }

    /**
     * Progress the weather cycle in the level of {@code context} by its time delta.
     *
     * @param context  the {@link TimeContext} of the current tick
     */
    private void progressWeather(TimeContext context) {
        ServerLevelWrapper level = context.getLevel();
        int clearWeatherTime = level.levelData.getClearWeatherTime();
        int thunderTime = level.levelData.getThunderTime();
        int rainTime = level.levelData.getRainTime();

        // Subtract 1 from weather speed to account for vanilla's weather progression of 1 per tick.
        int weatherSpeed = Ints.saturatedCast(context.getTimeDelta().longValue() - 1);

        if (clearWeatherTime <= 0) {
            if (thunderTime > 0) {
                thunderTime = Math.max(1, thunderTime - weatherSpeed);
                level.levelData.setThunderTime(thunderTime);
            }
            if (rainTime > 0) {
                rainTime = Math.max(1, rainTime - weatherSpeed);
                level.levelData.setRainTime(rainTime);
            }
        }
    }

}
