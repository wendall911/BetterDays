package betterdays.utils;

import net.minecraft.world.level.Level;

import betterdays.platform.Services;

public class SeasonHelper {

    public static final int[] days = { 110, 141, 172, 204, 235, 266, 296, 326, 355, 20, 50, 80 };

    private static int lastSeasonDay = -1;
    private static double daylightHoursCache = -1;

    /*
     * Based upon : "A model comparison for daylength as a function of latitude and day of year"
     * Forsythe et al., 1995, Ecological Modelling 80 (1995) 87-95
     *
     * https://lhypercube.arep.fr/en/blog/daylength/
     */
    public static double getDayRatio(Level level, double latitude) {
        int day = Services.PLATFORM.getSeasonDay(level);

        if (lastSeasonDay != day) {
            double P = Math.asin(0.39795 * Math.cos(0.2163108 + 2 * Math.atan(0.9671396 * Math.tan(0.00860 * (day - 186)))));
            double daylightHours = 24 - (24 / Math.PI) * Math.acos((Math.sin(0.8333 * Math.PI / 180) + Math.sin(latitude * Math.PI / 180) * Math.sin(P)) / (Math.cos(latitude * Math.PI / 180) * Math.cos(P)));

            lastSeasonDay = day;
            daylightHoursCache = daylightHours / 24;
        }

        return daylightHoursCache;
    }

    public static double getNightRatio(Level level, double latitude) {
        return 1.0 - getDayRatio(level, latitude);
    }

}
