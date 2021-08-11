package com.fintech.insurance.components.web.configuration;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/28 18:59
 */

@Component
@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8")
public class ExceptionProperties implements EnvironmentAware {

    private Environment environment;

    public String getMessage(String errCode) {
        return this.environment.getProperty(errCode);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
