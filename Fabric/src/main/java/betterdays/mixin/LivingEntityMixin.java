package betterdays.mixin;

import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // Does nothing, here so I can generate mapping with tiny mapper
    @Inject(method = "tickEffects", at = @At(value = "HEAD"))
    private void betterdays$tickEffects(CallbackInfo ci) {}

}
