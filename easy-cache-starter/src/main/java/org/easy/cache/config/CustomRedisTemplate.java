package org.easy.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 缓存专用 RedisTemplate
 * key 使用 String 序列化
 * value 使用 kryo 序列化
 */
public class CustomRedisTemplate extends RedisTemplate {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public CustomRedisTemplate(RedisConnectionFactory connectionFactory) {

//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        setKeySerializer(stringRedisSerializer);
//        setHashKeySerializer(stringRedisSerializer);


        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat(PATTERN_DATETIME));

        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        setValueSerializer(jsonSerializer);
        setHashValueSerializer(jsonSerializer);

        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }
}
