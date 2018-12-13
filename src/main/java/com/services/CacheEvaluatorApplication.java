package com.services;

import com.services.beans.ItemInfo;
import com.services.constants.ItemCacheConstants;
import com.services.processors.ItemConcurrentMapProcessor;
import com.services.processors.ItemGuavaCacheProcessor;
import com.services.utils.ServiceUtils;
import java.util.List;
import java.util.Map;

/**
 * This main application class compares Guava and ConcurrentHashMap cache implementation
 * Test was performed for 5 iterations / add 100 items with the interval of 500ms between each execution
 *
 *** GUAVA CACHE IMPLEMENTATION ***
 Number of Items in Cache :100
 Number of Items in Cache :200
 Number of Items in Cache :300
 Number of Items in Cache :400
 Number of Items in Cache :418
 addItemsAvgDuration=3 MILLISECONDS
 fetchItemsAvgDuration=15 MILLISECONDS
 Number of Items in Cache :338
 Number of Items in Cache :253
 Number of Items in Cache :176
 Number of Items in Cache :100
 Number of Items in Cache :100
 fetchItemsAvgDuration=1 MILLISECONDS


 *** CONCURRENT HASHMAP CACHE IMPLEMENTATION ***
 Number of Items in Cache :100
 Number of Items in Cache :200
 Number of Items in Cache :300
 Number of Items in Cache :400
 Number of Items in Cache :418
 addItemsAvgDuration=0 MILLISECONDS
 fetchItemsAvgDuration=0 MILLISECONDS
 Number of Items in Cache :341
 Number of Items in Cache :261
 Number of Items in Cache :186
 Number of Items in Cache :100
 Number of Items in Cache :100
 fetchItemsAvgDuration=0 MILLISECONDS

 * @author info4siva
 **/
public class CacheEvaluatorApplication {
    private static final long NO_OF_RECORDS_FOR_EVALUATION = 100L;
    private static final int REPEAT_EVALUATION = 5;

    public static void main(String args[]) {
        ItemCache itemCacheService = new ItemGuavaCacheProcessor();
        System.out.println("\n*** GUAVA CACHE IMPLEMENTATION ***");
        evaluateCacheStrategyInContext(itemCacheService);
        System.out.println("\n\n*** CONCURRENT HASHMAP CACHE IMPLEMENTATION ***");
        itemCacheService = new ItemConcurrentMapProcessor();
        evaluateCacheStrategyInContext(itemCacheService);
    }

    private static void evaluateCacheStrategyInContext (ItemCache itemCacheService) {
        try {
            itemCacheService.initializeCacheParameters
                    (ItemCacheConstants.MAX_RECORDS_IN_CACHE, ItemCacheConstants.MIN_RECORDS_IN_CACHE, ItemCacheConstants.TIMEOUT_IN_MILLIS);
            int count = REPEAT_EVALUATION;
            long startTime = 0l;
            long finishTime = 0l;
            long addOperationAvgTime = 0l;
            long fetchOperationAvgTime = 0l;

            while (count > 0) {
                List<ItemInfo> itemsGenerated = ServiceUtils.generateItems(NO_OF_RECORDS_FOR_EVALUATION);
                //2.Add generated items to the cache
                startTime = System.currentTimeMillis();
                itemCacheService.addItems(itemsGenerated);
                finishTime = System.currentTimeMillis();
                addOperationAvgTime = addOperationAvgTime + (finishTime -startTime);
                //2.Fetch the items in Cache after addition
                startTime = System.currentTimeMillis();
                List<Map<String, ItemInfo>> numberOfItems = itemCacheService.fetchItems();
                finishTime = System.currentTimeMillis();

                if(null != numberOfItems) {
                    System.out.println("Number of Items in Cache :" + numberOfItems.size());
                }
                fetchOperationAvgTime = fetchOperationAvgTime + (finishTime -startTime);
                Thread.sleep(500);
                count--;
            }
            System.out.println("addItemsAvgDuration=" + (addOperationAvgTime/REPEAT_EVALUATION) + " MILLISECONDS");
            System.out.println("fetchItemsAvgDuration=" + (fetchOperationAvgTime/REPEAT_EVALUATION) + " MILLISECONDS");
            //3.Fetch the items in Cache and see whether it satisfies minimum record rule
            count = REPEAT_EVALUATION;
            fetchOperationAvgTime = 0l;
            while (count > 0) {
                startTime = System.currentTimeMillis();
                List<Map<String, ItemInfo>> numberOfItems = itemCacheService.fetchItems();
                finishTime = System.currentTimeMillis();
                if (null != numberOfItems) {
                    System.out.println("Number of Items in Cache :" + numberOfItems.size());
                }
                fetchOperationAvgTime = fetchOperationAvgTime + (finishTime - startTime);
                Thread.sleep(500);
                count--;
            }
            System.out.println("fetchItemsAvgDuration=" + (fetchOperationAvgTime/REPEAT_EVALUATION) + " MILLISECONDS");
        } catch (InterruptedException interruptedExec) {
            //DO NOTHING
        }
    }
}
