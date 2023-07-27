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

import betterdays.registry.TimeEffectsRegistry;
import betterdays.time.TimeContext;
import betterdays.time.TimeService;

/**
 * Base interface used for time effects.
 *
 * A time effect is anything that uses the speed of time to affect something in the game.
 *
 * Implementations of this class should be registered to the {@link TimeEffectsRegistry#KEY}
 * registry.
 */
public interface TimeEffect {

    /**
     * Method that is called by {@link TimeService} every tick after time has been adjusted.
     *
     * @param context  the context of the time adjustment
     */
    public void onTimeTick(TimeContext context);

}
