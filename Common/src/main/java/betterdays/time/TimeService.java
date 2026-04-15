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

package betterdays.time;

import java.util.Collection;

import net.minecraft.world.clock.ServerClockManager;

import betterdays.BetterDays;
import betterdays.registry.TimeEffectsRegistry;
import betterdays.registry.RegistryObject;
import betterdays.config.ConfigHandler;
import betterdays.platform.Services;
import betterdays.time.effects.TimeEffect;
import betterdays.utils.MathUtils;
import betterdays.wrappers.ServerLevelWrapper;

/**
 * Handles the Better Days time and sleep functionality for a level.
 */
public class TimeService {

    /** Time of day when the sun rises above the horizon. */
    public static final Time DAY_START = new Time(ConfigHandler.Common.dayStart());

    /** Time of day players are awake */
    public static final Time WAKEUP = new Time(23450);

    /** Time of day when the sun sets below the horizon. */
    public static final Time NIGHT_START = new Time(ConfigHandler.Common.nightStart());

    // The largest number of lunar cycles that can be stored in an int
    private static final int OVERFLOW_THRESHOLD = 11184 * Time.LUNAR_CYCLE_TICKS;

    private static MonotonicInterpolator monotonicInterpolator;

    /** The level managed by this {@code TimeService}. */
    public final ServerLevelWrapper level;
    /** The {@code SleepStatus} object for this level. */
    public final SleepStatus sleepStatus;

    private double timeDecimalAccumulator = 0;

    /**
     * Creates a new instance.
     *
     * @param level  the wrapped level whose time this object should manage
     */
    public TimeService(ServerLevelWrapper level) {
        this.level = level;
        this.sleepStatus = new SleepStatus(ConfigHandler.Common::enableSleepFeature);

        this.level.setSleepStatus(this.sleepStatus);

        if (ConfigHandler.Common.enableInterpolatedTime()) {
            monotonicInterpolator = new MonotonicInterpolator(ConfigHandler.Common.interpolatedTimePairs(), ConfigHandler.Common.interpolatedTimeSmoothingFactor());
        }
    }

    /**
     * Performs all time, sleep, and weather calculations. Should run once per tick.
     */
    public void tick() {
        if (!level.daylightRuleEnabled()) {
            return;
        }

        Time time = getDayTime();
        float speed = (float) getTimeSpeed(time);

        ServerClockManager clockManager = level.get().clockManager();

        level.get().dimensionType().defaultClock().ifPresent(defaultClock -> clockManager.setRate(defaultClock, speed));

        if (!sleepStatus.allAwake()) {
            Time deltaTime = tickTime();
            TimeContext context = new TimeContext(this, time, deltaTime);

            getActiveTimeEffects().forEach(effect -> effect.get().onTimeTick(context));

            if (ConfigHandler.Common.enableSleepFeature() && Time.crossedMorning(WAKEUP, time)) {
                handleMorning();
            }
        }
    }

    public void handleMorning() {
        long time = level.get().getDefaultClockTime();

        Services.PLATFORM.onSleepFinished(level, time);

        BetterDays.LOGGER.debug("Sleep cycle complete on dimension: {}.",
                level.get().dimension().identifier());
    }

    /**
     * Progresses time in this {@link #level} based on the current time-speed.
     * This method should be called every tick.
     *
     * @return the amount of time that elapsed
     */
    private Time tickTime() {
        Time time = getDayTime();

        return new Time(getTimeSpeed(time));
    }

    /**
     * Calculates the current time-speed multiplier based on the time-of-day and number of sleeping
     * players.
     *
     * Accepts time as a parameter to allow for prediction of other times. Prediction of times other
     * than the current time may not be accurate due to sleeping player changes.
     *
     * A return value of 1 is equivalent to vanilla time speed.
     *
     * @param time  the time at which to calculate the time-speed
     * @return the time-speed
     */
    public double getTimeSpeed(Time time) {
        if (!ConfigHandler.Common.enableSleepFeature()
                || sleepStatus.allAwake()
                || (sleepStatus.ratio() < ConfigHandler.Common.percentPlayersForSleep())) {
            if (ConfigHandler.Common.enableInterpolatedTime()) {
                return monotonicInterpolator.evaluate(time.timeOfDay().longValue());
            }
            if (time.equals(DAY_START) || time.timeOfDay().betweenMod(DAY_START, NIGHT_START)) {
                return ConfigHandler.Common.daySpeed(level.get());
            }
            else {
                return ConfigHandler.Common.nightSpeed(level.get());
            }
        }

        if (sleepStatus.allAsleep() && ConfigHandler.Common.sleepSpeedAll() >= 0) {
            return ConfigHandler.Common.sleepSpeedAll();
        }

        double sleepRatio = sleepStatus.ratio();
        double curve = ConfigHandler.Common.sleepSpeedCurve();
        double speedRatio = MathUtils.normalizedTunableSigmoid(sleepRatio, curve);

        double sleepSpeedMin = ConfigHandler.Common.sleepSpeedMin();
        double sleepSpeedMax = ConfigHandler.Common.sleepSpeedMax();
        double multiplier = MathUtils.lerp(speedRatio, sleepSpeedMin, sleepSpeedMax);

        return multiplier;
    }

    /**
     * {@return this level's time as an instance of {@link Time}}
     */
    public Time getDayTime() {
        return new Time(level.get().getDefaultClockTime(), timeDecimalAccumulator);
    }

    private Collection<RegistryObject<TimeEffect>> getActiveTimeEffects() {
        return TimeEffectsRegistry.TIME_EFFECT_REGISTRY.getEntries();
    }

}
