package betterdays.utils;

import betterdays.BetterDays;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper extends technology.roughness.whitenoise.util.ResourceLocationHelper {

    public static ResourceLocation prefix(String path) {
        return loc(BetterDays.MODID, path);
    }

}
