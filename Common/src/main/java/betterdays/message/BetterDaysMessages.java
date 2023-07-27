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

package betterdays.message;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import betterdays.config.ConfigHandler;
import betterdays.time.SleepStatus;
import betterdays.time.TimeService;
import betterdays.time.TimeServiceManager;
import betterdays.wrappers.ServerLevelWrapper;
import betterdays.wrappers.ServerPlayerWrapper;

/** This class listens for events and sends out BetterDays chat notifications. */
public class BetterDaysMessages {

    /**
     * Event listener that is called every tick for every player who is sleeping.
     * @param player sleeping player
     */
    public static void onSleepingCheckEvent(Player player) {
        TimeService service = TimeServiceManager.service;

        if (ConfigHandler.Common.enableSleepFeature()
                && player.getSleepTimer() == 2
                && player.getClass() == ServerPlayerWrapper.playerClass
                && service != null
                && service.level.get().equals(player.level)
                && service.level.get().players().size() > 1
                && service.level.daylightRuleEnabled()) {

            sendEnterBedMessage(new ServerPlayerWrapper(player));
        }
    }

    /**
     * Event listener that is called when a player gets out of bed.
     * @param player sleeping player
     */
    public static void onPlayerWakeUpEvent(Player player) {
        TimeService service = TimeServiceManager.service;

        if (ConfigHandler.Common.enableSleepFeature()
                && player.getClass() == ServerPlayerWrapper.playerClass
                && service != null
                && service.level.get().equals(player.level)
                && service.level.get().players().size() > 1
                && service.level.daylightRuleEnabled()) {

            sendLeaveBedMessage(new ServerPlayerWrapper(player));
        }
    }

    /**
     * Event listener that is called at morning when sleep has completed in a dimension.
     * @param level current level
     */
    public static void onSleepFinishedEvent(LevelAccessor level) {
        TimeService service = TimeServiceManager.service;

        if (ConfigHandler.Common.enableSleepFeature()
                && service != null
                && service.level.get().equals(level)
                && service.level.daylightRuleEnabled()) {

            ServerLevelWrapper levelWrapper = new ServerLevelWrapper(level);
            sendMorningMessage(levelWrapper);
        }
    }

    /**
     * Sends a message to all targeted players informing them that a player has entered their bed.
     *
     * @param player  the player who started sleeping
     */
    public static void sendEnterBedMessage(ServerPlayerWrapper player) {
        String templateMessage = ConfigHandler.Common.enterBedMessage();
        TimeService timeService = TimeServiceManager.service;

        if (templateMessage.isEmpty() || timeService == null) {
            return;
        }

        SleepStatus sleepStatus = timeService.sleepStatus;

        new TemplateMessage().setTemplate(templateMessage)
                .setOverlay(ConfigHandler.Common.enterBedMessageType().isOverlay())
                .setVariable("player", player.get().getGameProfile().getName())
                .setVariable("totalPlayers", Integer.toString(sleepStatus.amountActive()))
                .setVariable("sleepingPlayers", Integer.toString(sleepStatus.amountSleeping()))
                .setVariable("sleepingPercentage", Integer.toString(sleepStatus.percentage()))
                .bake().send(ConfigHandler.Common.enterBedMessageTarget(), player.getLevel());
    }

    /**
     * Sends a message to all targeted players informing them that a player has left their bed.
     *
     * @param player  the player who left their bed
     */
    public static void sendLeaveBedMessage(ServerPlayerWrapper player) {
        String templateMessage = ConfigHandler.Common.leaveBedMessage();
        TimeService timeService = TimeServiceManager.service;

        if (templateMessage.isEmpty() || timeService == null) {
            return;
        }

        SleepStatus sleepStatus = timeService.sleepStatus;

        new TemplateMessage().setTemplate(templateMessage)
                .setOverlay(ConfigHandler.Common.leaveBedMessageType().isOverlay())
                .setVariable("player", player.get().getGameProfile().getName())
                .setVariable("totalPlayers", Integer.toString(sleepStatus.amountActive()))
                .setVariable("sleepingPlayers", Integer.toString(sleepStatus.amountSleeping() - 1))
                .setVariable("sleepingPercentage", Integer.toString(sleepStatus.percentage()))
                .bake().send(ConfigHandler.Common.leaveBedMessageTarget(), player.getLevel());
    }

    /**
     * Sends a message to all targeted players informing them that the night has passed in level
     * after being accelerated by sleeping players.
     *
     * @param level  the level that night has passed in
     */
    public static void sendMorningMessage(ServerLevelWrapper level) {
        String templateMessage = ConfigHandler.Common.morningMessage();
        TimeService timeService = TimeServiceManager.service;

        if (templateMessage.isEmpty() || timeService == null) {
            return;
        }

        SleepStatus sleepStatus = timeService.sleepStatus;

        new TemplateMessage().setTemplate(templateMessage)
                .setOverlay(ConfigHandler.Common.morningMessageType().isOverlay())
                .setVariable("totalPlayers", Integer.toString(sleepStatus.amountActive()))
                .setVariable("sleepingPlayers", Integer.toString(sleepStatus.amountSleeping()))
                .setVariable("sleepingPercentage", Integer.toString(sleepStatus.percentage()))
                .bake().send(ConfigHandler.Common.morningMessageTarget(), level);
    }

}
