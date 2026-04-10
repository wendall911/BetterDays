package betterdays.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import betterdays.platform.Services;
import org.jspecify.annotations.NonNull;

public class ReflectionUtil {

    public static @NonNull Field findField(final @NonNull Class<?> clazz, final @NonNull String name) throws NoSuchFieldException {
        return Services.PLATFORM.findField(clazz, name);
    }

    public static @NonNull Method findMethod(final @NonNull Class<?> clazz, final @NonNull String name, final Class<?> @NonNull ... parameters) throws NoSuchMethodException {
        return Services.PLATFORM.findMethod(clazz, name, parameters);
    }

}
