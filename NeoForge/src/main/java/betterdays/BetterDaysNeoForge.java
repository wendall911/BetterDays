package betterdays;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

import betterdays.config.ConfigHandler;
import betterdays.event.ServerEventListener;

@Mod(BetterDays.MODID)
public class BetterDaysNeoForge {

    public BetterDaysNeoForge(IEventBus eventBus) {
        BetterDays.init();
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BetterDaysClientNeoForge.init(eventBus);
            ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
            eventBus.addListener(this::configSetup);
        }

        eventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ServerEventListener());
    }

    private void configSetup(final ModConfigEvent.Loading event) {
        ConfigHandler.init();
    }

}
