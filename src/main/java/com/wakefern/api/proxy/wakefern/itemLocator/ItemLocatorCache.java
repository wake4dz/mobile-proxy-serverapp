package com.wakefern.api.proxy.wakefern.itemLocator;

import com.wakefern.global.VcapProcessor;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Item Locator In Memory Cache via Cache2K as a singleton.
 */
public class ItemLocatorCache {
    private Cache<String, ItemLocatorDto> mCache;

    private static final long CACHE_CAPACITY = 10000;

    private boolean mEnabled;

    private static ItemLocatorCache sInstance = null;

    /**
     * Get the singleton instance of the ItemLocatorCache.
     * @return
     */
    public static ItemLocatorCache getInstance() {
        if (sInstance == null) {
            sInstance = new ItemLocatorCache();
        }
        return sInstance;
    }

    private ItemLocatorCache() {
        mEnabled = VcapProcessor.isItemLocatorCacheEnabled();
        if (mEnabled) {
            mCache = Cache2kBuilder.of(String.class, ItemLocatorDto.class)
                    .name("itemLocations")
                    .entryCapacity(CACHE_CAPACITY)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .build();
        }
    }

    /**
     * Serialize cache key.
     * @param storeId
     * @param upc
     * @return
     */
    private static String createKey(String storeId, String upc) {
        return storeId + ":" +upc;
    }

    /**
     * Fetch an item location object from the cache. Returns null if object not in cache.
     * @param storeId
     * @param sku
     * @return
     */
    public ItemLocatorDto getItemLocation(String storeId, String upc) {
        if (!mEnabled) return null;
        return mCache.get(createKey(storeId, upc));
    }

    /**
     * Add an item location dto to the cache.
     * @param storeId
     * @param upc
     * @param itemLocator
     */
    public void putItemLocation(String storeId, String upc, ItemLocatorDto itemLocator) {
        if (!mEnabled) return;
        mCache.put(createKey(storeId, upc), itemLocator);
    }
}
