package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/8 14:12
 */
public class InstalmentDetailVO implements Serializable {
    // 合同id
    private Integer contractId;
    // 合同编号
    private String contractNumber;
    // 车牌号
    private String carNumber;
    // 车辆数
    private Integer carCount;
    // 合同pdf跳转地址
    private String contractUrl;
    // 服务合同
    private String serviceUrl;
    // 申请单id
    private Integer requisitionId;
    // 申请单号
    private String requisitionNumber;
    // 渠道名称
    private String channelName;
    // 还款状态
    private String refundStatus;
    // 合同状态
    private String contractStatus;
    // 总期数
    private Integer totalInstalment;
    // 当前期数
    private Integer currentInstalment;
    // 合同申请金额
    @FinanceDataPoint
    private Double repayTotalAmount;
    // 还款日
    private Date repayDate;
    // 逾期标志
    private Boolean overdueFlag;
    // 逾期天数
    private long overdueDays;
    // 剩余还款金额
    @FinanceDataPoint
    private Double surplusAmount;
    // 分期开始时间
    private Date instalmentBeginDate;
    // 分期结束时间
    private Date instalmentEndDate;
    // 总保单数
    private Integer totalInsurance;
    // 渠道标号
    private String channelCode;
    // 放款时间
    private Date loanTime;
    // 还款方式
    private String repayType;
    // 还款日类型
    private String repayDayType;
    // 用户id
    private Integer customerId;
    // 企业名称
    private String enterpriseName;

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
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

    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Date getInstalmentBeginDate() {
        return instalmentBeginDate;
    }

    public void setInstalmentBeginDate(Date instalmentBeginDate) {
        this.instalmentBeginDate = instalmentBeginDate;
    }

    public Date getInstalmentEndDate() {
        return instalmentEndDate;
    }

    public void setInstalmentEndDate(Date instalmentEndDate) {
        this.instalmentEndDate = instalmentEndDate;
    }

    public Integer getTotalInsurance() {
        return totalInsurance;
    }

    public void setTotalInsurance(Integer totalInsurance) {
        this.totalInsurance = totalInsurance;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getRepayDayType() {
        return repayDayType;
    }

    public void setRepayDayType(String repayDayType) {
        this.repayDayType = repayDayType;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
