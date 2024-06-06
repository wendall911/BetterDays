package betterdays;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import betterdays.event.ServerEventListener;

@Mod(BetterDays.MODID)
public class BetterDaysForge {

    public BetterDaysForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        BetterDays.init();
        BetterDays.initConfig();
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ServerEventListener());

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BetterDaysClientForge::new);
    }

}
