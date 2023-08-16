package betterdays.time.effects;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class PotionTimeEffectForge extends PotionTimeEffect implements IForgeRegistryEntry<PotionTimeEffect> {

    private ResourceLocation name;
    private final Supplier<PotionTimeEffect> potionTimeEffectSupplier;

    public PotionTimeEffectForge(ResourceLocation name, Supplier<PotionTimeEffect> potionTimeEffectSupplier) {
        this.name = name;
        this.potionTimeEffectSupplier = potionTimeEffectSupplier;
    }

    @Override
    public PotionTimeEffect setRegistryName(ResourceLocation name) {
        this.name = name;
        return potionTimeEffectSupplier.get();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<PotionTimeEffect> getRegistryType() {
        return PotionTimeEffect.class;
    }

}
