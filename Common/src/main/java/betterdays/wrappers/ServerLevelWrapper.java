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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;

import betterdays.time.SleepStatus;

/**
 * This class acts as a wrapper for {@link ServerLevel} to increase consistency between Minecraft
 * versions.
 *
 * Since the server-level class changes its name and package between different versions of
 * Minecraft, supporting different Minecraft versions would require modifications to any class that
 * imports or references {@link ServerLevel}. This class consolidates these variations into itself,
 * allowing other classes to depend on it instead.
 */
public class ServerLevelWrapper extends Wrapper<ServerLevel> {

    // Store classes at the top to minimize file changes between Minecraft versions.
    private static final Class<ServerLevel> levelClass = ServerLevel.class;
    private static final Class<ServerLevelData> levelDataClass = ServerLevelData.class;

    /** The level-data of this level. */
    public final ServerLevelData levelData;

    /**
     * Instantiates a new object.
     * @param level  the server level to wrap
     */
    public ServerLevelWrapper(LevelAccessor level) {
        super(levelClass.cast(level));
        this.levelData = levelDataClass.cast(this.get().getLevelData());
    }

    /** {@return true if the 'daylight cycle' game rule is enabled in this level} */
    public boolean daylightRuleEnabled() {
        return this.get().getGameRules().get(GameRules.ADVANCE_TIME);
    }

    /** {@return true if the 'weather cycle' game rule is enabled in this level} */
    public boolean weatherRuleEnabled() {
        return this.get().getGameRules().get(GameRules.ADVANCE_WEATHER);
    }

    /**
     * Convenience method that returns true if the weather cycle is progressing in this level.
     * @return true if the weather cycle is progressing in this level
     */
    public boolean weatherCycleEnabled() {
        return weatherRuleEnabled() && this.get().dimensionType().hasSkyLight();
    }

    /**
     * Sets the level sleep status using reflection, as access modifiers prevent this.
     * In Minecraft versions lower than 1.17 this method should do nothing.
     *
     * @param newStatus  the new sleep status
     */
    public void setSleepStatus(SleepStatus newStatus) {
        wrapped.sleepStatus = newStatus;
    }

    /** Ticks all loaded block entities in this level. */
    public void tickBlockEntities() {
        try {
            wrapped.tickBlockEntities();
        }
        catch (Exception _) {}
    }

    /**
     * {@return true if {@code level} is an instance of a server level}
     * @param level  the level to check
     */
    public static boolean isServerLevel(LevelAccessor level) {
        return level != null && level.getClass() == levelClass;
    }

}
