package betterdays.event;

import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.Minecraft;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import betterdays.client.gui.SleepGui;
import betterdays.client.TimeInterpolator;

public class ClientEventListener {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();

            SleepGui.onClientTick(minecraft);
        }
    }

    @SubscribeEvent
    public void onGuiEvent(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InBedChatScreen) {
            SleepGui.onGuiEvent(event.getScreen(), event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        TimeInterpolator.onWorldLoad(event.getLevel());
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        TimeInterpolator.onWorldUnload(event.getLevel());
    }

    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            TimeInterpolator.onRenderTickEvent(event.renderTickTime);
        }
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();

            TimeInterpolator.onClientTickEvent(minecraft);
        }
    }

}
