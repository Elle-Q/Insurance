package com.fintech.insurance.micro.dto.finance;

import java.util.Date;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:49
 */
public class RepaymentRecordVO {

    //还款记录ID
    private Integer id;

    //还款计划
    private FinanceRepaymentPlanVO repaymentPlanVO;

    //还款日期
    private Date repayDate;

    //还款时间
    private Date repayTime;

    //还款总金额，单位为分
    private Long repayTotalAmount;

    //还款本金，单位为分
    private Long repayCapitalAmount;

    //还款利息金额，单位为分
    private Long repayInterestAmount;

    //逾期罚息金额，单位为分
    private Long overdueInterestAmount;

    //是否逾期
    private Boolean isOverdue;

    //是否提前还款
    private Boolean isPrepayment;

    //提前还款惩罚金额，单位为分
    private Long prepaymentPenaltyAmount;

    //客户上传的凭证照片，以json数组存储
    private String customerVoucher;

    //交易流水号
    private String transactionSerial;

    //确认操作状态
    private String confirmStatus;

    //确认人标识
    private Integer confirmBy;

    //确认时间
    private Date confirmTime;

    //确认备注
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FinanceRepaymentPlanVO getRepaymentPlanVO() {
        return repaymentPlanVO;
    }

    public void setRepaymentPlanVO(FinanceRepaymentPlanVO repaymentPlanVO) {
        this.repaymentPlanVO = repaymentPlanVO;
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

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Boolean isOverdue) {
        isOverdue = isOverdue;
    }

    public Boolean getIsPrepayment() {
        return isPrepayment;
    }

    public void setIsPrepayment(Boolean isPrepayment) {
        isPrepayment = isPrepayment;
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
}
