package betterdays.mixin;

import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin {

    // Does nothing, here so I can generate mapping with tiny mapper
    @Inject(method = "tickBlockEntities", at = @At(value = "HEAD"))
    private void betterdays$tickBlockEntities(CallbackInfo ci) {
    }

}
