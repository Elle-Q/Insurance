package com.fintech.insurance.micro.dto.biz;


import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Liu man
 * @Description: 服务合同vo
 * @Date: 2018/1/2 10:22
 */
public class LoanContractVO implements Serializable {
    // 合同id
    private Integer contractId;
    // 合同号
    private String contractCode;
    // 业务单id
    private Integer requisitionId;
    // 业务单号
    private String requisitionNumber;
    // 渠道编码
    private String channelCode;
    // 渠道名称
    private String channelName;
    //渠道用户姓名
    private String channelUserName;
    //渠道用户手机号码
    private String channelUserMobile;
    // 产品类型
    private String productType;
    // 客户名称
    private String customerName;
    //客户手机号码
    private String customerMobile;
    // 放款时间
    private Date loanDate;
    // 借款金额
    @FinanceDataPoint
    private Double borrowAmount;
    // 还款总期数
    private Integer totalPhase;
    // 已还款期数
    private Integer refundPhase;
    // 订单状态
    private String contractStatus;
    // 当期还款计划的状态
    private String refundStatus;
    // 最大逾期天数
    private Integer maxOverdueDays;
    // 合同月利率
    private Double interestRate;
    // 还款日
    private Date repayDate;
    // 是否逾期
    private Boolean overdueFlag;
    // 逾期天数
    private Integer overdueDays;
    //分期还款当期总金额，单位为分
    private Double repayTotalAmount;
    // 车牌号
    private String carNumber;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Double getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(Double repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public Integer getMaxOverdueDays() {
        return maxOverdueDays;
    }

    public void setMaxOverdueDays(Integer maxOverdueDays) {
        this.maxOverdueDays = maxOverdueDays;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUserName() {
        return channelUserName;
    }

    public void setChannelUserName(String channelUserName) {
        this.channelUserName = channelUserName;
    }

    public String getChannelUserMobile() {
        return channelUserMobile;
    }

    public void setChannelUserMobile(String channelUserMobile) {
        this.channelUserMobile = channelUserMobile;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Integer getTotalPhase() {
        return totalPhase;
    }

    public void setTotalPhase(Integer totalPhase) {
        this.totalPhase = totalPhase;
    }

    public Integer getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(Integer refundPhase) {
        this.refundPhase = refundPhase;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "ContractVO{" +
                "contractId=" + contractId +
                ", contractCode='" + contractCode + '\'' +
                ", requisitionId=" + requisitionId +
                ", requisitionNumber='" + requisitionNumber + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelUserName='" + channelUserName + '\'' +
                ", channelUserMobile='" + channelUserMobile + '\'' +
                ", productType='" + productType + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", loanDate=" + loanDate +
                ", borrowAmount=" + borrowAmount +
                ", totalPhase=" + totalPhase +
                ", refundPhase=" + refundPhase +
                ", contractStatus='" + contractStatus + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", maxOverdueDays=" + maxOverdueDays +
                ", interestRate=" + interestRate +
                ", repayDate=" + repayDate +
                ", overdueFlag=" + overdueFlag +
                ", overdueDays=" + overdueDays +
                ", repayTotalAmount=" + repayTotalAmount +
                '}';
    }
}