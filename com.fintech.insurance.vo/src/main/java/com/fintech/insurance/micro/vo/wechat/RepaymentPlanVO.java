package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: qxy
 * @Description: 分期明细VO
 * @Date: 2017/12/8 14:53
 */
public class RepaymentPlanVO implements Serializable{
    // 还款计划id
    private Integer repaymentPlanId;
    // 还款状态
    private String refundStatus;
    // 总期数
    private Integer totalInstalment;
    // 当前期数
    private Integer currentInstalment;
    // 当前应还金额
    @FinanceDataPoint
    private Double repayTotalAmount;
    // 利息金额
    @FinanceDataPoint
    private Double repayInterestAmount;
    // 逾期罚金
    @FinanceDataPoint
    private Double repayOverdueAmount;
    // 还款日
    private Date repayDate;
    // 逾期标志
    private Boolean overdueFlag;
    // 逾期天数
    private long overdueDays;

    public Integer getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Integer repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getTotalInstalment() {
        return totalInstalment;
    }

    public void setTotalInstalment(Integer totalInstalment) {
        this.totalInstalment = totalInstalment;
    }

    public Integer getCurrentInstalment() {
        return currentInstalment;
    }

    public void setCurrentInstalment(Integer currentInstalment) {
        this.currentInstalment = currentInstalment;
    }

    public Double getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(Double repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
    }

    public Double getRepayInterestAmount() {
        return repayInterestAmount;
    }

    public void setRepayInterestAmount(Double repayInterestAmount) {
        this.repayInterestAmount = repayInterestAmount;
    }

    public Double getRepayOverdueAmount() {
        return repayOverdueAmount;
    }

    public void setRepayOverdueAmount(Double repayOverdueAmount) {
        this.repayOverdueAmount = repayOverdueAmount;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }
}
