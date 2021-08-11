package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.micro.finance.service.yjf.enums.SMSStatus;
import com.fintech.insurance.micro.finance.service.yjf.enums.VerifyCardStatus;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/9 16:42
 */
public class BankCardVerifyResponse extends YjfResponse {

    /**
     * 商户订单号: 新验卡订单需保证外部订单号的唯一性
     *
     * 如商户传入同一订单号，视为重发验证短信
     */
    @JSONField(name = "outOrderNo")
    private String platformOrderNum;

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

    @JSONField(name = "errorCode")
    private String errorCode;

    @Override
    public String toString() {
        return "BankCardVerifyResponse{" +
                "platformOrderNum='" + platformOrderNum + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankCardType='" + bankCardType + '\'' +
                ", verifyCardStatus=" + verifyCardStatus +
                ", smsStatus=" + smsStatus +
                ", errorCode='" + errorCode + '\'' +
                ", platformOrderNo='" + platformOrderNo + '\'' +
                ", resultCodeType=" + resultCodeType +
                ", resultMessage='" + resultMessage + '\'' +
                ", success='" + isSuccess + '\'' +
                ", notifyTime='" + notifyTime + '\'' +
                ", protocol='" + protocol + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", version='" + version + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", platformOrderNo='" + platformOrderNo + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
