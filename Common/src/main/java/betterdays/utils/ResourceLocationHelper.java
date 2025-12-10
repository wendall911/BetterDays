package betterdays.utils;

import betterdays.BetterDays;

import net.minecraft.resources.Identifier;

public class ResourceLocationHelper extends technology.roughness.whitenoise.util.ResourceLocationHelper {

    public static Identifier prefix(String path) {
        return loc(BetterDays.MODID, path);
    }

}
