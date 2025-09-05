package betterdays;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;

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

        ConfigRegistry.INSTANCE.register(BetterDays.MODID, ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        if (Services.PLATFORM.isPhysicalClient()) {
            ConfigRegistry.INSTANCE.register(BetterDays.MODID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
            ModConfigEvents.loading(BetterDays.MODID).register((ModConfig config) -> {
                if (config.getSpec() == ConfigHandler.CLIENT_SPEC) {
                    ConfigHandler.init();
                }
            });
        }
    }


}
