package betterdays.mixin;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import betterdays.message.BetterDaysMessages;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "startSleepInBed", at = @At(value = "RETURN"))
    private void betterdays$startSleeping(BlockPos pos, CallbackInfoReturnable<Either<BedSleepingProblem, Unit>> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        if (cir.getReturnValue().right().isPresent()) {
            BetterDaysMessages.onSleepingCheckEvent(player);
        }
    }

}
