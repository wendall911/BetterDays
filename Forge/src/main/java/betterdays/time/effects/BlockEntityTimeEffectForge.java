package betterdays.time.effects;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class BlockEntityTimeEffectForge extends BlockEntityTimeEffect implements IForgeRegistryEntry<BlockEntityTimeEffect> {

    private ResourceLocation name;
    private final Supplier<BlockEntityTimeEffect> blockEntityTimeEffectSupplier;

    public BlockEntityTimeEffectForge(ResourceLocation name, Supplier<BlockEntityTimeEffect> blockEntityTimeEffectSupplier) {
        this.name = name;
        this.blockEntityTimeEffectSupplier = blockEntityTimeEffectSupplier;
    }

    @Override
    public BlockEntityTimeEffect setRegistryName(ResourceLocation name) {
        this.name = name;
        return blockEntityTimeEffectSupplier.get();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<BlockEntityTimeEffect> getRegistryType() {
        return BlockEntityTimeEffect.class;
    }

}
