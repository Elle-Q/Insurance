package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;

/**
 * 验证码VO
 */
public class VerificationCodeVO implements Serializable {

    //短信序列号
    private String sequenceId;

    //短信事件
    private String eventCode;

    //短信验证码
    private String verification;

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
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
