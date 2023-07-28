package betterdays.event;

import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import betterdays.client.gui.SleepGui;
import betterdays.client.TimeInterpolator;

public class ClientEventListener {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();

            SleepGui.onClientTick(minecraft);
        }
    }

    @SubscribeEvent
    public static void onGuiEvent(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InBedChatScreen) {
            SleepGui.onGuiEvent(event.getScreen(), event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        TimeInterpolator.onWorldLoad(event.getLevel());
    }

    @SubscribeEvent
    public static void onWorldUnload(LevelEvent.Unload event) {
        TimeInterpolator.onWorldUnload(event.getLevel());
    }

    @SubscribeEvent
    public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            TimeInterpolator.onRenderTickEvent(event.renderTickTime);
        }
    }

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();

            TimeInterpolator.onClientTickEvent(minecraft);
        }
    }

}
