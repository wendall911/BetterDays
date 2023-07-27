package betterdays;

import net.minecraftforge.common.MinecraftForge;

import betterdays.event.ClientEventListener;

public class BetterDaysClientForge {

    public BetterDaysClientForge() {
        MinecraftForge.EVENT_BUS.register(ClientEventListener.class);
    }

}
