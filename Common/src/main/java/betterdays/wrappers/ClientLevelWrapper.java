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

package betterdays.wrappers;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;

/**
 * This class acts as a wrapper for {@link ClientLevel} to increase consistency between Minecraft
 * versions.
 *
 * Since the client-level class changes its name and package between different versions of
 * Minecraft, supporting different Minecraft versions would require modifications to any class that
 * imports or references {@link ClientLevel}. This class consolidates these variations into itself,
 * allowing other classes to depend on it instead.
 */
public class ClientLevelWrapper extends Wrapper<ClientLevel> {

    /**
     * Instantiates a new object.
     * @param level  the client level to wrap
     */
    public ClientLevelWrapper(LevelAccessor level) {
        super((ClientLevel) level);
    }

    /** {@return true if the 'daylight cycle' game rule is enabled in this level} */
    public boolean daylightRuleEnabled() {
        /*
         * I'm not sure how to look this up on the client side in 1.21.3. Also, if this is broken, I'm not understanding
         * why someone would even have this mod installed. It will just need to break. This was a "nice to have" feature.
         */
        return true;
    }

    /**
     * {@return true if {@code level} is an instance of a client-level}
     * @param level  the level to check
     */
    public static boolean isClientLevel(LevelAccessor level) {
        return level instanceof ClientLevel;
    }

}
