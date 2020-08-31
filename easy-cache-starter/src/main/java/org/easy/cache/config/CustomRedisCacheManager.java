package org.easy.cache.config;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * 订制缓存管理，增强 redis 缓存配置
 * 1. 解决缓存预热(暂未解决)
 * 2. 解决缓存有效时间
 * 3. 解决缓存刷新问题
 * 4. 增加空值缓存配置存储时间,不能存储大量的空键,需要清理
 */
public class CustomRedisCacheManager extends RedisCacheManager  {

	// 空键存储时间
	private Duration emptyKeyExpire;
	private RedisCacheWriter cacheWriter;
	private  RedisCacheConfiguration defaultCacheConfiguration;
	private RedisTemplate redisTemplate;

	public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,RedisTemplate redisTemplate) {
		super(cacheWriter, defaultCacheConfiguration);
		this.cacheWriter = cacheWriter;
		this.defaultCacheConfiguration = defaultCacheConfiguration;
		this.redisTemplate=redisTemplate;
	}


	@Override
	public Cache getCache(String cacheName) {
		CustomRedisCache customRedisCache = new CustomRedisCache(cacheName,cacheWriter,defaultCacheConfiguration, redisTemplate);
		customRedisCache.setEmptyKeyExpire(this.emptyKeyExpire);
		return customRedisCache;
	}

	public void setEmptyKeyExpire(Duration emptyKeyExpire) {
		this.emptyKeyExpire = emptyKeyExpire;
	}


}
