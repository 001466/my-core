package org.easy.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

@Slf4j
public class CustomRedisKeyGenerator implements KeyGenerator {

    public static final String NO_PARAM_KEY = "()";
    public static final String NULL_PARAM_KEY = "";

    @Override
    public Object generate(Object target, Method method, Object... params) {

        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName()).append(".").append(method.getName());
        if (params.length == 0) {
            return key.append(NO_PARAM_KEY).toString();
        }

        StringBuilder args=new StringBuilder();
        for (Object param : params) {
            if (param == null) {
                args.append(NULL_PARAM_KEY);
            } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                int length = Array.getLength(param);
                for (int i = 0; i < length; i++) {
                    args.append(Array.get(param, i));
                    args.append(',');
                }
            } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                args.append(param);
            } else {
                log.warn("Using an object as a cache key may lead to unexpected results. "
                        + "Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass()
                        + "#" + method.getName());
                args.append(param.toString().hashCode());
            }
            args.append(',');
        }

        key.append("(").append(args.substring(0, args.length()-1)).append(")");



        String finalKey=key.toString();
        log.info("using cache key={} ", finalKey);
        return finalKey;


    }
}
