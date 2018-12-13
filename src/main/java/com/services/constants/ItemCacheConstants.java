package com.services.constants;

/**
 * Constants to support ItemCache implementation
 * @author info4siva
 **/
public final class ItemCacheConstants {
    public static final int SERVER_CONFIG_PORT = 8080;
    public static final String SERVER_CONFIG_PATH_SPEC = "/api/*";
    public static final int MIN_RECORDS_IN_CACHE = 3;
    public static final long TIMEOUT_IN_MILLIS = 2000L;
    public static final int GUAVA_CONCURRENCY_LEVEL = 16;
    public static final int MAX_RECORDS_IN_CACHE = 10000;
    public static final String ITEM = "item";
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
}
