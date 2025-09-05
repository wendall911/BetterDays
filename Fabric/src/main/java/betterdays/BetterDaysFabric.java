package betterdays;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;

import net.fabricmc.api.ModInitializer;

import net.neoforged.fml.config.ModConfig;

import betterdays.config.ConfigHandler;
import betterdays.event.ServerEventListener;
import betterdays.platform.Services;

public class BetterDaysFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BetterDays.init();
        ServerEventListener.setup();

        NeoForgeConfigRegistry.INSTANCE.register(BetterDays.MODID, ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        if (Services.PLATFORM.isPhysicalClient()) {
            NeoForgeConfigRegistry.INSTANCE.register(BetterDays.MODID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
            NeoForgeModConfigEvents.loading(BetterDays.MODID).register((ModConfig config) -> {
                if (config.getSpec() == ConfigHandler.CLIENT_SPEC) {
                    ConfigHandler.init();
                }
            });
        }
    }


}
