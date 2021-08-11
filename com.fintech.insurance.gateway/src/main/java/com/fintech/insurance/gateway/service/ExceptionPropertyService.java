package com.fintech.insurance.gateway.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8")
public class ExceptionPropertyService implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getProperty(String errorCode) {
        if (StringUtils.isEmpty(errorCode)) {
            return "";
        }
        return this.environment.getProperty(errorCode);
    }
}
