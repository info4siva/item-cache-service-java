# item-cache-service-java - open source (open-source license information to be added)
This implementation uses ConcurrentHashMap with additional callable task to clear cache entries which doesn't satisfy the criteria

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
 fetchItemsAvgDuration=0 MILLISECONDS (in NANO SECONDS)
