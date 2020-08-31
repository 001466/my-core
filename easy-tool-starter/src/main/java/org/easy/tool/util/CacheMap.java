package org.easy.tool.util;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMap extends AbstractMap<String, CacheMapEntry<String,Object>>  implements   Serializable {

    private Map<String, CacheMapEntry<String,Object>> map = new ConcurrentHashMap<>();

    @Override
    public Set<Entry<String, CacheMapEntry<String, Object>>> entrySet() {
        return map.entrySet();
    }

    public Object put(String key, Object value) {
        CacheMapEntry entry= new CacheMapEntry(key,value);
        map.put(key,entry);
        return value;
    }

    Object get(String key){
        CacheMapEntry entry= map.get(key);
        return entry==null?null:entry .getValue();
    }

    public Object remove(String key) {
        CacheMapEntry entry= map.remove(key);
        return entry==null?null:entry .getValue();
    }

    @Override
    public boolean containsValue(Object value){
        Iterator<Entry<String, CacheMapEntry<String, Object>>> i = entrySet().iterator();
        if (value==null) {
            while (i.hasNext()) {
                Entry<String, CacheMapEntry<String, Object>> e = i.next();
                if (e==null || e.getValue()==null) {
                    return true;
                }
            }
        } else {
            while (i.hasNext()) {
                Entry<String, CacheMapEntry<String, Object>> e = i.next();
                if(e==null){
                    continue;
                }
                if (value.equals(e.getValue().getValue())) {
                    return true;
                }
            }
        }
        return false;
    }







    private static final long DEFAULT_TIMEOUT = 1000*360;
    private static CacheMap defaultInstance;
    public static synchronized final CacheMap getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new CacheMap(DEFAULT_TIMEOUT);
        }
        return defaultInstance;
    }

    private long cacheTimeout;
    public long getCacheTimeout(){
        return cacheTimeout;
    }
    private CacheMap(long timeout) {
        this.cacheTimeout = timeout;
        new ClearThread().start();
    }
    private CacheMap() {
        this.cacheTimeout = DEFAULT_TIMEOUT;
        new ClearThread().start();
    }

    private class ClearThread extends Thread {
        ClearThread() {
            setName("clear cache thread");
        }
        @Override
        public void run() {
            while (true) {
                try {
                    long now = System.currentTimeMillis();
                    Object[] keys = map.keySet().toArray();
                    for (Object key : keys) {
                        CacheMapEntry entry = map.get(key);
                        if (now - entry.time >= cacheTimeout) {
                            map.remove(key);
                        }
                    }
                    Thread.sleep(cacheTimeout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


