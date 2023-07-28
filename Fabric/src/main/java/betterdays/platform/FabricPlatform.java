package betterdays.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import betterdays.message.BetterDaysMessages;
import betterdays.platform.services.IPlatform;
import betterdays.wrappers.ServerLevelWrapper;

public class FabricPlatform implements IPlatform {

    static HashMap<String, String> loomMapping = new HashMap<>();

    static {
        loomMapping.put("tickBlockEntities", "method_18471");
        loomMapping.put("sleepStatus", "field_28859");
        loomMapping.put("tickEffects", "method_6050");
    }

    @Override
    public ResourceLocation getResourceLocation(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    @Override
    public boolean isModLoaded(String name) {
        return FabricLoader.getInstance().isModLoaded(name);
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        BetterDaysMessages.onSleepFinishedEvent(levelWrapper.get());
    }

    @Override
    public @NotNull Field findField(@NotNull Class<?> clazz, @NotNull String name) throws NoSuchFieldException {
        String mappedName = FabricLoader.getInstance().isDevelopmentEnvironment() ? name : loomMapping.get(name);
        final Field field = clazz.getDeclaredField(mappedName);
        field.setAccessible(true);
        return field;
    }

    @Override
    public @NotNull Method findMethod(@NotNull Class<?> clazz, @NotNull String name, Class<?> @NotNull ... parameters) throws NoSuchMethodException {
        String mappedName = FabricLoader.getInstance().isDevelopmentEnvironment() ? name : loomMapping.get(name);
        final Method method = clazz.getDeclaredMethod(mappedName, parameters);
        method.setAccessible(true);
        return method;
    }

}
