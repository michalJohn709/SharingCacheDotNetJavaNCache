package org.example;

import com.alachisoft.ncache.client.CacheDataModificationListener;
import com.alachisoft.ncache.client.CacheEventArg;

public class CacheDataModificationImpl implements CacheDataModificationListener {
    @Override
    public void onCacheDataModified(String s, CacheEventArg cacheEventArg) {
        System.out.println("Cache event Type: " + cacheEventArg.getEventType());
        System.out.println("Cache event received for the key: " + s);
    }

    @Override
    public void onCacheCleared(String s) {

    }
}
