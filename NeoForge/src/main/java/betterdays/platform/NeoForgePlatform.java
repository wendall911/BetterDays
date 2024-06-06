package betterdays.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.event.EventHooks;

import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import betterdays.platform.services.IPlatform;
import betterdays.wrappers.ServerLevelWrapper;

public class NeoForgePlatform implements IPlatform {

    static HashMap<String, String> srgMapping = new HashMap<>();

    static {
        srgMapping.put("tickBlockEntities", "m_46463_");
        srgMapping.put("sleepStatus", "f_143245_");
        srgMapping.put("tickEffects", "m_21217_");
    }

    @Override
    public ResourceLocation getResourceLocation(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    @Override
    public boolean isModLoaded(String name) {
        return ModList.get().isLoaded(name);
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        EventHooks.onSleepFinished(levelWrapper.get(), time, time);
    }

    @Override
    public @NotNull Field findField(@NotNull Class<?> clazz, @NotNull String name) throws NoSuchFieldException {
        try {
            final Field field = ObfuscationReflectionHelper.findField(clazz, srgMapping.get(name));
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
            final Method method = ObfuscationReflectionHelper.findMethod(clazz, srgMapping.get(name));
            method.setAccessible(true);
            return method;
        }
        catch (Exception e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

}
