package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 易极付扣款查询响应
 * @Author: Yong Li
 * @Date: 2017/12/20 18:16
 */
public class DebtQueryResponse extends YjfResponse {

    /**
     * 交易背景商户订单号: 【录入交易背景】的商户订单号 ，等同于【录入交易背景】接口中的merchOrderNo；或直接传入商户合同
     */
    @JSONField(name = "backgroundMerchOrderNo")
    private String backgroundMerchOrderNo;

    @JSONField(name = "merchOrderNo")
    private String platformOrderNo;

    /**
     * 客户姓名
     */
    @JSONField(name = "name")
    private String customerName;

    /**
     * 客户身份证号
     */
    @JSONField(name = "certNo")
    private String certNo;

    /**
     * 预留手机号（用于发送短信）
     */
    @JSONField(name="mobileNo")
    private String mobileNo;

    /**
     * 扣款银行卡号，这里不支持支付账户ID。否则有划转其他账户资金风险
     */
    @JSONField(name = "bankCardNo")
    private String bankCardNo;

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
     * 扣款金额
     * 如: 88.88
     */
    @JSONField(name = "transAmount")
    private Double amount;

    /**
     * 代扣成功时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date receiptTime;

    /**
     * 验卡处理中: VERIFY_CARD_DEALING
     * 验卡失败: VERIFY_CARD_FAIL
     * 待审核: CHECK_NEEDED
     * 审核驳回: CHECK_REJECT
     * 待处理：INIT
     * 还款处理中：WITHHOLD_DEALING
     * 还款失败：WITHHOLD_FAIL
     * 还款成功：WITHHOLD_SUCCESS
     * 分润成功：PROFIT_SUCCESS
     * 结算成功：SETTLE_SUCCESS
     */
    @JSONField(name = "serviceStatus")
    private String serviceStatus;

    /**
     * 错误码
     */
    private String errorCode;

    public String getBackgroundMerchOrderNo() {
        return backgroundMerchOrderNo;
    }

    public void setBackgroundMerchOrderNo(String backgroundMerchOrderNo) {
        this.backgroundMerchOrderNo = backgroundMerchOrderNo;
    }

    @Override
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    @Override
    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
}
