package com.fintech.insurance.micro.thirdparty.service.sms;

import com.fintech.insurance.commons.enums.NotificationEvent;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/15 0015 9:59
 */
public abstract class AbstractSMSService implements InitializingBean {

    @Autowired
    private SMSProperties smsProperties;

    /**
     * 短信签名
     */
    private String signName = "诺米金服";
    /**
     * 事件短信模板
     */
    private Map<NotificationEvent, String> eventTemplateMap = new HashMap<NotificationEvent, String>();

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化短信配置
        if (StringUtils.isBlank(this.smsProperties.getSignName())) {
            this.signName = this.smsProperties.getSignName();
        }
        if (this.smsProperties.getEventTemplateMap() != null && !this.smsProperties.getEventTemplateMap().isEmpty()) {
            for (Map.Entry<String, String> entry : this.smsProperties.getEventTemplateMap().entrySet()) {
                this.eventTemplateMap.put(NotificationEvent.codeOf(entry.getKey()), entry.getValue());
            }
        }
    }

    public boolean isSupportedEvent(NotificationEvent event) {
        return this.eventTemplateMap.containsKey(event);
    }

    public String getEventTemplateCode(NotificationEvent event) {
        return this.eventTemplateMap.get(event);
    }
}
