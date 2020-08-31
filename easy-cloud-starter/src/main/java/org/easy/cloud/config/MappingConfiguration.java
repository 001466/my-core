package org.easy.cloud.config;
//
//import com.deocean.common.jackson.BladeJavaTimeModule;
//import com.deocean.common.util.DateUtil;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//
//import java.text.SimpleDateFormat;
//import java.time.ZoneId;
//import java.util.Locale;
//import java.util.TimeZone;
//
//@Configuration
//public class MappingConfiguration {
//
//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.setDateFormat(new java.text.SimpleDateFormat(DateUtil.PATTERN_DATETIME));
//        MappingJackson2HttpMessageConverter mappingJsonpHttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
//        return mappingJsonpHttpMessageConverter;
//    }
//
//
//
//}
