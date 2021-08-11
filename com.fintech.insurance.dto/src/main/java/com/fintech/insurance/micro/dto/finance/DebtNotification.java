package com.fintech.insurance.micro.dto.finance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 易极付扣款异步通知
 * @Author: Yong Li
 * @Date: 2017/12/13 13:37
 */
public class DebtNotification extends YjfNotification {

    /**
     * 商户扣款订单号
     */
    private String merchOrderNo;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 银行卡号；涉及用户银行卡号等隐私信息，用掩码展示
     */
    private String bankCardNo;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 扣款金额
     * Money类型
     */
    @FinanceDataPoint
    private Double transAmount;

    /**
     * 无论成功或者失败，只要有回执就会有； 处理中状态不会返回
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date receiptTime;

    /**
     * 回款状态
     * 包含：
     * 审核驳回: CHECK_REJECT
     * 还款失败：WITHHOLD_FAIL
     * 还款成功：WITHHOLD_SUCCESS
     * 分润成功：PROFIT_SUCCESS
     * 结算成功：SETTLE_SUCCESS
     */
    private String serviceStatus;

    /**
     * 错误码
     */
    private String errorCode;

    public String getMerchOrderNo() {
        return merchOrderNo;
    }

    public void setMerchOrderNo(String merchOrderNo) {
        this.merchOrderNo = merchOrderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
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

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "DebtNotification{" +
                "merchOrderNo='" + merchOrderNo + '\'' +
                ", name='" + name + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", transAmount=" + transAmount +
                ", receiptTime=" + receiptTime +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", protocol='" + protocol + '\'' +
                ", service='" + service + '\'' +
                ", version='" + version + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", success='" + success + '\'' +
                ", notifyTime='" + notifyTime + '\'' +
                '}';
    }
}
