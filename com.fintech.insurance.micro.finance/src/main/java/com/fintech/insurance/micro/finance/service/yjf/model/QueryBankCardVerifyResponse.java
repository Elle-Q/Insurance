package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.micro.finance.service.yjf.enums.SMSStatus;
import com.fintech.insurance.micro.finance.service.yjf.enums.VerifyCardStatus;

/**
 * @Description: 验卡查询响应
 * @Author: Nicholas
 * @Date: 2018/1/22
 */
public class QueryBankCardVerifyResponse extends YjfResponse{

    @JSONField(name = "outOrderNo")
    private String platformOrderNum;


    @JSONField(name = "name")
    private String userName;

    /**
     * 身份证号
     */
    @JSONField(name = "certNo")
    private String idNumber;

    @JSONField(name = "bankCardNo")
    private String bankCardNumber;

    @JSONField(name = "mobileNo")
    private String mobile;

    /**
     * 银行名称
     */
    @JSONField(name = "bankName")
    private String bankName;

    /**
     * 银行编码
     */
    @JSONField(name = "bankCode")
    private String bankCode;

    /**
     * 银行卡类型: DEBIT_CARD - 借记卡
     */
    @JSONField(name = "bankCardType")
    private String bankCardType;

    /**
     * 验卡状态
     */
    @JSONField(name = "serviceStatus")
    private VerifyCardStatus verifyCardStatus;

    @JSONField(name = "smsStatus")
    private SMSStatus smsStatus;

    /**
     * 验卡类型（三要素、四要素）
     */
    @JSONField(name = "verifyCardType")
    private String verifyCardType;

    @JSONField(name = "errorCode")
    private String errorCode;

    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public VerifyCardStatus getVerifyCardStatus() {
        return verifyCardStatus;
    }

    public void setVerifyCardStatus(VerifyCardStatus verifyCardStatus) {
        this.verifyCardStatus = verifyCardStatus;
    }

    public SMSStatus getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(SMSStatus smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getVerifyCardType() {
        return verifyCardType;
    }

    public void setVerifyCardType(String verifyCardType) {
        this.verifyCardType = verifyCardType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "QueryBankCardVerifyResponse{" +
                "platformOrderNum='" + platformOrderNum + '\'' +
                ", userName='" + userName + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", bankCardNumber='" + bankCardNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankCardType='" + bankCardType + '\'' +
                ", verifyCardStatus=" + verifyCardStatus +
                ", smsStatus=" + smsStatus +
                ", verifyCardType='" + verifyCardType + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
