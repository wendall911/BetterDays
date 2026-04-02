package betterdays.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.clock.ServerClockManager;

import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.common.util.ClockAdjustment;
import net.neoforged.neoforge.event.EventHooks;

import betterdays.platform.services.IPlatform;
import betterdays.wrappers.ServerLevelWrapper;

public class NeoForgePlatform implements IPlatform {

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        EventHooks.onSleepFinished(levelWrapper.get(), new ClockAdjustment.Absolute(time));
    }

    @Override
    public @NotNull Field findField(@NotNull Class<?> clazz, @NotNull String name) throws NoSuchFieldException {
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
    public @NotNull Method findMethod(@NotNull Class<?> clazz, @NotNull String name, Class<?> @NotNull ... parameters) throws NoSuchMethodException {
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
    public void setTimeSpeed(ServerLevelWrapper level, float speed) {
        ServerClockManager clockManager = level.get().clockManager();

        level.get().dimensionType().defaultClock().ifPresent(defaultClock -> clockManager.setRate(defaultClock, speed));
    }

}
