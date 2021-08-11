package com.fintech.insurance.micro.dto.thirdparty.sms;

import com.fintech.insurance.commons.utils.encode.MD5Utils;
import com.fintech.insurance.commons.web.FintechResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/15 0015 15:55
 */
public class SMSCacheVO implements Serializable {

    /**
     * 业务流水，全局唯一标识
     */
    private String sequenceId;
    /**
     * 接收手机号码
     */
    private String phoneNumber;
    /**
     * 短信事件码
     */
    private String eventCode;
    /**
     * 验证码
     */
    private String verification;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 验证签名
     */
    private String signature;

    public SMSCacheVO(String sequenceId, String phoneNumber, String eventCode, String verification, Date expireTime) {
        this.sequenceId = sequenceId;
        this.phoneNumber = phoneNumber;
        this.eventCode = eventCode;
        this.verification = verification;
        this.expireTime = expireTime;
        this.signature = this.buildSignature(this.sequenceId, this.phoneNumber, this.eventCode, this.verification);
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getVerification() {
        return verification;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public String getSignature() {
        return signature;
    }

    public boolean checkVerification(String sequenceId, String phoneNumber, String eventCode, String verification) {
        DateTime expireTime = new DateTime(this.getExpireTime());
        if (expireTime.isAfterNow() && StringUtils.equalsIgnoreCase(verification, this.getVerification()) && this.checkSignature(sequenceId, phoneNumber, eventCode, verification)) {
            return true;
        }

        return false;
    }


    private boolean checkSignature(String sequenceId, String phoneNumber, String eventCode, String verification) {
        return StringUtils.equalsIgnoreCase(this.signature, this.buildSignature(sequenceId, phoneNumber, eventCode, verification));
    }

    private String buildSignature(String sequenceId, String phoneNumber, String eventCode, String verification) {
        return MD5Utils.encode(StringUtils.join(new String[]{sequenceId, phoneNumber, eventCode, verification}));
    }

    @Override
    public String toString() {
        return "SMSCacheVO{" +
                "sequenceId='" + sequenceId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", eventCode='" + eventCode + '\'' +
                ", verification='" + verification + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
