package betterdays.time.effects;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RandomTickSleepEffectForge extends RandomTickSleepEffect implements IForgeRegistryEntry<RandomTickSleepEffect> {

    private ResourceLocation name;
    private final Supplier<RandomTickSleepEffect> randomTickSleepEffectSupplier;

    public RandomTickSleepEffectForge(ResourceLocation name, Supplier<RandomTickSleepEffect> randomTickSleepEffectSupplier) {
        this.name = name;
        this.randomTickSleepEffectSupplier = randomTickSleepEffectSupplier;
    }

    @Override
    public RandomTickSleepEffect setRegistryName(ResourceLocation name) {
        this.name = name;
        return randomTickSleepEffectSupplier.get();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<RandomTickSleepEffect> getRegistryType() {
        return RandomTickSleepEffect.class;
    }

}
