package betterdays;

import net.neoforged.bus.api.IEventBus;

import betterdays.event.ClientEventListener;
import net.neoforged.neoforge.common.NeoForge;

public class BetterDaysClientNeoForge {

    public static void init(IEventBus eventBus) {
        NeoForge.EVENT_BUS.register(new ClientEventListener());
    }

}
