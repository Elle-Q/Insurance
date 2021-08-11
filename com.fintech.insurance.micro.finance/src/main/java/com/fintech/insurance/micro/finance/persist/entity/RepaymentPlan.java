package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.enums.converter.RefundStatusConverter;
import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/17 0017 9:13
 */
@Entity
@Table(name = "finance_repayment_plan")
public class RepaymentPlan extends BaseEntity {

    @Column(name = "customer_id", columnDefinition = "int(10) unsigned not null comment '客户id,关联cust_account表的主键'")
    private Integer customerId;

    @Column(name = "contract_number", columnDefinition = "varchar(64) not null comment '合同编号'")
    private String contractNumber;

    @Column(name = "channel_id", columnDefinition = "int(10) unsigned not null comment '渠道id'")
    private Integer channelId;

    @Column(name = "repay_date", columnDefinition = "date not null comment '还款日'")
    private Date repayDate;

    @Column(name = "repay_total_amount", columnDefinition = "bigint unsigned not null comment '分期还款当期总金额，单位为分'")
    private BigDecimal repayTotalAmount;

    @Column(name = "repay_capital_amount", columnDefinition = "bigint unsigned not null comment '分期还款本金金额'")
    private BigDecimal repayCapitalAmount;

    @Column(name = "repay_interest_amount", columnDefinition = "bigint unsigned not null comment '分期还款利息金额'")
    private BigDecimal repayInterestAmount;

    @Column(name = "rest_capital_amount", columnDefinition = "bigint unsigned not null comment '本期还款计划还完之后还剩余未还本金金额'")
    private BigDecimal restCapitalAmount;

    @Column(name = "current_instalment", columnDefinition = "int unsigned not null comment '还款计划所在的期数'")
    private Integer currentInstalment;

    @Column(name = "total_instalment", columnDefinition = "int unsigned not null comment '总期数'")
    private Integer totalInstalment;

    @Convert(converter = RefundStatusConverter.class)
    @Column(name = "repay_status", columnDefinition = "varchar(16) not null comment '还款状态'")
    private RefundStatus repayStatus;

    @Column(name = "manual_flag", columnDefinition = "boolean not null default 0 comment '人工干预标识'")
    private Boolean manualFlag = false;

    @OneToMany(mappedBy = "repaymentPlan", fetch = FetchType.EAGER)
    private Set<RepaymentRecord> repaymentRecordSet;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public BigDecimal getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(BigDecimal repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
    }

    public BigDecimal getRepayCapitalAmount() {
        return repayCapitalAmount;
    }

    public void setRepayCapitalAmount(BigDecimal repayCapitalAmount) {
        this.repayCapitalAmount = repayCapitalAmount;
    }

    public BigDecimal getRepayInterestAmount() {
        return repayInterestAmount;
    }

    public void setRepayInterestAmount(BigDecimal repayInterestAmount) {
        this.repayInterestAmount = repayInterestAmount;
    }

    public Integer getCurrentInstalment() {
        return currentInstalment;
    }

    public void setCurrentInstalment(Integer currentInstalment) {
        this.currentInstalment = currentInstalment;
    }

    public Integer getTotalInstalment() {
        return totalInstalment;
    }

    public void setTotalInstalment(Integer totalInstalment) {
        this.totalInstalment = totalInstalment;
    }

    public RefundStatus getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(RefundStatus repayStatus) {
        this.repayStatus = repayStatus;
    }

    public Boolean getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Boolean manualFlag) {
        this.manualFlag = manualFlag;
    }

    public Set<RepaymentRecord> getRepaymentRecordSet() {
        return repaymentRecordSet;
    }

    public void setRepaymentRecordSet(Set<RepaymentRecord> repaymentRecordSet) {
        this.repaymentRecordSet = repaymentRecordSet;
    }

    public BigDecimal getRestCapitalAmount() {
        return restCapitalAmount;
    }

    public void setRestCapitalAmount(BigDecimal restCapitalAmount) {
        this.restCapitalAmount = restCapitalAmount;
    }
}
