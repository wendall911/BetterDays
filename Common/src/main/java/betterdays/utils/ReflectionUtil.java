package betterdays.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import betterdays.platform.Services;
import org.jetbrains.annotations.NotNull;

public class ReflectionUtil {

    public static @NotNull Field findField(final @NotNull Class<?> clazz, final @NotNull String name) throws NoSuchFieldException {
        return Services.PLATFORM.findField(clazz, name);
    }

    public static @NotNull Method findMethod(final @NotNull Class<?> clazz, final @NotNull String name, final Class<?> @NotNull ... parameters) throws NoSuchMethodException {
        return Services.PLATFORM.findMethod(clazz, name, parameters);
    }

}
