package org.leeu1911;

import java.util.regex.Pattern;

public abstract class JsonBox {
    public static final String API_VERSION = "0.0.1";
    public static final String LIVE_API_BASE = "https://jsonbox.io";

    public static volatile String boxId;
    private static volatile String apiBase = LIVE_API_BASE;
    private static final String filterRegex = "^\\w+:((( )*\\w+(%|\\s)?\\w+)|(\\*?\\w+\\*?)|((=|>=|>|<|<=)\\d+))$";
    private static Pattern filterPattern = Pattern.compile(filterRegex);

    /**
     * (FOR TESTING ONLY) If you'd like your API requests to hit your own (mocked) server, you can set
     * this up here by overriding the base api URL.
     */
    public static void overrideApiBase(final String overriddenApiBase) {
        apiBase = overriddenApiBase;
    }

    public static String getApiBase() {
        return apiBase;
    }
    public static Pattern getFilterPattern() { return filterPattern; }

    public static void overrideFilterPattern(Pattern filterPattern) { JsonBox.filterPattern = filterPattern; }
}
