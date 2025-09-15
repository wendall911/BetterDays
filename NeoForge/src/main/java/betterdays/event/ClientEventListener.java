package betterdays.event;

import net.minecraft.client.gui.screens.InBedChatScreen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import betterdays.client.gui.SleepGui;

public class ClientEventListener {

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();

        SleepGui.onClientTick(minecraft);
    }

    @SubscribeEvent
    public void onGuiEvent(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InBedChatScreen) {
            SleepGui.onGuiEvent(event.getScreen(), event.getGuiGraphics());
        }
    }

}
