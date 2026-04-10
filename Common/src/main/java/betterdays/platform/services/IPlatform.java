package betterdays.platform.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jspecify.annotations.NonNull;

import net.minecraft.world.level.Level;

import betterdays.platform.ModLoader;

import betterdays.wrappers.ServerLevelWrapper;
import betterdays.wrappers.ServerPlayerWrapper;

public interface IPlatform {

    void onSleepFinished(ServerLevelWrapper levelWrapper, long time);

    @NonNull Field findField(final @NonNull Class<?> clazz, final @NonNull String name) throws NoSuchFieldException;

    @NonNull Method findMethod(final @NonNull Class<?> clazz, final @NonNull String name, final Class<?> @NonNull ... parameters) throws NoSuchMethodException;

    ModLoader getModLoader();

    int getSeasonDay(Level level);

    void tickEffects(ServerPlayerWrapper playerWrapper);

}
