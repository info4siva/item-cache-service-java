# item-cache-service-java - open source (open-source license information to be added)
This implementation uses ConcurrentHashMap with additional callable task to clear cache entries which doesn't satisfy the criteria

Develop a RESTful web service that implement the following 2 APIs in Kotlin, Java or Clojure

HTTP POST /items , request body { item:{ id: 123, timestamp: 2016-01-01T23:01:01.000Z } } should return 201 created

HTTP GET /items should return the list of items POSTed in the last 2 seconds or the list of last 100 POSTed items, whichever greater. [ {item: {id: 123, timestamp: 2016-01-01T23:01:01.000Z} }, {item: {id: 124, timestamp: 2016-01-01T23:01:01.001Z} },…]

Notes:

1.The implementation should maximize throughput and minimize latency

2.The implementation should not rely on an external Database, using heap memory is ok

3.The implementation should run on the JVM and minimize heap footprint