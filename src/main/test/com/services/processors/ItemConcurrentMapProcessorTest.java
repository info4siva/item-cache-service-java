package com.services.processors;

import com.services.ItemCache;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemConcurrentMapProcessorTest {
    private ItemCache itemCache = null;

    @setup
    public void setup() {
        itemCache
    }
    @Test
    public void initializeCacheParameters() {
        itemCache = new ItemConcurrentMapProcessor();
        assertTrue(itemCache.initializeCacheParameters(3, 2, 2));
    }

    @Test
    public void addItems() {
    }

    @Test
    public void fetchItems() {
    }
}