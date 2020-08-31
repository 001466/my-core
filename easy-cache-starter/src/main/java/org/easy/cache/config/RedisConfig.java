package org.easy.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.cache.duration-of-days:3}")
	private Long entryTtl = null;
	@Value("${spring.cache.empty-key-duration-of-seconds:120}")
	private Long emptyKeyExpire = null;


	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

	@Override
	@Bean
	public KeyGenerator keyGenerator() {
		return new CustomRedisKeyGenerator();
	}

	@Bean
	public CustomRedisTemplate customRedisTemplate(RedisConnectionFactory redisConnectionFactory){
		return new CustomRedisTemplate(redisConnectionFactory);
	}




	@Bean
	@Primary
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

		// 设置CacheManager的值序列化方式为json序列化
		ObjectMapper objectMapper = new ObjectMapper();
		// 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setDateFormat(new java.text.SimpleDateFormat(PATTERN_DATETIME));

		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));


		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		RedisCacheConfiguration customConfiguration = defaultCacheConfig
				.entryTtl(Duration.ofDays(entryTtl))
				.serializeValuesWith(pair);

		RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
		CustomRedisCacheManager customRedisCacheManager = new CustomRedisCacheManager(cacheWriter, customConfiguration,customRedisTemplate(redisConnectionFactory));

		// 空值 5 分钟失效
		customRedisCacheManager.setEmptyKeyExpire(Duration.ofSeconds(emptyKeyExpire));
		return customRedisCacheManager;
	}




//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//
//		// 初始化一个RedisCacheWriter
//		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
//
//
//		// 设置CacheManager的值序列化方式为json序列化
//		ObjectMapper objectMapper = new ObjectMapper();
//		// 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		// 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//		objectMapper.setDateFormat(new java.text.SimpleDateFormat(PATTERN_DATETIME));
//
//
//	 	RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);
//		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//		// 设置默认超过期时间是
//		//defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofDays(3));
//		// 初始化RedisCacheManager
//		RedisCacheManager redisCacheManager= new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
//		return redisCacheManager;
//
//	}






}
