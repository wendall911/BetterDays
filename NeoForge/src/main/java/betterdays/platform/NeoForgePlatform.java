package betterdays.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jspecify.annotations.NonNull;

import net.minecraft.world.level.Level;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.common.util.ClockAdjustment;
import net.neoforged.neoforge.event.EventHooks;

import technology.roughness.whitenoise.platform.Services;

import betterdays.platform.services.IPlatform;
import betterdays.utils.HomeostaticSeasonsHelper;
import betterdays.utils.SeasonHelper;
import betterdays.wrappers.ServerLevelWrapper;

public class NeoForgePlatform implements IPlatform {

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        EventHooks.onSleepFinished(levelWrapper.get(), new ClockAdjustment.Absolute(time));
    }

    @Override
    public @NonNull Field findField(@NonNull Class<?> clazz, @NonNull String name) throws NoSuchFieldException {
        try {
            final Field field = ObfuscationReflectionHelper.findField(clazz, name);
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            throw new NoSuchFieldException(e.toString());
        }
    }

    @Override
    public @NonNull Method findMethod(@NonNull Class<?> clazz, @NonNull String name, Class<?> @NonNull ... parameters) throws NoSuchMethodException {
        try {
            final Method method = ObfuscationReflectionHelper.findMethod(clazz, name);
            method.setAccessible(true);
            return method;
        }
        catch (Exception e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

    @Override
    public ModLoader getModLoader() {
        return ModLoader.NEOFORGE;
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
