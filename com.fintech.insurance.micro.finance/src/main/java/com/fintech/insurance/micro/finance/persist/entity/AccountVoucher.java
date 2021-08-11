package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:33
 */
@Entity
@Table(name = "finance_account_voucher")
public class AccountVoucher extends BaseEntity {

    @Column(name = "account_type", columnDefinition = "varchar(16) not null comment '记账类型'")
    private String accountType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "repayment_record_id")
    private RepaymentRecord repaymentRecord;

    @ManyToOne
    @JoinColumn(name = "payment_order_id")
    private PaymentOrder paymentOrder;

    @Column(name = "transaction_serial", columnDefinition = "varchar(64) not null")
    private String transactionSerial;

    @Column(name = "requisition_id", columnDefinition = "varchar(128)")
    private Integer requisitionId;

    @Column(name = "requisition_code", columnDefinition = "varchar(128)")
    private String requisitionCode;

    @Column(name = "user_id", columnDefinition = "int(10) unsigned not null")
    private Integer userId;

    @Column(name = "voucher", columnDefinition = "text not null")
    private String voucher;

    @Column(name = "account_amount", columnDefinition = "bigint unsigned not null")
    private BigDecimal accountAmount;

    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    public String getRequisitionCode() {
        return requisitionCode;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public RepaymentRecord getRepaymentRecord() {
        return repaymentRecord;
    }

    public void setRepaymentRecord(RepaymentRecord repaymentRecord) {
        this.repaymentRecord = repaymentRecord;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
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

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }
}
