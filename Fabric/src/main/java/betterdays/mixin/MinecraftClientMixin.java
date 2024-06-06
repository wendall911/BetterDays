package betterdays.mixin;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.multiplayer.ClientLevel;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import betterdays.client.TimeInterpolator;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable
    public ClientLevel level;

    @Shadow private float pausePartialTick;

    @Shadow private volatile boolean pause;

    @Shadow @Final private Timer timer;

    @Shadow public boolean noRender;

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void $betterdaysInjectSetLevel(ClientLevel levelClient, CallbackInfo ci) {
        if (this.level != null) {
            TimeInterpolator.onWorldLoad(this.level);
        }
    }

    @Inject(method = "disconnect()V", at = @At("HEAD"))
    private void $betterdaysInjectDisconnect(CallbackInfo ci) {
        if (this.level != null) {
            TimeInterpolator.onWorldLoad(this.level);
        }
    }

    @Inject(method = "runTick", at = @At(value = "HEAD"))
    private void $betterdaysInjectRunTick(boolean renderLevel, CallbackInfo ci) {
        if (!this.noRender) {
            TimeInterpolator.onRenderTickEvent(this.pause ? this.pausePartialTick : this.timer.partialTick);
        }
    }

}
