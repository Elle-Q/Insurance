package com.fintech.insurance.micro.dto.thirdparty.sms;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/22 0022 17:26
 */
public class SMSSendVerificationParamVO {
    /**
     * 短信接收号码
     */
    @NotNull(message = "106123")
    private String phoneNumber;
    /**
     * 短信事件编码
     */
    @NotNull(message = "106122")
    private String eventCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

}
