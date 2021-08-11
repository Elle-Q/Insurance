package com.fintech.insurance.micro.dto.thirdparty.sms;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/22 0022 17:34
 */
public class SMSCheckVerificationParamVO {
    /**
     * 业务流水，全局唯一标识
     */
    @NotNull(message = "106134")
    private String sequenceId;
    /**
     * 接收手机号码
     */
    @NotNull(message = "106123")
    private String phoneNumber;
    /**
     * 短信事件码
     */
    @NotNull(message = "106122")
    private String eventCode;
    /**
     * 验证码
     */
    @NotNull(message = "106134")
    private String verification;

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

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

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
