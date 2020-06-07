package betterdays;

import betterdays.config.TimeConfig;
import betterdays.handler.PacketHandler;
import betterdays.proxy.CommonProxy;
import betterdays.proxy.ServerProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(BetterDays.MODID)
public class BetterDays {

    public static final String MODID = "betterdays";

    public static final Logger logger = LogManager.getFormatterLogger(BetterDays.MODID);

    public BetterDays() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TimeConfig.SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(TimeConfig::onLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TimeConfig::onFileChange);

        PacketHandler.setup();

        DistExecutor.runForDist(() -> CommonProxy::new, () -> ServerProxy::new);
    }
    
}

