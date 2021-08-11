package com.fintech.insurance.micro.thirdparty.service.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 系统短信配置
 * @Author: East
 * @Date: 2017/11/15 0015 9:02
 */
@Component
@ConfigurationProperties(prefix = "sms")
public class SMSProperties {

    /**
     * 短信签名
     */
    private String signName;
    /**
     * 事件短信模板
     */
    private Map<String, String> eventTemplateMap;

    public SMSProperties() {
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Map<String, String> getEventTemplateMap() {
        return eventTemplateMap;
    }

    public void setEventTemplateMap(Map<String, String> eventTemplateMap) {
        this.eventTemplateMap = eventTemplateMap;
    }
}
