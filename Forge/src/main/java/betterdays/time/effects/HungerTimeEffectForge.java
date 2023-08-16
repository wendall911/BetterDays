package betterdays.time.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class HungerTimeEffectForge extends HungerTimeEffect implements IForgeRegistryEntry<HungerTimeEffect> {

    private ResourceLocation name;
    private final Supplier<HungerTimeEffect> hungerTimeEffectSupplier;

    public HungerTimeEffectForge(ResourceLocation name, Supplier<HungerTimeEffect> hungerTimeEffectSupplier) {
        this.name = name;
        this.hungerTimeEffectSupplier = hungerTimeEffectSupplier;
    }

    @Override
    public HungerTimeEffect setRegistryName(ResourceLocation name) {
        this.name = name;
        return hungerTimeEffectSupplier.get();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<HungerTimeEffect> getRegistryType() {
        return HungerTimeEffect.class;
    }

}
