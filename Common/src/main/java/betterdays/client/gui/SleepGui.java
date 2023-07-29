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

package betterdays.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import betterdays.config.ConfigHandler;

/**
 * This class handles modifications to the sleep interface.
 */
public class SleepGui {

    private static ItemStack clock = new ItemStack(Items.CLOCK);

    /**
     * Event listener that is called once per client tick. Updates the clock texture to prevent
     * clock wobble when getting in bed.
     */
    public static void onClientTick(Minecraft minecraft) {
        if (minecraft.player != null
                && minecraft.player.isSleeping()
                && ConfigHandler.Client.preventClockWobble()
                && minecraft.level != null
                && !minecraft.isPaused()
                && clockEnabled()) {
            // Render a clock every tick to prevent clock wobble after getting in bed.
            minecraft.getItemRenderer().getModel(clock, minecraft.level, minecraft.player, 0);
        }
    }

    /**
     * Event listener that is called during GUI rendering. Renders additional GUI elements.
     */
    public static void onGuiEvent(Screen screen, GuiGraphics guiGraphics) {
        if (clockEnabled()) {
            renderSleepInterface(screen, guiGraphics);
        }
    }

    /**
     * Renders the interface that displays extra information over the sleep screen.
     */
    public static void renderSleepInterface(Screen screen, GuiGraphics guiGraphics) {
        float x, y;
        int scale = ConfigHandler.Client.clockScale();
        int margin = ConfigHandler.Client.clockMargin();
        ScreenAlignment alignment = ConfigHandler.Client.clockAlignment();

        if (alignment == ScreenAlignment.TOP_LEFT
                || alignment == ScreenAlignment.CENTER_LEFT
                || alignment == ScreenAlignment.BOTTOM_LEFT) {
            x = margin;
        } else if (alignment == ScreenAlignment.TOP_CENTER
                || alignment == ScreenAlignment.CENTER_CENTER
                || alignment == ScreenAlignment.BOTTOM_CENTER) {
            x = screen.width / 2 - scale / 2;
        } else {
            x = screen.width - scale - margin;
        }

        if (alignment == ScreenAlignment.TOP_LEFT
                || alignment == ScreenAlignment.TOP_CENTER
                || alignment == ScreenAlignment.TOP_RIGHT) {
            y = margin;
        } else if (alignment == ScreenAlignment.CENTER_LEFT
                || alignment == ScreenAlignment.CENTER_CENTER
                || alignment == ScreenAlignment.CENTER_RIGHT) {
            y = screen.height / 2 - scale / 2;
        } else {
            y = screen.height - scale - margin;
        }

        renderClock(guiGraphics, x, y, scale);
    }

    /**
     * Renders a clock on the screen.
     */
    public static void renderClock(GuiGraphics guiGraphics, float x, float y, float scale) {
        scale /= 16F;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 0);
        guiGraphics.renderItem(clock, 0, 0);
        guiGraphics.pose().popPose();
    }

    public static boolean clockEnabled() {
        return ConfigHandler.Common.enableSleepFeature() && ConfigHandler.Common.displayBedClock();
    }

}
