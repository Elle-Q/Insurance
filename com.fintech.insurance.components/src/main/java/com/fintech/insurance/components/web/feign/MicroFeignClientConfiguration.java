package com.fintech.insurance.components.web.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/2 15:00
 */
@Configuration
public class MicroFeignClientConfiguration {

    @Autowired
    @Qualifier("jacksonObjectMapper")
    private ObjectMapper jacksonObjectMapper;

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .encoder(new JacksonEncoder(jacksonObjectMapper))
                .decoder(new JacksonDecoder(jacksonObjectMapper))
                .requestInterceptor(new FeignRequestInterceptor());
    }

    @Bean
    public Logger.Level feignLoggerLevel(ConfigurableEnvironment env) {
        if (SystemProfile.DEFAULT == FInsuranceApplicationContext.getSystemProfile(env)) {
            return Logger.Level.FULL; //只在本地开发环境才打开Feign的日志
        }
        return Logger.Level.BASIC;
    }

    @Bean
    Request.Options feignOptions(ConfigurableEnvironment env) {
        //return new Request.Options(*//**connectTimeoutMillis**//* 5 * 1000, *//** readTimeoutMillis **//* 0);
        if (SystemProfile.DEFAULT == FInsuranceApplicationContext.getSystemProfile(env)) { // 开发环境设置读取数据时间100分钟，同时需要设置nginx的proxy_connection_timeout和proxy_read_timeout，必须大于该值
            return new Request.Options(150 * 1000, 150 * 1000);
        } else {
            return new Request.Options(120 * 1000, 120 * 1000);
        }
    }

    @Bean
    Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }
}
