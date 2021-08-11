package com.fintech.insurance.micro.dto.thirdparty.sms;

import com.fintech.insurance.commons.enums.NotificationEvent;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/21 0021 15:33
 */
public class SMSSendParamVO {
    /**
     * 短信接收号码
     */
    @NotEmpty(message = "106121")
    private String[] phoneNumbers;
    /**
     * 短信事件
     */
    @NotNull(message = "106122")
    private NotificationEvent event;
    /**
     * 短信模板参数
     */
    private Map<String, String> smsParams;

    public SMSSendParamVO() {
    }

    public SMSSendParamVO(String[] phoneNumbers, NotificationEvent event, Map<String, String> smsParams) {
        this.phoneNumbers = phoneNumbers;
        this.event = event;
        this.smsParams = smsParams;
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

    public Map<String, String> getSmsParams() {
        return smsParams;
    }

    public void setSmsParams(Map<String, String> smsParams) {
        this.smsParams = smsParams;
    }

    @Override
    public String toString() {
        return "SMSSendParamVO{" +
                "phoneNumbers=" + Arrays.toString(phoneNumbers) +
                ", event=" + event +
                ", smsParams=" + smsParams +
                '}';
    }
}
