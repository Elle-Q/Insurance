package com.fintech.insurance.micro.dto.finance;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/12 9:55
 */
public class BankCardVerifyResult extends BaseBankResult {

    /**
     * 银行卡开卡银行名称
     */
    private String bankName;

    /**
     * 银行卡开卡银行代码
     */
    private String bankCode;

    // 验卡状态
    private String verificationStatus;

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }

    public String getRequestSerialNum() {
        return requestSerialNum;
    }

    public void setRequestSerialNum(String requestSerialNum) {
        this.requestSerialNum = requestSerialNum;
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

    @Override
    public String toString() {
        return "BankCardVerifyResult{" +
                "bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", isSuccess=" + isSuccess +
                ", requestSerialNum='" + requestSerialNum + '\'' +
                ", failedMessage='" + failedMessage + '\'' +
                '}';
    }
}
