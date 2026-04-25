package betterdays.platform;

import net.minecraft.world.level.Level;

import technology.roughness.whitenoise.platform.Services;

import betterdays.platform.services.IPlatform;
import betterdays.utils.HomeostaticSeasonsHelper;
import betterdays.utils.SeasonHelper;
import betterdays.wrappers.ServerPlayerWrapper;

public class NeoForgePlatform implements IPlatform {

    @Override
    public int getSeasonDay(Level level) {
        int day = SeasonHelper.days[0];

        if (Services.WN_PLATFORM.isModLoaded("homeostaticseasons") && HomeostaticSeasonsHelper.isDimensionWhitelisted(level.dimension())) {
            day = HomeostaticSeasonsHelper.getSeasonDay(level);
        }

        return day;
    }

    @Override
    public void tickEffects(ServerPlayerWrapper playerWrapper) {
        playerWrapper.get().tickEffects();
    }

}
