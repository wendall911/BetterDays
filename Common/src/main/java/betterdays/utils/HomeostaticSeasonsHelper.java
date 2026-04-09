package betterdays.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import homeostaticseasons.api.HomeostaticSeasonsAPI;

import static betterdays.utils.SeasonHelper.days;

public class HomeostaticSeasonsHelper {

    public static int getSeasonDay(Level level) {
        return days[HomeostaticSeasonsAPI.getCurrentSeason(level).ordinal()];
    }

    public static boolean isDimensionWhitelisted(ResourceKey<Level> dimension) {
        return HomeostaticSeasonsAPI.isSeasonalDimension(dimension);
    }

}
