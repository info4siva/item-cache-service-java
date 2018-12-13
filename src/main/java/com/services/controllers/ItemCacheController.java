package com.services.controllers;

import com.services.ItemCache;
import com.services.beans.ItemInfo;
import com.services.beans.ItemInfoRequestBean;
import com.services.beans.Status;
import com.services.constants.ItemCacheConstants;
import com.services.processors.ItemConcurrentMapProcessor;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Controller which helps to transform JSON request to the appropriate processor for caching
 * @author info4siva
 **/
@Path("/items")
public class ItemCacheController {
    private static final ItemCache itemCache = new ItemConcurrentMapProcessor();
    private static final boolean cacheInitialized;

    static {
        cacheInitialized = itemCache.initializeCacheParameters(ItemCacheConstants.MAX_RECORDS_IN_CACHE,
                ItemCacheConstants.MIN_RECORDS_IN_CACHE, ItemCacheConstants.TIMEOUT_IN_MILLIS);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, ItemInfo>> fetchItems() {
        if(cacheInitialized) {
            return itemCache.fetchItems();
        }
        return null;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Status addItems(@Valid ItemInfoRequestBean items) {
        Status status = null;

        if(cacheInitialized) {
            if (null != items) {
                status = itemCache.addItems(items.getItems());
            } else {
                System.out.println("EMPTY INPUT !!!");
            }
        }
        return status;
    }
}
