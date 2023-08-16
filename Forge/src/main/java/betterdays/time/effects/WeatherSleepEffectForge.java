package betterdays.time.effects;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class WeatherSleepEffectForge extends WeatherSleepEffect implements IForgeRegistryEntry<WeatherSleepEffect> {

    private ResourceLocation name;
    private final Supplier<WeatherSleepEffect> weatherSleepEffectSupplier;

    public WeatherSleepEffectForge(ResourceLocation name, Supplier<WeatherSleepEffect> weatherSleepEffectSupplier) {
        this.name = name;
        this.weatherSleepEffectSupplier = weatherSleepEffectSupplier;
    }

    @Override
    public WeatherSleepEffect setRegistryName(ResourceLocation name) {
        this.name = name;
        return weatherSleepEffectSupplier.get();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<WeatherSleepEffect> getRegistryType() {
        return WeatherSleepEffect.class;
    }

}
