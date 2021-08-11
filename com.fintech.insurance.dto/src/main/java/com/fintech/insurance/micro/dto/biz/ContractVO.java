package com.fintech.insurance.micro.dto.biz;


import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description: 合同VO
 * @Date: 2017/11/9 18:22
 */
public class ContractVO implements Serializable {
    // 合同id
    protected Integer contractId;
    // 合同号
    protected String contractCode;
    // 真实合同编码(展示使用的合同号)
    protected String customerContractNumber;
    // 业务单id
    protected Integer requisitionId;
    // 业务单号
    protected String requisitionNumber;
    // 渠道编码
    protected String channelCode;
    // 渠道名称
    protected String channelName;
    //渠道用户姓名
    protected String channelUserName;
    //渠道用户手机号码
    protected String channelUserMobile;
    // 产品类型
    protected String productType;
    // 客户名称
    protected String customerName;
    //客户手机号码
    protected String customerMobile;
    // 放款时间
    protected Date loanDate;
    // 借款金额
    @FinanceDataPoint
    protected Double borrowAmount;
    // 还款总期数
    protected Integer totalPhase;
    // 已还款期数
    protected Integer refundPhase;
    // 订单状态
    protected String contractStatus;
    // 当期还款计划的状态
    protected String refundStatus;
    // 最大逾期天数
    protected Integer maxOverdueDays;
    // 合同月利率
    @FinanceDataPoint
    protected Double interestRate;
    // 还款日
    protected Date repayDate;
    // 是否逾期
    protected Boolean overdueFlag;
    // 逾期天数
    protected long overdueDays;
    //分期还款当期总金额，单位为分
    @FinanceDataPoint
    protected Double repayTotalAmount;
    // 车牌号
    protected String carNumber;

    // 用户id
    protected Integer customerId;

    // 服务费率
    @FinanceDataPoint
    protected Double serviceRate;

    //提前还款天数
    protected Integer prepaymentDays = 0;

    //还款日类型
    protected String repayDayType;

    //逾期罚息率（万倍）
    private Double overdueFineRate;

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Double serviceRate) {
        this.serviceRate = serviceRate;
    }

    public Integer getPrepaymentDays() {
        return prepaymentDays;
    }

    public void setPrepaymentDays(Integer prepaymentDays) {
        this.prepaymentDays = prepaymentDays;
    }

    public String getRepayDayType() {
        return repayDayType;
    }

    public void setRepayDayType(String repayDayType) {
        this.repayDayType = repayDayType;
    }

    public Double getOverdueFineRate() {
        return overdueFineRate;
    }

    public void setOverdueFineRate(Double overdueFineRate) {
        this.overdueFineRate = overdueFineRate;
    }

    public String getCustomerContractNumber() {
        return customerContractNumber;
    }

    public void setCustomerContractNumber(String customerContractNumber) {
        this.customerContractNumber = customerContractNumber;
    }

    @Override
    public String toString() {
        return "ContractVO{" +
                "contractId=" + contractId +
                ", contractCode='" + contractCode + '\'' +
                ", customerContractNumber='" + customerContractNumber + '\'' +
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