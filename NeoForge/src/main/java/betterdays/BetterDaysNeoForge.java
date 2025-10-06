package betterdays;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import technology.roughness.whitenoise.platform.Services;

import betterdays.event.ServerEventListener;

@Mod(BetterDays.MODID)
public class BetterDaysNeoForge {

    public BetterDaysNeoForge(IEventBus eventBus) {
        BetterDays.init();
        BetterDays.initConfig();

        if (Services.PLATFORM.isPhysicalClient()) {
            BetterDaysClientNeoForge.init(eventBus);
        }

        eventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ServerEventListener());
    }

}
