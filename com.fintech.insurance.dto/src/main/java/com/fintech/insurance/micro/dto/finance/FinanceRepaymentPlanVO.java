package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.enums.RepayDayType;

import java.util.Date;

/**
 * @Description: (业务还款计划vo)
 * @Author: East
 * @Date: 2017/11/16 0016 20:24
 */
public class FinanceRepaymentPlanVO {

    private Integer id;

    //客户id,关联cust_account表的主键
    private Integer customerId;

    //合同编号
    private String contractNumber;

    //渠道id
    private Integer channelId;

    //还款日
    private Date repayDate;

    //分期还款当期总金额，单位为分
    private Double repayTotalAmount;

    //分期还款本金金额
    private Double repayCapitalAmount;

    //分期还款利息金额
    private Double repayInterestAmount;

    //本次分期之后剩余本金金额
    private Double restCapitalAmount;

    //还款记录所在的期数
    private Integer currentInstalment;

    //总期数
    private Integer totalInstalment;

    //还款状态
    private RefundStatus repayStatus;

    //人工干预标识
    private Boolean manualFlag;

    // 逾期罚金
    private Double overdueFine;

    // 还款时间
    private Date actualRepayDate;

    // 是否逾期
    private Boolean overdueFlag;

    //利率点（万倍）
    private Double interestRate;

    // 逾期天数
    private long overdueDays;

    //提前还款天数
    private Integer advanceRepayDays = 0;

    //还款类型
    private RepayDayType type;

    //产品类型
    private ProductType productType;

    public FinanceRepaymentPlanVO() {
    }

    public FinanceRepaymentPlanVO(Integer id, String contractNumber, Date repayDate, Double repayCapitalAmount, Double restCapitalAmount) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.repayDate = repayDate;
        this.repayCapitalAmount = repayCapitalAmount;
        this.restCapitalAmount = restCapitalAmount;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public Double getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(Double overdueFine) {
        this.overdueFine = overdueFine;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Double getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(Double repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
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

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Boolean getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Boolean manualFlag) {
        this.manualFlag = manualFlag;
    }

    public Integer getAdvanceRepayDays() {
        return advanceRepayDays;
    }

    public void setAdvanceRepayDays(Integer advanceRepayDays) {
        this.advanceRepayDays = advanceRepayDays;
    }

    public RepayDayType getType() {
        return type;
    }

    public void setType(RepayDayType type) {
        this.type = type;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Double getRestCapitalAmount() {
        return restCapitalAmount;
    }

    public void setRestCapitalAmount(Double restCapitalAmount) {
        this.restCapitalAmount = restCapitalAmount;
    }

    @Override
    public String toString() {
        return "RepaymentPlanVO{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", contractNumber='" + contractNumber + '\'' +
                ", channelId=" + channelId +
                ", repayDate=" + repayDate +
                ", repayTotalAmount=" + repayTotalAmount +
                ", repayCapitalAmount=" + repayCapitalAmount +
                ", repayInterestAmount=" + repayInterestAmount +
                ", currentInstalment=" + currentInstalment +
                ", totalInstalment=" + totalInstalment +
                ", repayStatus=" + repayStatus +
                ", manualFlag=" + manualFlag +
                ", overdueFine=" + overdueFine +
                ", actualRepayDate=" + actualRepayDate +
                '}';
    }
}
