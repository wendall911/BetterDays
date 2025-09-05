package betterdays;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;

import net.fabricmc.api.ModInitializer;

import net.minecraftforge.fml.config.ModConfig;

import betterdays.config.ConfigHandler;
import betterdays.event.ServerEventListener;
import betterdays.platform.Services;

public class BetterDaysFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BetterDays.init();
        ServerEventListener.setup();

        ForgeConfigRegistry.INSTANCE.register(BetterDays.MODID, ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        if (Services.PLATFORM.isPhysicalClient()) {
            ForgeConfigRegistry.INSTANCE.register(BetterDays.MODID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
            ModConfigEvents.loading(BetterDays.MODID).register((ModConfig config) -> ConfigHandler.init());
        }
    }


}
