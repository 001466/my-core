package org.easy.cache.config;

import com.deocean.common.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class MybatisRedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;

    //存活时间：2天
    private Long entryTtl = 1000L*60*60*24*2;

    //空值存活时间：30分钟
    private Long emptyTtl = 20L;

    private static ExecutorService executor = new ThreadPoolExecutor(2, 4,
            2000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     *
     * @param key
     * @param value
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value) {

        RedisTemplate redisTemplate = getRedisTemplate();
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value);

        boolean isEmpty=false;

        if (value == null) {
            isEmpty=true;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            if (CollectionUtils.isEmpty(collection)) {
                isEmpty=true;
            }
        } else if (value instanceof Map) {
            Map map = (Map) value;
            if (map.isEmpty()) {
                isEmpty=true;
            }
        } else if (value.getClass().isArray()) {
            if (ArrayUtils.isEmpty((Object[]) value)) {
                isEmpty=true;
            }
        } else if (value instanceof String) {
            if (org.springframework.util.StringUtils.isEmpty((CharSequence) value)) {
                isEmpty=true;
            }
        }

        if(isEmpty){
            refresh(key, emptyTtl);
        }else{
            refresh(key,entryTtl);
        }

//        log.info("Put query result to redis");
//        System.err.println("Put query result to redis");
    }

    /**
     * Get cached query result from redis
     *
     * @param key
     * @return
     */
    @Override
    public Object getObject(Object key) {
        RedisTemplate redisTemplate = getRedisTemplate();
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Object value=opsForValue.get(key);

        executor.submit(new Runnable() {
            @Override
            public void run() {
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
                //当值不为空时，续约绶存时间
                refresh(key, entryTtl);
            }
        });

//        log.info("Get cached query result from redis");
//        System.err.println("Get cached query result from redis");
        return value;

    }

    /**
     * Remove cached query result from redis
     *
     * @param key
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        RedisTemplate redisTemplate = getRedisTemplate();
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Object value=opsForValue.get(key);
        redisTemplate.delete(key);
//        log.info("Remove cached query result from redis");
//        System.err.println("Remove cached query result from redis");
        return value;
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushDb();
            return null;
        });
//        log.info("Clear all the cached query result from redis");
//        System.err.println("Clear all the cached query result from redis");
    }

    @Override
    public int getSize() {
        RedisTemplate redisTemplate = getRedisTemplate();
        return Integer.parseInt(String.valueOf(redisTemplate.execute((RedisCallback) connection -> {
            return connection.dbSize();
         })));
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private RedisTemplate getRedisTemplate() {
            return SpringUtil.getBean("customRedisTemplate",CustomRedisTemplate.class);
    }

    private  void refresh(Object cacheKey,long ttl) {
        Long expire = getRedisTemplate().getExpire(cacheKey);
        if (expire != -2) {
             getRedisTemplate().expire(cacheKey, ttl, TimeUnit.MILLISECONDS);
        }
    }
}

