package betterdays.mixin;

import java.util.List;
import java.util.function.BooleanSupplier;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.world.level.GameRules;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import betterdays.message.BetterDaysMessages;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Final @Shadow private SleepStatus sleepStatus;
    @Final @Shadow List<ServerPlayer> players;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void $betterdaysSleepFinishedTimeEvent(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        int i = level.getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);
        boolean daylightRule = level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);

        if (sleepStatus.areEnoughSleeping(i) && sleepStatus.areEnoughDeepSleeping(i, players) && daylightRule) {
            BetterDaysMessages.onSleepFinishedEvent(level);
        }
    }

}
