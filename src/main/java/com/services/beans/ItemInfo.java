package com.services.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Bean which holds Item value from JSON request
 * @author info4siva
 **/
@JsonIgnoreProperties({"lastModifiedTime"})
public class ItemInfo implements Comparable<ItemInfo> {
    private int id;
    private String timestamp;
    private long lastModifiedTime;

    public ItemInfo() {
    }
    public ItemInfo(int pId, String pTimestamp) {
        id = pId;
        timestamp = pTimestamp;
        lastModifiedTime = System.currentTimeMillis();
    }
    public ItemInfo (int pId, String pTimestamp, long pLastModifiedTime) {
        id = pId;
        timestamp = pTimestamp;
        lastModifiedTime = pLastModifiedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }

    @Override
    public int compareTo(ItemInfo other){
        Long result = other.lastModifiedTime - this.lastModifiedTime;
        return result.intValue();
    }
}
