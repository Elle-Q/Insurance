package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description: 还款信息VO
 * @Date: 2017/11/10 14:32
 */
public class RefundVO implements Serializable {
    // 还款计划ID
    private Integer repaymentPlanId;
    // 合同号
    private String contractCode;
    // 真实合同编码(展示使用)
    private String customerContractNumber;
    // 产品类型
    private String productType;
    // 业务单id
    private Integer requisitionId;
    // 业务单号
    private String requisitionNumber;
    // 车牌号/合格证
    private String carNumber;
    // 客户名称
    private String customerName;
    // 还款期数
    private Integer refundPhase;
    // 还款总额
    @FinanceDataPoint
    private Double refundAmount;
    // 还款本金金额
    @FinanceDataPoint
    private Double repayCapitalAmount;
    // 还款利息金额
    @FinanceDataPoint
    private Double repayInterestAmount;
    // 逾期罚金
    @FinanceDataPoint
    private Double overdueFines;
    // 还款日
    private Long refundDate;
    // 最后还款日
    private Long lastRefundDate;
    // 还款时间
    private Long refundTime;
    // 还款状态
    private String refundStatus;
    // 是否是人工处理状态
    private Boolean manualFlag;
    // 逾期罚息率
    @FinanceDataPoint
    private Double overdueRate;
    // 剩余本息金额
    @FinanceDataPoint
    private Double surplusAmount;
    // 还款记录的划扣状态
    private String confirmStatus;
    // 总还款期数
    private Integer totalInstalment;
    // 借款总额
    @FinanceDataPoint
    private Double borrowAmount;

    private Date repayDate;

    // 剩余本金（当期之后期的本金之和，不包含当期）
    @FinanceDataPoint
    private Double restCapitalAmount;

    private String repayDayType;

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Double getRepayCapitalAmount() {
        return repayCapitalAmount;
    }

    public void setRepayCapitalAmount(Double repayCapitalAmount) {
        this.repayCapitalAmount = repayCapitalAmount;
    }

    public Double getRepayInterestAmount() {
        return repayInterestAmount;
    }

    public void setRepayInterestAmount(Double repayInterestAmount) {
        this.repayInterestAmount = repayInterestAmount;
    }

    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Double getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(Double overdueRate) {
        this.overdueRate = overdueRate;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public Integer getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Integer repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public Boolean getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Boolean manualFlag) {
        this.manualFlag = manualFlag;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(Integer refundPhase) {
        this.refundPhase = refundPhase;
    }

    public Double getOverdueFines() {
        return overdueFines;
    }

    public void setOverdueFines(Double overdueFines) {
        this.overdueFines = overdueFines;
    }

    public Long getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Long refundDate) {
        this.refundDate = refundDate;
    }

    public Long getLastRefundDate() {
        return lastRefundDate;
    }

    public void setLastRefundDate(Long lastRefundDate) {
        this.lastRefundDate = lastRefundDate;
    }

    public Long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Long refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Integer getTotalInstalment() {
        return totalInstalment;
    }

    public void setTotalInstalment(Integer totalInstalment) {
        this.totalInstalment = totalInstalment;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Double getRestCapitalAmount() {
        return restCapitalAmount;
    }

    public void setRestCapitalAmount(Double restCapitalAmount) {
        this.restCapitalAmount = restCapitalAmount;
    }

    public String getCustomerContractNumber() {
        return customerContractNumber;
    }

    public void setCustomerContractNumber(String customerContractNumber) {
        this.customerContractNumber = customerContractNumber;
    }

    public String getRepayDayType() {
        return repayDayType;
    }

    public void setRepayDayType(String repayDayType) {
        this.repayDayType = repayDayType;
    }

}
