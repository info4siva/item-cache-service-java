package com.services.beans;

import java.io.Serializable;
import java.util.List;
/**
 * Holder bean for capturing array of Items
 * @author info4siva
 **/
public class ItemInfoRequestBean implements Serializable {
    private List<ItemInfo> items;

    public List<ItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ItemInfo> items) {
        this.items = items;
    }
}
