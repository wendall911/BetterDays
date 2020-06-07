package betterdays.config;

public enum TimeOption implements ISyncedOption {

    TIME_MODE("timeMode"),
    DAY_FULL_LENGTH("dayFullLength"),
    DAY_HALF_LENGTH("dayHalfLength"),
    NIGHT_HALF_LENGTH("nightHalfLength"),
    SUNRISE_QUARTER_LENGTH("sunriseQuarterLength"),
    DAY_QUARTER_LENGTH("dayQuarterLength"),
    SUNSET_QUARTER_LENGTH("sunsetQuarterLength"),
    NIGHT_QUARTER_LENGTH("nightQuarterLength");

    private final String name;

    TimeOption(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

}

