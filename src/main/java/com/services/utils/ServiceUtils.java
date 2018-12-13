package com.services.utils;

import com.services.beans.ItemInfo;
import com.services.constants.ItemCacheConstants;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Holds generic service utils to support ItemCache
 * @author info4siva
 **/
public class ServiceUtils {

    public static Iterable<Integer> toIterable(Iterator<Integer> iterator) {
        return () -> iterator;
    }
    /**
     * Test utility method to compute random payload for validating the logic
     * @return List<ItemInfo>
     */
    public static List<ItemInfo> generateItems (Long numberOfItems) {
        List<ItemInfo> result = new ArrayList<>();
        Random random = new Random();
        SimpleDateFormat inputSimpleFormat
                = new SimpleDateFormat(ItemCacheConstants.INPUT_DATE_FORMAT);
        //create dummy data based upon requested input
        while (numberOfItems > 0) {
            ItemInfo item = new ItemInfo(random.nextInt(Integer.MAX_VALUE),
                    inputSimpleFormat.format(Calendar.getInstance().getTime()), System.currentTimeMillis());
            result.add(item);
            numberOfItems--;
        }
        return result;
    }
}
