package org.leeu1911;

public abstract class JsonBox {
    public static final String API_VERSION = "0.0.1";
    public static final String LIVE_API_BASE = "https://jsonbox.io";

    public static volatile String boxId;
    private static volatile String apiBase = LIVE_API_BASE;
}
