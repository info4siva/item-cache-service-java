package com.services.processors;

import com.services.ItemCache;
import com.services.beans.ItemInfo;
import com.services.beans.Status;
import com.services.constants.ItemCacheConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
/**
 * FINAL SOLUTION :-
 * Implements ItemCache with ConcurrentHashMap and scheduled task to clean up the cache every 1 second
 * @author info4siva
 **/
public class ItemConcurrentMapProcessor implements ItemCache {
    private static final Logger logger = LoggerFactory.getLogger(ItemConcurrentMapProcessor.class);

    private static int initialCapacity = ItemCacheConstants.MAX_RECORDS_IN_CACHE;
    private static int minRecordsInCache = ItemCacheConstants.MIN_RECORDS_IN_CACHE;
    private static long recordTimeoutInMillis = ItemCacheConstants.TIMEOUT_IN_MILLIS;

    private static ConcurrentHashMap<Integer, ItemInfo> itemCache = null;
    private static CacheMaintenanceService maintenanceTask = null;
    /**
     * This static method initializes the cache
     **/
    public boolean initializeCacheParameters(final int pInitialCapacity,
                                             final int pMinRecordsInCache, final long pRecordTimeoutInMillis) {
        if(null == itemCache) {
            initialCapacity = pInitialCapacity;
            minRecordsInCache = pMinRecordsInCache;
            recordTimeoutInMillis = pRecordTimeoutInMillis;
            itemCache = new ConcurrentHashMap(initialCapacity);
            maintenanceTask = new CacheMaintenanceService();
            //2. scheduled-executor to invalidate cache entries based upon business logic
            Executors.newSingleThreadScheduledExecutor().schedule(maintenanceTask, 1, TimeUnit.SECONDS);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * This callable task cleans up cache based on list of items added to cache in the last 2 seconds
     * or the list of last 100 POSTed items, whichever greater
     **/
    private static class CacheMaintenanceService implements Callable<Void> {
        @Override
        public Void call() {

            if (!itemCache.entrySet().isEmpty()) {
                Set<Map.Entry<Integer, ItemInfo>> cachedEntrySet = itemCache.entrySet();
                Iterator<Map.Entry<Integer, ItemInfo>> cachedEntrySetIterator = cachedEntrySet.iterator();
                int counter = 1;
                long currentTime = System.currentTimeMillis();

                while (cachedEntrySetIterator.hasNext()) {
                    Map.Entry<Integer, ItemInfo> nextEntry = cachedEntrySetIterator.next();
                    ItemInfo itemInfo = nextEntry.getValue();

                    if (null != itemInfo
                            && (currentTime - itemInfo.getLastModifiedTime()) > recordTimeoutInMillis
                            && counter > minRecordsInCache) {
                        itemCache.remove(nextEntry.getKey());
                    }
                    counter++;
                }
            }
            return null;
        }
    }
    /**
     * This method adds the given list of items to cache after input validation
     * Operation result will be returned in the form of Status object
     * @param items
     * @return Status
     */
    public Status addItems(final List<ItemInfo> items) {
        //add items to the cache. This method is invoked from Controller
        boolean itemAddedToCache = Boolean.FALSE;
        Status finalStatus = new Status(Status.ResponseCodes.SUCCESS);

        if(null != items) {
            SimpleDateFormat inputSimpleFormat
                    = new SimpleDateFormat(ItemCacheConstants.INPUT_DATE_FORMAT);
            for(ItemInfo itemInContext: items) {
                try {
                    if (null != itemInContext && null != itemInContext.getTimestamp()) {
                        inputSimpleFormat.parse(itemInContext.getTimestamp());
                        itemCache.put(itemInContext.getId(), itemInContext);
                        //in case value not added then tempItemInfo will be NULL
                        itemAddedToCache = Boolean.TRUE;
                    }
                } catch (ParseException parseExec) {
                    logger.error("Error validating timestamp input", parseExec);
                    return new Status(Status.ResponseCodes.USER_INPUT);
                }
            }
        } else {
            return new Status(Status.ResponseCodes.USER_INPUT);
        }
        //in case of error in adding item to cache
        if(!itemAddedToCache)
            finalStatus = new Status(Status.ResponseCodes.SYSTEM_ERROR);

        return finalStatus;
    }
    /**
     * This method fetches the list of items added to cache in the last 2 seconds
     * or the list of last 100 POSTed items, whichever greater
     * @return List<ItemInfo>
     */
    public List<Map<String, ItemInfo>> fetchItems() {
        List<Map<String, ItemInfo>> output= null;
        Hashtable<String, ItemInfo> item;

        if (null != itemCache && null != itemCache.entrySet() && !itemCache.entrySet().isEmpty()) {
            Set<Integer> currentKeysSet = itemCache.keySet();
            output = new ArrayList<>();
            Iterator<Integer> keysIterator = currentKeysSet.iterator();
            int counter = 1;

            if(null != keysIterator) {
                long currentTime = System.currentTimeMillis();

                while (keysIterator.hasNext()) {
                    Integer keyInCache = keysIterator.next();
                    ItemInfo itemInfo = itemCache.get(keyInCache);

                    if (counter <= minRecordsInCache
                            || ((currentTime - itemInfo.getLastModifiedTime()) <= recordTimeoutInMillis)) {
                        item = new Hashtable<>();
                        item.put(ItemCacheConstants.ITEM, itemInfo);
                        output.add(item);
                    }
                    counter++;
                }
            }
        }
        return output;
    }
}
