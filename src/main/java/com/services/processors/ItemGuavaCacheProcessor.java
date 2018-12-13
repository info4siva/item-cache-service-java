package com.services.processors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.services.ItemCache;
import com.services.beans.ItemInfo;
import com.services.beans.Status;
import com.services.constants.ItemCacheConstants;
import com.services.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * ######## NOT PREFERRED APPROACH #########
 * This class uses Google Guava for Cache Implementation. Guava Cache is currently cleaned up only when the records were retrieved
 * and currently it doesn't use explicit scheduler to cleanup the records. This approach is not preferred since Guava Cache doesn't
 *  maintain insertion order and lower performance compared to ConcurrentHashMap => both of them with concurrencyLevel=16
 *
 * @author info4siva
 **/
public class ItemGuavaCacheProcessor implements ItemCache {
    private static final Logger logger = LoggerFactory.getLogger(ItemGuavaCacheProcessor.class);

    private static int initialCapacity = ItemCacheConstants.MAX_RECORDS_IN_CACHE;
    private static int minRecordsInCache = ItemCacheConstants.MIN_RECORDS_IN_CACHE;
    private static long recordTimeoutInMillis = ItemCacheConstants.TIMEOUT_IN_MILLIS;
    private static ItemCacheLoader customItemCacheLoader = null;
    private static LoadingCache<Integer, ItemInfo> itemCache = null;
    /**
     * Custom cache-loader to clean up the  cache upon retrieval of items
     */
    private static class ItemCacheLoader extends CacheLoader<Integer, ItemInfo> {
        @Override
        public ItemInfo load(Integer key) throws ExecutionException {
            return itemCache.get(key);
        }
        @Override
        public Map<Integer, ItemInfo> loadAll(Iterable<? extends Integer> keys) throws ExecutionException {
            int counter = 1;
            int keyInCache;
            Iterator keysIterator;
            Map<Integer, ItemInfo> filteredResultMap = null;
            //using LinkedHashMap to maintain order
            if(null != keys) {
                filteredResultMap = new LinkedHashMap<>();
                keysIterator = keys.iterator();

                if(null != keysIterator) {
                    long currentTime = System.currentTimeMillis();
                    //Iterate through the keys and filter the records
                    while (keysIterator.hasNext()) {
                        keyInCache = (Integer) keysIterator.next();
                        ItemInfo itemInfo = load(keyInCache);

                        if (counter <= minRecordsInCache
                                || ((currentTime - itemInfo.getLastModifiedTime()) <= recordTimeoutInMillis)) {
                            filteredResultMap.put(keyInCache, itemInfo);
                        } else {
                            itemCache.invalidate(keyInCache);
                        }
                        counter++;
                    }
                }
            }
            return filteredResultMap;
        }
    }
    /**
     * This method initializes the cache
     **/
    public boolean initializeCacheParameters(final int pInitialCapacity,
                                             final int pMinRecordsInCache, final long pRecordTimeoutInMillis) {
        if(null == itemCache) {
            initialCapacity = pInitialCapacity;
            minRecordsInCache = pMinRecordsInCache;
            recordTimeoutInMillis = pRecordTimeoutInMillis;
            //1.Build LoadingCache with custom initial cache-loader (it is not required for item update through REST service)
            customItemCacheLoader = new ItemCacheLoader();
            itemCache =
                CacheBuilder.newBuilder()
                        .maximumSize(initialCapacity)
                        .concurrencyLevel(ItemCacheConstants.GUAVA_CONCURRENCY_LEVEL)
                        .build(customItemCacheLoader);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * This utility method adds the given list of items to cache after validation
     * Operation result will be returned in the form of Status object
     * @param items
     * @return Status
     */
    public Status addItems(final List<ItemInfo> items) {
        //add items to the cache. This method is invoked from Controller
        if(null != items) {
            for(ItemInfo itemInContext: items) {
                itemCache.put(itemInContext.getId(),itemInContext);
            }
        }
        return new Status(Status.ResponseCodes.SUCCESS);
    }
    /**
     * This utility method fetches the list of items list of items added to cache in the last 2 seconds
     * or the list of last 100 POSTed items, whichever greater
     * @return List<ItemInfo>
     */
    public List<Map<String, ItemInfo>> fetchItems() {
        List<Map<String, ItemInfo>> output= null;
        List<ItemInfo> items;
        Hashtable<String, ItemInfo> itemInMap;

        if (null != itemCache && !itemCache.asMap().entrySet().isEmpty()) {
            Set<Integer> currentKeySet = itemCache.asMap().keySet();
            Map<Integer, ItemInfo> resultMap = new LinkedHashMap<>();
            items = new ArrayList<>();

            try {
                resultMap = customItemCacheLoader.loadAll(ServiceUtils.toIterable(currentKeySet.iterator()));
            } catch (ExecutionException executionExec) {
                logger.error("Error while fetching items from Cache", executionExec);
            }
            items.addAll(resultMap.values());

            for(ItemInfo itemInfo : items) {
                itemInMap = new Hashtable<>();
                itemInMap.put(ItemCacheConstants.ITEM, itemInfo);
                output.add(itemInMap);
            }
        }
        return output;
    }
}
