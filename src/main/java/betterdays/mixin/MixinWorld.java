package betterdays.mixin;

import betterdays.BetterDays;
import betterdays.util.TimeMap;

import net.minecraft.util.Util;
import net.minecraft.world.GameRules;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.World;

import org.apache.commons.lang3.time.DurationFormatUtils;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(World.class)
public abstract class MixinWorld {

    @Final
    @Shadow
    public Dimension dimension;

    @Shadow
    @Final
    protected WorldInfo levelData;

    private final TimeMap timeMap = new TimeMap();
    private long segmentStartTicks = 0L;
    private long segmentTicks = 0L;
    private long segmentStartTime = -1L;
    private long segmentTime = 0L;
    private long lastDayTicks = 0L;

    private long checkTime = 0L;

    @ModifyConstant(method = "tickTime", constant = @Constant(longValue = 1L), require = 2, expect = 2)
    public long getTickLength(final long value) {
        if (dimension.getType() == DimensionType.OVERWORLD) {
            if (!levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                return value;
            }

            long dayTime = levelData.getDayTime();
            long dayTicks = getDayTicks(dayTime);
            double targetTickRate = timeMap.getTickRate(dayTicks);
            long segmentStart = timeMap.getSegmentStart(dayTicks);
            long now = 0L;
            double tickRate = 0.0D;
            double tickRateDiff = 0.0D;
            long duration = 0L;

            if (targetTickRate >= 20.0D) {
                return value;
            }
            
            if (lastDayTicks == 0L) {
                lastDayTicks = dayTicks;
            }
            else if (dayTicks - lastDayTicks > 50 || dayTicks - lastDayTicks < 0) {
                // Catch if /time set XXX was called
                segmentStartTime = -1L;
            }

            lastDayTicks = dayTicks;

            now = Util.getMillis();

            if (segmentStartTime == -1L || dayTicks == segmentStart) {

                segmentStartTime = now;
                segmentTime = 0L;
                segmentTicks = 0L;
                segmentStartTicks = dayTicks;
                if (checkTime != 0L) {
                    duration = now - checkTime;
                    if (duration > 999L) {
                        String durationString = DurationFormatUtils.formatDuration(duration, "H:mm:ss", true);
                        String msg = String.format("%s dayTime: %d, dayTicks: %d Tick Since Change: %d", durationString, dayTime, dayTicks, segmentTicks);
                        BetterDays.logger.debug(msg);
                        checkTime = Util.getMillis();
                    }
                }
                else {
                    checkTime = now;
                }
            }

            if (segmentTicks != 0L) {
                tickRate = (double)segmentTicks / (segmentTime / 1000.0D);
                tickRateDiff = tickRate - targetTickRate;
            }

            segmentTicks += dayTicks - segmentStartTicks;
            segmentTime += now - segmentStartTime;

            if (tickRateDiff > 0.01D) {
                return 0L;
            }
        }

        return value;
    }

    private long getDayTicks(long dayTime) {
        if (dayTime <= 24000L) {
            return dayTime;
        }
        else {
            return dayTime % 24000L;
        }
    }
}

