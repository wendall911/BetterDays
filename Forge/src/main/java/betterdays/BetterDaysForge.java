package betterdays;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import betterdays.config.ConfigHandler;
import betterdays.event.ServerEventListener;
import betterdays.platform.Services;

@Mod(BetterDays.MODID)
public class BetterDaysForge {

    public BetterDaysForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        BetterDays.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        if (Services.PLATFORM.isPhysicalClient()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
            bus.addListener(this::configSetup);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ServerEventListener.class);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BetterDaysClientForge::new);
    }

    private void configSetup(final ModConfigEvent.Loading event) {
        ConfigHandler.init();
    }

}
