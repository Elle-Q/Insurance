package com.fintech.insurance.micro.biz.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/11/10 0010 16:00
 */
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailPropertiesBean {
    private String username;//发送人
    public EmailPropertiesBean(String fromuser) {
        this.username = fromuser;
    }

    public EmailPropertiesBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
