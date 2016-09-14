package com.wakefern.Caching;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Created by brandyn.brosemer on 9/14/16.
 */
public class WakefernCacheControl {
    private static CacheManager cacheManager = CacheManager.getInstance();
    private static Cache cache = null;

    private static Cache getLocalCache(String cacheName){
        cache = cacheManager.getCache(cacheName);
        return cache;
    }

    public static void cacheRequest(String requestURL,String requestResponse){
        cache = getLocalCache("App");
        cache.put(new Element(requestURL,requestResponse));
    }

    public static String getCachedRequest(String requestURL){
        cache = getLocalCache("App");
        if(cache.isKeyInCache(requestURL)){
            Element ele = cache.get(requestURL);
            return ele.getObjectValue().toString();
        }
        return null;
    }
}
