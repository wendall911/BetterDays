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

import java.util.stream.Stream;

import betterdays.config.ConfigHandler;
import betterdays.time.SleepStatus;
import betterdays.time.TimeContext;
import betterdays.wrappers.ServerLevelWrapper;
import betterdays.wrappers.ServerPlayerWrapper;

/** Time effect that progresses potion effects at the same rate as the speed of time. */
public class PotionTimeEffect extends AbstractTimeEffect {

    @Override
    public void onTimeTick(TimeContext context) {
        EffectCondition condition = ConfigHandler.Common.potionEffect();

        if (condition == EffectCondition.NEVER) {
            return;
        }

        ServerLevelWrapper level = context.getLevel();
        SleepStatus sleepStatus = context.getTimeService().sleepStatus;
        long extraTicks = context.getTimeDelta().longValue() - 1;

        if (extraTicks <= 0 || (condition == EffectCondition.SLEEPING && sleepStatus.allAwake())) {
            return;
        }

        Stream<ServerPlayerWrapper> playerStream = level.get().players().stream()
                .map(ServerPlayerWrapper::new);

        if (condition == EffectCondition.SLEEPING) {
            playerStream = playerStream.filter(ServerPlayerWrapper::isSleeping);
        }

        playerStream.forEach(player -> tickEffects(player, extraTicks));
    }

    /** Ticks all effects on {@code player} {@code ticks} times, then sends client update. */
    private static void tickEffects(ServerPlayerWrapper player, long ticks) {
        for (int i = 0; i < ticks; i++) {
            player.tickEffects();
        }
        player.sendMobEffectUpdatePackets();
    }

}
