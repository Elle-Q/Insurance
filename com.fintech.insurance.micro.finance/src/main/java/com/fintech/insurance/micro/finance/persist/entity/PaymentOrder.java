package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 14:56
 */

@Entity
@Table(name = "finance_payment_order")
public class PaymentOrder extends BaseEntity {

    @Column(name = "customer_id", columnDefinition = "int(10) unsigned not null")
    private Integer customerId;

    @Column(name = "payment_status", columnDefinition = "varchar(16) not null comment '支付状态'")
    private String paymentStatus;

    @Column(name = "order_number", columnDefinition = "varchar(64) not null comment '订单号，唯一'")
    private String orderNumber;

    @Column(name = "order_amount", columnDefinition = "bigint unsigned not null comment '订单金额，折前金额，单位为分'")
    private BigDecimal orderAmount;

    @Column(name = "discount_amount", columnDefinition = "bigint unsigned not null default 0 comment '折扣金额，单位为分'")
    private BigDecimal discountAmount;

    @Column(name = "total_amount", columnDefinition = "bigint unsigned not null comment '订单总金额，折前金额减去折扣金额，单位为分'")
    private BigDecimal totalAmount;

    @Column(name = "payment_method", columnDefinition = "varchar(16) comment '支付方式'")
    private String paymentMethod;

    @Column(name = "payment_time", columnDefinition = "datetime comment '支付时间'")
    private Date paymentTime;

    @Column(name = "payment_amount", columnDefinition = "bigint unsigned not null default 0 comment '支付金额，单位为分'")
    private BigDecimal paymentAmount;

    @Column(name = "transactiont_serial", columnDefinition = "varchar(64) comment '支付流水号'")
    private String transactionSerial;

    @Column(name = "customer_voucher", columnDefinition = "text")
    private String customerVoucher;

    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    @Column(name = "is_manual", columnDefinition = "boolean not null default 0 comment '人工介入操作'")
    private Boolean manualFlag = false;

    @OneToMany(mappedBy = "paymentOrder", fetch = FetchType.LAZY)
    private Set<AccountVoucher> accountVouchers;

    @Column(name = "bank_account_number", columnDefinition = "varchar(32) comment '扣款银行卡卡号'")
    private String bankAccountNumber;

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Set<AccountVoucher> getAccountVouchers() {
        return accountVouchers;
    }

    public void setAccountVouchers(Set<AccountVoucher> accountVouchers) {
        this.accountVouchers = accountVouchers;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getTransactionSerial() {
        return transactionSerial;
    }

    public void setTransactionSerial(String transactionSerial) {
        this.transactionSerial = transactionSerial;
    }

    public String getCustomerVoucher() {
        return customerVoucher;
    }

    public void setCustomerVoucher(String customerVoucher) {
        this.customerVoucher = customerVoucher;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Boolean manualFlag) {
        this.manualFlag = manualFlag;
    }
}
