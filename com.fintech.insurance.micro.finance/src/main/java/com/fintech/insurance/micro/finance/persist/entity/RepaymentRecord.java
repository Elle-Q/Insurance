package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 14:46
 */
@Entity
@Table(name = "finance_repayment_record")
public class RepaymentRecord extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "repayment_plan_id", columnDefinition = "int(11) unsigned not null comment '还款计划ID'")
    private RepaymentPlan repaymentPlan;

    @Column(name = "repay_date", columnDefinition = "date not null comment '还款日期'")
    private Date repayDate;

    @Column(name = "repay_time", columnDefinition = "datetime not null comment '还款时间'")
    private Date repayTime;

    @Column(name = "repay_batch_no", columnDefinition = "varchar(64) comment '合并扣款订单号'")
    private String repayBatchNo;

    @Column(name = "repay_total_amount", columnDefinition = "bigint unsigned not null comment '还款总金额，单位为分'")
    private Long repayTotalAmount;

    @Column(name = "repay_capital_amount", columnDefinition = "bigint unsigned not null comment '还款本金，单位为分'")
    private Long repayCapitalAmount;

    @Column(name = "bank_account_number", columnDefinition = "varchar(32) comment '扣款银行卡卡号'")
    private String bankAccountNumber;

    @Column(name = "repay_interest_amount", columnDefinition = "bigint unsigned not null comment '还款利息金额，单位为分'")
    private Long repayInterestAmount;

    @Column(name = "overdue_interest_amount", columnDefinition = "bigint unsigned not null comment '逾期罚息金额，单位为分'")
    private Long overdueInterestAmount = 0L;

    @Column(name = "is_overdue", columnDefinition = "boolean not null comment '是否逾期'")
    private Boolean overdueFlag;

    @Column(name = "is_prepayment", columnDefinition = "boolean not null comment '是否提前还款'")
    private Boolean prepaymentFlag;

    @Column(name = "prepayment_penalty_amount", columnDefinition = "bigint unsigned not null comment '提前还款惩罚金额，单位为分'")
    private Long prepaymentPenaltyAmount;

    @Column(name = "customer_voucher", columnDefinition = "text comment '客户上传的凭证照片，以json数组存储'")
    private String customerVoucher;

    @Column(name = "transaction_serial", columnDefinition = "varchar(64) comment '交易流水号'")
    private String transactionSerial;

    @Column(name = "confirm_status", columnDefinition = "varchar(16) not null comment '确认操作状态'")
    private String confirmStatus;

    @Column(name = "confirmed_by", columnDefinition = "int(10) comment '确认人标识'")
    private Integer confirmBy;

    @Column(name = "confirmed_time", columnDefinition = "datetime comment '确认时间'")
    private Date confirmTime;

    @Column(name = "remark", columnDefinition = "text comment '确认备注'")
    private String remark;

    public RepaymentPlan getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(RepaymentPlan repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Date getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(Date repayTime) {
        this.repayTime = repayTime;
    }

    public Long getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(Long repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
    }

    public Long getRepayCapitalAmount() {
        return repayCapitalAmount;
    }

    public void setRepayCapitalAmount(Long repayCapitalAmount) {
        this.repayCapitalAmount = repayCapitalAmount;
    }

    public Long getRepayInterestAmount() {
        return repayInterestAmount;
    }

    public void setRepayInterestAmount(Long repayInterestAmount) {
        this.repayInterestAmount = repayInterestAmount;
    }

    public Long getOverdueInterestAmount() {
        return overdueInterestAmount;
    }

    public void setOverdueInterestAmount(Long overdueInterestAmount) {
        this.overdueInterestAmount = overdueInterestAmount;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public Boolean getPrepaymentFlag() {
        return prepaymentFlag;
    }

    public void setPrepaymentFlag(Boolean prepaymentFlag) {
        this.prepaymentFlag = prepaymentFlag;
    }

    public Long getPrepaymentPenaltyAmount() {
        return prepaymentPenaltyAmount;
    }

    public void setPrepaymentPenaltyAmount(Long prepaymentPenaltyAmount) {
        this.prepaymentPenaltyAmount = prepaymentPenaltyAmount;
    }

    public String getCustomerVoucher() {
        return customerVoucher;
    }

    public void setCustomerVoucher(String customerVoucher) {
        this.customerVoucher = customerVoucher;
    }

    public String getTransactionSerial() {
        return transactionSerial;
    }

    public void setTransactionSerial(String transactionSerial) {
        this.transactionSerial = transactionSerial;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Integer getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(Integer confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepayBatchNo() {
        return repayBatchNo;
    }

    public void setRepayBatchNo(String repayBatchNo) {
        this.repayBatchNo = repayBatchNo;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
