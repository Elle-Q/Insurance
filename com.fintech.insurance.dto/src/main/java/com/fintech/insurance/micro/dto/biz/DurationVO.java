
package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

public class DurationVO implements Serializable {

    //分期期数
    private Integer duration;

    //合同id
    private Integer contractId;

    //合同code
    private String contractCode;

    //保单数量
    private Integer insuranceNum;

    //借款金额
    @FinanceDataPoint
    private Double borrowAmount;

    //借款开始时间
    private Date borrowStartTime;

    //借款结束时间
    private Date borrowEndTime;

    //还款日类型
    private String repayDayType;

    //还款方式
    private String repayType;

    //交强险+车船税比重
    @FinanceDataPoint
    private Double percentage;

    //月利率
    @FinanceDataPoint
    private Double interestRate;

    //可借比例
    @FinanceDataPoint
    private Double loanRatio;

    //车牌号
    private String carNumber;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Double getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Double loanRatio) {
        this.loanRatio = loanRatio;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(Integer insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Date getBorrowStartTime() {
        return borrowStartTime;
    }

    public void setBorrowStartTime(Date borrowStartTime) {
        this.borrowStartTime = borrowStartTime;
    }

    public Date getBorrowEndTime() {
        return borrowEndTime;
    }

    public void setBorrowEndTime(Date borrowEndTime) {
        this.borrowEndTime = borrowEndTime;
    }

    public String getRepayDayType() {
        return repayDayType;
    }

    public void setRepayDayType(String repayDayType) {
        this.repayDayType = repayDayType;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
}

