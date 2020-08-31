/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.easy.secure.config;



import lombok.extern.slf4j.Slf4j;
import org.easy.secure.util.SecureRawUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * 工具配置类
 *
 * @author Chill
 */
@Configuration
@EnableScheduling
@Slf4j
public class SecureConfiguration {


    @Bean(name = "secureRawUtilRestTemplate")
    @LoadBalanced
    RestTemplate secureRawUtilRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecureRawUtil secureRawUtil() {
        return new SecureRawUtil(secureRawUtilRestTemplate());
    }



}
