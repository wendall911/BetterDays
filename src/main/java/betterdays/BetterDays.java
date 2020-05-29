package betterdays;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import betterdays.config.ConfigHandler;
import betterdays.proxy.CommonProxy;
import betterdays.proxy.ServerProxy;

@Mod(BetterDays.MODID)
public class BetterDays {

    public static final String MODID = "betterdays";

    public static final Logger logger = LogManager.getFormatterLogger(BetterDays.MODID);

    public BetterDays() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.SERVER_SPECS);

        DistExecutor.runForDist(() -> CommonProxy::new, () -> ServerProxy::new);
    }

}

