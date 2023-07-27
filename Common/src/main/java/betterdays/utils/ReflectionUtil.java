package betterdays.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

public class ReflectionUtil {

    public static @NotNull Field findField(final @NotNull Class<?> clazz, final @NotNull String name) throws NoSuchFieldException {
        final Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static @NotNull Method findMethod(final @NotNull Class<?> clazz, final @NotNull String name, final Class<?> @NotNull ... parameters) throws NoSuchMethodException {
        final Method method = clazz.getDeclaredMethod(name, parameters);
        method.setAccessible(true);

        return method;
    }

}
