package betterdays.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.jspecify.annotations.NonNull;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.Level;

import technology.roughness.whitenoise.platform.Services;

import betterdays.message.BetterDaysMessages;
import betterdays.platform.services.IPlatform;
import betterdays.utils.HomeostaticSeasonsHelper;
import betterdays.utils.SeasonHelper;
import betterdays.wrappers.ServerLevelWrapper;

public class FabricPlatform implements IPlatform {

    static HashMap<String, String> loomMapping = new HashMap<>();

    static {
        loomMapping.put("tickBlockEntities", "method_18471");
        loomMapping.put("sleepStatus", "field_28859");
        loomMapping.put("tickEffects", "method_6050");
    }

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        BetterDaysMessages.onSleepFinishedEvent(levelWrapper.get());
    }

    @Override
    public @NonNull Field findField(@NonNull Class<?> clazz, @NonNull String name) throws NoSuchFieldException {
        String mappedName = FabricLoader.getInstance().isDevelopmentEnvironment() ? name : loomMapping.get(name);
        final Field field = clazz.getDeclaredField(mappedName);
        field.setAccessible(true);
        return field;
    }

    @Override
    public @NonNull Method findMethod(@NonNull Class<?> clazz, @NonNull String name, Class<?> @NonNull ... parameters) throws NoSuchMethodException {
        String mappedName = FabricLoader.getInstance().isDevelopmentEnvironment() ? name : loomMapping.get(name);
        final Method method = clazz.getDeclaredMethod(mappedName, parameters);
        method.setAccessible(true);
        return method;
    }

    @Override
    public ModLoader getModLoader() {
        return ModLoader.FABRIC;
    }

    @Override
    public int getSeasonDay(Level level) {
        int day = SeasonHelper.days[0];

        if (Services.PLATFORM.isModLoaded("homeostaticseasons") && HomeostaticSeasonsHelper.isDimensionWhitelisted(level.dimension())) {
            day = HomeostaticSeasonsHelper.getSeasonDay(level);
        }

        return day;
    }

}
