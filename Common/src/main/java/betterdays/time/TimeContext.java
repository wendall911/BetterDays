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

import betterdays.wrappers.ServerLevelWrapper;

/**
 * Holds information about a time change performed by a {@code TimeService} on its level.
 * This class is used to pass time information to time effects.
 *
 * <p>This class intentionally excludes references to external libraries to minimize changes between
 * Minecraft versions.
 */
public class TimeContext {

    /** The {@code TimeService} for the level whose time changed. */
    protected final TimeService timeService;
    /** The new time after this time change occurred. */
    protected final Time currentTime;
    /** The amount of time that passed during this time change. */
    protected final Time timeDelta;

    /**
     * Creates a new instance.
     *
     * @param timeService  the {@code TimeService} for the level
     * @param currentTime  the current time after the change occurred
     * @param timeDelta  the time that has elapsed during this tick
     */
    public TimeContext(TimeService timeService, Time currentTime, Time timeDelta) {
        this.timeService = timeService;
        this.currentTime = currentTime;
        this.timeDelta = timeDelta;
    }

    /** {@return the time service for the level} */
    public TimeService getTimeService() {
        return timeService;
    }

    /** {@return the new time set during this tick} */
    public Time getCurrentTime() {
        return currentTime;
    }

    /** {@return the time that has elapsed during this tick} */
    public Time getTimeDelta() {
        return timeDelta;
    }

    /** {@return the level in which this time tick event occurred} */
    public ServerLevelWrapper getLevel() {
        return getTimeService().level;
    }

}
