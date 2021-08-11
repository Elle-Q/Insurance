package com.fintech.insurance.components.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Description: 通用组件
 * @Author: Yong Li
 * @Date: 2017/12/28 11:53
 */
@Component
public class CommonComponents {

    @Bean("jacksonObjectMapper")
    @Scope("prototype")
    public ObjectMapper getJacksonConfig() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.ALWAYS)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
