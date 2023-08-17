package betterdays.compat.sereneseasons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import sereneseasons.season.SeasonTime;

import betterdays.config.ConfigHandler;

@Mixin(SeasonTime.class)
public class MixinSeasonTime {

    @Inject(method = "getDayDuration", at = @At("HEAD"), cancellable = true)
    private void betterdays$getDayDuration(CallbackInfoReturnable<Integer> cir) {
        if (ConfigHandler.Common.sereneSeasonsFix()) {
            cir.setReturnValue(24000);
        }
    }

}
