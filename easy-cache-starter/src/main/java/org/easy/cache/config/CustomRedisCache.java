package org.easy.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomRedisCache extends RedisCache {
    private RedisTemplate redisTemplate;
    private Duration emptyKeyExpire;
    private static ExecutorService executor = new ThreadPoolExecutor(2, 4,
            2000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public CustomRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, RedisTemplate redisTemplate) {
        super(name, cacheWriter, cacheConfig);
        this.redisTemplate = redisTemplate;
    }

    public void setEmptyKeyExpire(Duration emptyKeyExpire) {
        this.emptyKeyExpire = emptyKeyExpire;
    }

    @Override
    public ValueWrapper get(Object key) {
        //System.err.println("get key=" + String.valueOf(key));
        ValueWrapper valueWrapper = super.get(key);
        if (getCacheConfiguration().getTtl().isZero()) {
            return valueWrapper;
        }
        if (valueWrapper == null) {
            return valueWrapper;
        }

        executor.submit(new Runnable() {
            @Override
            public void run() {
                Object value = valueWrapper.get();
                if (value == null) {
                    return;
                } else if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    if (CollectionUtils.isEmpty(collection)) {
                        return;
                    }
                } else if (value instanceof Map) {
                    Map map = (Map) value;
                    if (map.isEmpty()) {
                        return;
                    }
                } else if (value.getClass().isArray()) {
                    if (ArrayUtils.isEmpty((Object[]) value)) {
                        return;
                    }
                } else if (value instanceof String) {
                    if (org.springframework.util.StringUtils.isEmpty((CharSequence) value)) {
                        return;
                    }
                }
                refresh(key, getCacheConfiguration().getTtl());
            }
        });


        return valueWrapper;
    }


    @Override
    public void put(Object key, Object value) {
        //System.err.println("put key=" + String.valueOf(key) + ",value=" + value);
        if (emptyKeyExpire == null || emptyKeyExpire.isZero()) {
            return;
        }

        super.put(key, value);

//        executor.submit(new Runnable() {
//            @Override
//            public void run() {

                if (value == null) {
                    refresh(key, emptyKeyExpire);
                } else if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    if (CollectionUtils.isEmpty(collection)) {
                        refresh(key, emptyKeyExpire);
                    }
                } else if (value instanceof Map) {
                    Map map = (Map) value;
                    if (map.isEmpty()) {
                        refresh(key, emptyKeyExpire);
                    }
                } else if (value.getClass().isArray()) {
                    if (ArrayUtils.isEmpty((Object[]) value)) {
                        refresh(key, emptyKeyExpire);
                    }
                } else if (value instanceof String) {
                    if (org.springframework.util.StringUtils.isEmpty((CharSequence) value)) {
                        refresh(key, emptyKeyExpire);
                    }
                }

//            }
//        });


    }

    private  void refresh(Object key, Duration duration) {
        String cacheKey = createCacheKey(key);
        Long expire = redisTemplate.getExpire(cacheKey);
        if (expire != -2) {
            long ttl = duration.toMillis();
            redisTemplate.expire(cacheKey, ttl, TimeUnit.MILLISECONDS);
            //System.err.println("expire cache key=" + String.valueOf(cacheKey) + ",expire="+expire+ ",ttl="+ttl);
        }
    }


}
