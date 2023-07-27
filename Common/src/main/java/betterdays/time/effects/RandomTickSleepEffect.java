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

import betterdays.config.ConfigHandler;
import betterdays.time.SleepStatus;
import betterdays.time.TimeContext;

import static betterdays.time.effects.EffectCondition.ALWAYS;
import static betterdays.time.effects.EffectCondition.SLEEPING;

/**
 * Time effect that increases the random tick speed while players are sleeping, proportionate to
 * the current speed of time.
 */
public class RandomTickSleepEffect extends AbstractTimeEffect {

    @Override
    public void onTimeTick(TimeContext context) {
        updateRandomTickSpeed(context);
    }

    /**
     * Updates the random tick speed based on configuration values.
     * @param context  the {@link TimeContext} of the current tick
     */
    private void updateRandomTickSpeed(TimeContext context) {
        EffectCondition condition = ConfigHandler.Common.randomTickEffect();

        if (condition == EffectCondition.NEVER) {
            return;
        }

        int speed = ConfigHandler.Common.baseRandomTickSpeed();
        SleepStatus sleepStatus = context.getTimeService().sleepStatus;
        if (condition == ALWAYS || (condition == SLEEPING && !sleepStatus.allAwake())) {
            speed *= context.getTimeDelta().longValue();
        }

        context.getLevel().setRandomTickSpeed(speed);
    }

}
