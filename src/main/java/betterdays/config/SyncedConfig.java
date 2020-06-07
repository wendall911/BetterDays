package betterdays.config;

import betterdays.config.ISyncedOption;

import java.util.Map;
import java.util.HashMap;

public class SyncedConfig {

    private static Map<String, SyncedConfigOption> options = new HashMap<>();

    public static void addOption(ISyncedOption option, String defaultValue) {
        options.put(option.getName(), new SyncedConfigOption(defaultValue));
    }

    public static SyncedConfigOption getEntry(String key) {
        return options.get(key);
    }

    public static boolean getBooleanValue(ISyncedOption option) {
        return Boolean.valueOf(getValue(option));
    }

    public static int getIntValue(ISyncedOption option) {
        return Integer.valueOf(getValue(option));
    }

    public static double getDoubleValue(ISyncedOption option) {
        return Double.valueOf(getValue(option));
    }

    public static String getValue(ISyncedOption option) {
        return options.get(option.getName()).value;
    }

    public static class SyncedConfigOption {
        public String value;
        public final String defaultValue;

        public SyncedConfigOption(String defaultValue) {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public String getValue() {
            return this.value;
        }
    }

}

