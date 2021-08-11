package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/20 11:15
 */
public class VoucherVO implements Serializable{
    // 记账类型
    @NotBlank(message = "105005")
    private String accountType;
    // 还款记录id
    private Integer repaymentRecordId;
    // 支付订单id
    private Integer paymentOrderId;
    // 申请单id
    private Integer requisitionId;
    // 申请单code
    private String requisitionCode;
    // 交易流水号
    private String transactionSerial;
    // 用户id
    private Integer userId;
    // 图片uuid的json数组
    private String voucher;
    // 转账金额
    @FinanceDataPoint
    private Double accountAmount;
    // 备注
    private String remark;

    //图片资源
    private String[] imageKeys;

    public String[] getImageKeys() {
        return imageKeys;
    }

    public void setImageKeys(String[] imageKeys) {
        this.imageKeys = imageKeys;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(Integer repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
    }

    public Integer getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Integer paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getRequisitionCode() {
        return requisitionCode;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
    }

    public String getTransactionSerial() {
        return transactionSerial;
    }

    public void setTransactionSerial(String transactionSerial) {
        this.transactionSerial = transactionSerial;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }
}
