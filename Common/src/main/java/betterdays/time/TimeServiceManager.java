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

import net.minecraft.world.level.LevelAccessor;

import betterdays.wrappers.ServerLevelWrapper;

/**
 * Creates {@link TimeService} objects and passes events to them.
 */
public class TimeServiceManager {

    /** The Overworld {@code TimeService} object. null if Overworld not loaded. */
    public static TimeService service;

    /**
     * Event listener that is called when a new level is loaded.
     *
     * @param level current world level
     */
    public static void onWorldLoad(LevelAccessor level) {
        if (ServerLevelWrapper.isServerLevel(level)) {
            ServerLevelWrapper wrappedLevel = new ServerLevelWrapper(level);
            if (wrappedLevel.get().equals(wrappedLevel.get().getServer().overworld())) {
                service = new TimeService(wrappedLevel);
            }
        }
    }

    /**
     * Event listener that is called when a level is unloaded.
     *
     * @param level current world level
     */
    public static void onWorldUnload(LevelAccessor level) {
        if (service != null && service.level.get() == level) {
            service = null;
        }
    }

    /**
     * Event listener that is called every tick per level.
     *
     * @param level current world level
     */
    public static void onWorldTick(LevelAccessor level) {
        if (service != null && service.level.get() == level) {
            service.tick();
        }
    }

}
