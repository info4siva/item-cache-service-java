package com.services;

import com.services.beans.ItemInfo;
import com.services.beans.Status;
import java.util.List;
import java.util.Map;

/**
 * Interface to list allowed actions on ItemCache
 * @author info4siva
 **/
public interface ItemCache {
    /**
     * This method initializes the cache
     **/
    boolean initializeCacheParameters(final int pInitialCapacity,
                                      final int minRecordsInCache, final long recordTimeoutInMillis);
    /**
     * This method adds the given list of items to cache after input validation
     * Operation result will be returned in the form of Status object
     * @param items
     * @return Status
     */
    Status addItems(final List<ItemInfo> items);
    /**
     * This method fetches the list of items added to cache in the last 2 seconds
     * or the list of last 100 POSTed items, whichever greater
     * @return Map<String, ItemInfo>
     */
    List<Map<String, ItemInfo>> fetchItems();
        /**
     * This method clears the cache and shutdown executor. It is mainly used in repeated test cases execution
     **/
    boolean clearCacheAndShutdownMaintenanceExecutor ();
}
