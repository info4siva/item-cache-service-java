
package processors;

import com.services.ItemCache;
import com.services.beans.ItemInfo;
import com.services.beans.Status;
import com.services.constants.ItemCacheConstants;
import com.services.processors.ItemConcurrentMapProcessor;
import com.services.utils.ServiceUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This class covers UNIT test cases for ItemConcurrentMapProcessor
 * @author info4siva
 **/
public class ItemConcurrentMapProcessorTest {
    private ItemCache itemCache = null;

    @Before
    public void setupTestCase()  {
        itemCache = new ItemConcurrentMapProcessor();
    }
    @After
    public void clearSetup()  {
        if(null != itemCache)
            itemCache.clearCacheAndShutdownMaintenanceExecutor();
    }
    @Test
    public void initializeCacheParameters() {
        assertTrue(itemCache.initializeCacheParameters(3, 2, 2));
    }
    @Test
    public void addItemsOverCapacityAndMinRecords() {
        List<ItemInfo> itemsToAdd = ServiceUtils.generateItems(5L);
        itemCache.initializeCacheParameters(3, 3, 2999999999L);
        Status status = itemCache.addItems(itemsToAdd);
        assertTrue(status.getCode().equals(Status.ResponseCodes.SUCCESS.getCodeValue()));
        List<Map<String, ItemInfo>> addedItems = itemCache.fetchItems();
        assertTrue(compareItems(itemsToAdd, addedItems));
    }
    @Test
    public void addItemsWithInvalidInput() {
        List<ItemInfo> itemsToAdd = ServiceUtils.generateItems(3L);
        itemCache.initializeCacheParameters(3, 3, 2999999999L);
        itemsToAdd.get(0).setTimestamp(Calendar.getInstance().getTime().toString());
        Status status = itemCache.addItems(itemsToAdd);
        assertTrue(status.getCode().equals(Status.ResponseCodes.USER_INPUT.getCodeValue()));
    }
    @Test
    public void fetchItemsCheckForMinRecordsAndExpireAfterTimeout() {
        List<ItemInfo> itemsToAdd = ServiceUtils.generateItems(5L);
        itemCache.initializeCacheParameters(3, 3, 2);
        Status status = itemCache.addItems(itemsToAdd);
        assertTrue(status.getCode().equals(Status.ResponseCodes.SUCCESS.getCodeValue()));
        try {
            //sleep threads for certain duration to invalidate items over min records
            Thread.sleep(5);
        } catch (InterruptedException interruptedExec) {
            //DO NOTHING
        }
        List<Map<String, ItemInfo>> addedItems = itemCache.fetchItems();
        assertFalse(compareItems(itemsToAdd, addedItems));
    }

    @Test
    public void fetchUpdatedItems() {
        List<ItemInfo> itemsToAdd = ServiceUtils.generateItems(3L);
        itemCache.initializeCacheParameters(3, 3, 2);
        itemCache.addItems(itemsToAdd);
        itemsToAdd.get(0).setId(200);
        Status status = itemCache.addItems(itemsToAdd);
        assertTrue(status.getCode().equals(Status.ResponseCodes.SUCCESS.getCodeValue()));
        List<Map<String, ItemInfo>> addedItems = itemCache.fetchItems();
        assertTrue(compareItems(itemsToAdd, addedItems));
    }

    /**
     * This utility method compares itemToAdd versus itemsInCache
     * @param itemsToAdd
     * @param addedItems
     **/
    private boolean compareItems (List<ItemInfo> itemsToAdd, List<Map<String, ItemInfo>> addedItems) {

        if(null != itemsToAdd) {
            Map<Integer, Boolean> verifiedItems = new HashMap<>();
            //1.initialize verify items Map
            for (ItemInfo item: itemsToAdd) {
                Integer itemId = item.getId();
                verifiedItems.put(itemId, Boolean.FALSE);
            }

            if(null != addedItems) {
                for (Map<String, ItemInfo> itemInCache : addedItems) {
                    ItemInfo itemInfoInCache = itemInCache.get(ItemCacheConstants.ITEM);
                    //verify items in cache versus itemsToAdd
                    for (ItemInfo item : itemsToAdd) {
                        int itemId = item.getId();

                        if (null != itemInfoInCache && itemInfoInCache.getId() == itemId) {
                            verifiedItems.put(itemId, Boolean.TRUE);
                            break;
                        }
                    }
                }
            }

            for(Boolean verified: verifiedItems.values()) {
                //if some of them are not verified return false
                if(!verified) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }
}
