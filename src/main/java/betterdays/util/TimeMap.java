package betterdays.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NavigableMap;
import java.util.TreeMap;

import betterdays.config.TimeOption;
import betterdays.config.SyncedConfig;

public class TimeMap {
    
    private NavigableMap<Long, String> quarterDaySegments = new TreeMap<Long, String>();
    private double segmentDurationCache;
    private long segmentLengthCache;
    private double tickRate;

    public TimeMap() {
        quarterDaySegments.put(0L, "sunrise");
        quarterDaySegments.put(1000L, "day");
        quarterDaySegments.put(12000L, "sunset");
        quarterDaySegments.put(12542L, "night");
        quarterDaySegments.put(23000L, "sunrise");
    }

    public long getSegmentStart(long dayTicks) {
        return quarterDaySegments.floorEntry(dayTicks).getKey();
    }

    public double getTickRate(long dayTicks) {
        String timeMode = SyncedConfig.getValue(TimeOption.TIME_MODE);
        double segmentDuration = 1.0D;
        long segmentLength = 20L;

        switch(timeMode) {
            case "QUARTER_DAY":
                String segment = quarterDaySegments.floorEntry(dayTicks).getValue();
                switch(segment) {
                    case "sunrise":
                        segmentDuration = SyncedConfig.getDoubleValue(TimeOption.SUNRISE_QUARTER_LENGTH);
                        segmentLength = 2000L;
                        break;
                    case "day":
                        segmentDuration = SyncedConfig.getDoubleValue(TimeOption.DAY_QUARTER_LENGTH);
                        segmentLength = 11000L;
                        break;
                    case "sunset":
                        segmentDuration = SyncedConfig.getDoubleValue(TimeOption.SUNSET_QUARTER_LENGTH);
                        segmentLength = 542L;
                        break;
                    case "night":
                        segmentDuration = SyncedConfig.getDoubleValue(TimeOption.NIGHT_QUARTER_LENGTH);
                        segmentLength = 10458L;
                        break;
                }
                break;
            case "HALF_DAY":
                if (dayTicks < 12000L) {
                    segmentDuration = SyncedConfig.getDoubleValue(TimeOption.DAY_HALF_LENGTH);
                }
                else {
                    segmentDuration = SyncedConfig.getDoubleValue(TimeOption.NIGHT_HALF_LENGTH);
                }
                segmentLength = 12000L;
                break;
            case "FULL_DAY":
                segmentDuration = SyncedConfig.getDoubleValue(TimeOption.DAY_FULL_LENGTH);
                segmentLength = 24000L;
                break;
        }

        if (segmentDurationCache == segmentDuration && segmentLengthCache == segmentLength) {
            return tickRate;
        }
        else {
            tickRate = BigDecimal.valueOf(segmentLength / segmentDuration).setScale(2, RoundingMode.HALF_UP).doubleValue();
            segmentDurationCache = segmentDuration;
            segmentLengthCache = segmentLength;
        }

        return tickRate;
    }

}

