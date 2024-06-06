package betterdays;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

import betterdays.event.ServerEventListener;

@Mod(BetterDays.MODID)
public class BetterDaysNeoForge {

    public BetterDaysNeoForge(IEventBus eventBus) {
        BetterDays.init();
        BetterDays.initConfig();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BetterDaysClientNeoForge.init(eventBus);
        }

        eventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ServerEventListener());
    }

}
