package com.fintech.insurance.micro.dto.thirdparty.sms;

import com.fintech.insurance.commons.enums.NotificationEvent;

import java.util.Arrays;
import java.util.Map;

/**
 * @Description: 短信发送请求
 * @Author: East
 * @Date: 2017/11/13 0013 20:28
 */
public class SMSSendRequestVO {

    /**
     * 短信接收号码
     */
    private String phoneNumbers[];
    /**
     * 短信事件
     */
    private NotificationEvent event;
    /**
     * 短信模板参数
     */
    private Map<String, String> templateParam;
    /**
     * 上行短信扩展码
     */
    private String upExtendCode;

    public SMSSendRequestVO() {
    }

    public SMSSendRequestVO(String phoneNumbers[], NotificationEvent event, Map<String, String> templateParam, String upExtendCode) {
        this.phoneNumbers = phoneNumbers;
        this.event = event;
        this.templateParam = templateParam;
        this.upExtendCode = upExtendCode;
    }

    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public NotificationEvent getEvent() {
        return event;
    }

    public void setEvent(NotificationEvent event) {
        this.event = event;
    }

    public Map<String, String> getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(Map<String, String> templateParam) {
        this.templateParam = templateParam;
    }

    public String getUpExtendCode() {
        return upExtendCode;
    }

    public void setUpExtendCode(String upExtendCode) {
        this.upExtendCode = upExtendCode;
    }

    @Override
    public String toString() {
        return "SMSSendRequestVO{" +
                "phoneNumbers=" + Arrays.toString(phoneNumbers) +
                ", event=" + event +
                ", templateParam=" + templateParam +
                ", upExtendCode='" + upExtendCode + '\'' +
                '}';
    }
}
