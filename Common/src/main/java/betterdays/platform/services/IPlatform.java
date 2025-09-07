package betterdays.platform.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import betterdays.wrappers.ServerLevelWrapper;

public interface IPlatform {

    void onSleepFinished(ServerLevelWrapper levelWrapper, long time);

    @NotNull Field findField(final @NotNull Class<?> clazz, final @NotNull String name) throws NoSuchFieldException;

    @NotNull Method findMethod(final @NotNull Class<?> clazz, final @NotNull String name, final Class<?> @NotNull ... parameters) throws NoSuchMethodException;

}
