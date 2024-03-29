/*
 * Derived from Hourglass mod
 * https://github.com/DuckyCrayfish/hourglass
 * Copyright (C) 2021 Nick Iacullo
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

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.server.level.ServerPlayer;

import betterdays.wrappers.ServerPlayerWrapper;

/**
 * This class keeps track of the number of active and sleeping players in a level.
 *
 * This class mimics all functionality of Minecraft's SleepStatus class in 1.17+, but includes the
 * ability to conditionally block vanilla sleep functionality.
 *
 * This class also includes a number of utility methods and getters for use in Better Days.
 */
public class SleepStatus extends net.minecraft.server.players.SleepStatus {

    /** The number of active (online and not spectating) players in this dimension. */
    protected int activePlayerCount;
    /** The number of sleeping players in this dimension. */
    protected int sleepingPlayerCount;
    /** A {@code Supplier} that determines whether or not vanilla sleep should be suppressed. */
    protected Supplier<Boolean> preventSleepSupplier;

    /**
     * Creates a new instance.
     *
     * @param preventSleepSupplier  a supplier that should return true when vanilla sleep
     * functionality should be blocked, false otherwise.
     */
    public SleepStatus(Supplier<Boolean> preventSleepSupplier) {
        this.preventSleepSupplier = preventSleepSupplier;
    }

    /**
     * Resets the sleeping player count.
     * Mimics super method in 1.17+.
     */
    public void removeAllSleepers() {
        sleepingPlayerCount = 0;
    }

    /**
     * {@return the number of sleeping players}
     * Mimics super method in 1.17+.
     */
    public int amountSleeping() {
        return sleepingPlayerCount;
    }

    /**
     * {@return the number of players currently active (not spectating)}
     * Mimics super method in 1.17+.
     */
    public int amountActive() {
        return activePlayerCount;
    }

    /** {@return true when all players are awake, false otherwise} */
    public boolean allAwake() {
        return sleepingPlayerCount == 0;
    }

    /** {@return true when all players are sleeping, false otherwise} */
    public boolean allAsleep() {
        return sleepingPlayerCount == activePlayerCount;
    }

    /** {@return the ratio of sleeping players to active players. Value between 0.0 and 1.0} */
    public double ratio() {
        return (double) sleepingPlayerCount / (double) activePlayerCount;
    }

    /**
     * {@return the percentage of sleeping players to active players as an integer between 0 and 100}
     */
    public int percentage() {
        return (int) (ratio() * 100D);
    }

    /**
     * Updates this object's player counts based on {@code playerList}.
     * @param playerList  the list of players to count
     */
    public void updatePlayerCounts(List<ServerPlayer> playerList) {
        activePlayerCount = 0;
        sleepingPlayerCount = 0;

        for (ServerPlayer player : playerList) {
            if (!player.isSpectator()) {
                activePlayerCount++;
                if (player.isSleeping()) {
                    sleepingPlayerCount++;
                }
            }
        }
    }

    /**
     * Returns the number of sleeping players required to meet the {@code percentageRequired} sleep
     * threshold based on current active player count.
     *
     * This method is only called on 1.17+.
     * Mimics super method in 1.17+.
     *
     * @param percentageRequired  percentage on which to calculate required sleeping player count
     * @return the number of sleeping players required to meet the sleep threshold
     */
    public int sleepersNeeded(int percentageRequired) {
        return Math.max(1, (int) Math.ceil(activePlayerCount * percentageRequired / 100.0D));
    }

    /**
     * This method should only be called by vanilla code.
     *
     * Returns true if enough players are sleeping to pass the night, false otherwise.
     * This method always returns false if {@link #preventSleepSupplier} returns true.
     *
     * This method is only called on 1.17+.
     * When {@link #preventSleepSupplier} returns false, this method mimics super method in 1.17+.
     * When {@link #preventSleepSupplier} returns true, this method blocks vanilla sleep in 1.17+.
     *
     * @param percentageRequired  percentage on which to calculate required sleeping player count
     */
    public boolean areEnoughSleeping(int percentageRequired) {
        if (preventSleepSupplier.get()) {
            return false;
        } else {
            return sleepingPlayerCount >= sleepersNeeded(percentageRequired);
        }
    }

    /**
     * This method should only be called by vanilla code.
     *
     * Returns true if enough players are in a deep sleep to pass the night, false otherwise.
     * This method always returns false if {@link #preventSleepSupplier} returns true.
     *
     * This method is only called on 1.17+.
     * When {@link #preventSleepSupplier} returns false, this method mimics super method in 1.17+.
     * When {@link #preventSleepSupplier} returns true, this method blocks vanilla sleep in 1.17+.
     *
     * @param percentageRequired  percentage on which to calculate required sleeping player count
     */
    public boolean areEnoughDeepSleeping(int percentageRequired, List<ServerPlayer> playerList) {
        if (preventSleepSupplier.get()) {
            return false;
        }

        long deepSleepers = playerList.stream()
                .map(ServerPlayerWrapper::new)
                .filter(ServerPlayerWrapper::isSleepingLongEnough)
                .count();

        return deepSleepers >= sleepersNeeded(percentageRequired);
    }

    /**
     * This method updates this object's player counts and returns true or false if sleeping player
     * messages should be displayed.
     *
     * This method is only called on 1.17+.
     * When {@link #preventSleepSupplier} returns false, this method mimics super method in 1.17+.
     * When {@link #preventSleepSupplier} returns true, this method blocks vanilla sleep messages in 1.17+.
     *
     * @param playerList  the list of players to count
     */
    public boolean update(List<ServerPlayer> playerList) {
        int oldActiveCount = activePlayerCount;
        int oldSleepingCount = sleepingPlayerCount;

        updatePlayerCounts(playerList);

        if (preventSleepSupplier.get()) {
            return false;
        } else {
            boolean noSleepers = oldSleepingCount == 0 && sleepingPlayerCount == 0;
            boolean valueChanged = oldActiveCount != activePlayerCount || oldSleepingCount != sleepingPlayerCount;
            return !noSleepers && valueChanged;
        }
    }

}
