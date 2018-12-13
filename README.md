# item-cache-service-java - Guava vs ConcurrentHashMap comparison
My main implementation uses ConcurrentHashMap with additional callable task to clear cache entries which doesn't satisfy the criteria provided in the problem-statement. Intially i have thought about implementing solution using 4 different data structures (Guava Cache, ConcurrentHashMap, LinkedBlockingQueue, DelayeQue) and comparing the performance between each of them. However i have elimated Q approach to pickup latest from Cache.

Guava Cache doesn't maintain insertion order and only average performance compared to ConcurrentHashMap solution for add and fetch operations. Hence i preferred ConcurrentHashMap over other Collection framework.

Please do feel free to drop your feedback to info4siva@gmail.com in case there are any discrepancies or issues !

## Problem Statement
Develop a RESTful web service that implement the following 2 APIs in Kotlin, Java or Clojure

HTTP POST /items , request body { item:{ id: 123, timestamp: 2016-01-01T23:01:01.000Z } } should return 201 created

HTTP GET /items should return the list of items POSTed in the last 2 seconds or the list of last 100 POSTed items, whichever greater. [ {item: {id: 123, timestamp: 2016-01-01T23:01:01.000Z} }, {item: {id: 124, timestamp: 2016-01-01T23:01:01.001Z} },…]

Notes:

1.The implementation should maximize throughput and minimize latency

2.The implementation should not rely on an external Database, using heap memory is ok

3.The implementation should run on the JVM and minimize heap footprint

## Solution - JAVA Frameworks / Tools used for Caching Implementation
Gradle

Jersey Container

Jetty Web Server

Jackson for parsing JSON

Experiment caching with Google Guava Cache libraries

Actual Implementation using ConcurrentHashMap

JUNIT4 (JUNIT5 - Not this time)

Rest Assured Framework for API Testing (TODO)

## Comparison Table

Test was performed for 5 iterations / add 100 items with the interval of 500ms between each execution

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
 addItemsAvgDuration=0 MILLISECONDS (in nano-secs)
 fetchItemsAvgDuration=0 MILLISECONDS
 Number of Items in Cache :341
 Number of Items in Cache :261
 Number of Items in Cache :186
 Number of Items in Cache :100
 Number of Items in Cache :100
 fetchItemsAvgDuration=0 MILLISECONDS

