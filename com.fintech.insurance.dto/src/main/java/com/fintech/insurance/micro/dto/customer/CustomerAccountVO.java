package com.fintech.insurance.micro.dto.customer;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/13 14:33
 */
public class CustomerAccountVO implements Serializable {
    // 姓名
    private String name;
    // 身份证号
    private String idNumber;
    // 银行名称
    private String bankName;
    // 银行编码
    private String bankCode;
    // 银行卡号
    @NotNull(message = "102001")
    private String bankCardNumber;
    // 银行预留手机号
    @NotNull(message = "102001")
    private String phone;
    // 图片验证码
    private String verificationCode;
    // 图片验证码序列号
    private String serialNumber;

    //短信序列号
    private String sequenceId;

    //短信事件
    private String eventCode;

    //短信验证码
    private String verification;

    //身份证正面照
    private String idFront;

    //身份证反面照
    private String idBack;

    public String getIdFront() {
        return idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
