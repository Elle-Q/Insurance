package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.annotations.ImageUrl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class RequisitionVO extends BaseVO{

    private static final long serialVersionUID = -98261361835512417L;
    //id
    private Integer id;

    //业务单号
    private String requisitionNumber;

    //渠道id
    private Integer channelId;

    //渠道编号
    private String channelCode;

    //渠道名称
    private String channelName;

    //渠道用户的id
    private Integer channelUserId;

    //渠道用户姓名
    private String channelUserName;

    //渠道用户手机号码
    private String channelUserMobile;

    //合同编号
    private String contractNumber;

    //产品类型
    private String productType;

    //产品名称
    private Integer productId;

    //产品名称
    private String productName;

    //客户id
    private Integer customerId;

    //客户AccountInfoId
    private Integer customerAccountInfoId;

    //客户名称
    private String customerName;

    //客户手机号码
    private String customerMobile;

    //借款开始时间
    private Date borrowStartTime;

    //客户公司名称
    private String enterpriseName;

    //借款结束时间
    private Date borrowEndTime;

    //提交开始时间
    private Date submmitStartTime;

    //提交结束时间
    private Date submmitEndTime;

    //订单状态
    private String requisitionStatus;

    //审核状态
    private String auditStatus;

    //操作备注
    private String operationRemark;

    //借款金额
    @FinanceDataPoint
    private Double borrowAmount;

    //服务费率
    @FinanceDataPoint
    private Double serviceFeeRate;

    //其他费用的费率
    @FinanceDataPoint
    private Double otherFeeRate;

    //提前还款罚息率
    @FinanceDataPoint
    private Double prepaymentPenaltyRate;

    //逾期罚息率
    @FinanceDataPoint
    private Double overdueFineRate;

    //提前还款天数
    private Integer prepaymentDays;

    //客户指定的分期数
    private Integer businessDuration;

    //最大逾期天数
    private Integer maxOverdueDays;

    // 支付订单号
    private String paymentOrderNumber;

    //服务费
    @FinanceDataPoint
    private Double serviceFee;

    //提交时间
    private Date submmitTime;

    //交强险+车船税比重
    private BigDecimal percentage;

    // 放款账户
    private String loanAccountNumber;

    private String[] paymentVoucher;

    // 商业保险总价值
    @FinanceDataPoint
    private Double totalCommercialAmount;

    // 交强险总价值
    @FinanceDataPoint
    private Double totalCompulsoryAmount;

    // 车船税总价值
    @FinanceDataPoint
    private Double totalTaxAmount;

    // 总订单金额
    @FinanceDataPoint
    private Double totalApplyAmount;

    //审核通过时间
    private Date auditSuccessTime;

    // 审核批次号
    private String latestAuditBatch;

    //还款日类型
    private String repayDayType;

    //放款帐户类型
    private String loanAccountType;

    //放款帐户银行编码
    private String loanAccountBank;

    //放款银行名称
    private String loanAccountBankBranch;

    //放款帐户名称型
    private String loanAccountName;
    // 保险公司名称
    private String insuranceCompanyName;
    // 保险公司支部名称
    private String insuranceBranchName;

    //放款时间
    private Date loanTime;

    //是否为渠道人员录单
    private Boolean isChannelApplication;

    //还款类型
    private String repayType;

    //是否可以审核
    private Integer canAudit = 1;

    //是否为人工处理状态
    private Integer manualFlag = 0;

    //车辆数
    private Integer carNum = 0;

    //保单数
    private Integer insuranceNum = 0;

    //手持证件照
    @ImageUrl
    private String handheldCertificate;
    private String handheldCertificateUrl;
    private String handheldCertificateNarrowUrl;

    //其他材料
    @ImageUrl
    private String[] otherResource;

    private String[] otherResourceUrl;

    private String[] otherResourceNarrowUrl;

    // 合计应付金额
    @FinanceDataPoint
    private Double totalPayAmount;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHandheldCertificateUrl() {
        return handheldCertificateUrl;
    }

    public void setHandheldCertificateUrl(String handheldCertificateUrl) {
        this.handheldCertificateUrl = handheldCertificateUrl;
    }

    public String getHandheldCertificateNarrowUrl() {
        return handheldCertificateNarrowUrl;
    }

    public void setHandheldCertificateNarrowUrl(String handheldCertificateNarrowUrl) {
        this.handheldCertificateNarrowUrl = handheldCertificateNarrowUrl;
    }

    public String[] getOtherResourceUrl() {
        return otherResourceUrl;
    }

    public void setOtherResourceUrl(String[] otherResourceUrl) {
        this.otherResourceUrl = otherResourceUrl;
    }

    public String[] getOtherResourceNarrowUrl() {
        return otherResourceNarrowUrl;
    }

    public void setOtherResourceNarrowUrl(String[] otherResourceNarrowUrl) {
        this.otherResourceNarrowUrl = otherResourceNarrowUrl;
    }

    public String[] getOtherResource() {
        return otherResource;
    }

    public void setOtherResource(String[] otherResource) {
        this.otherResource = otherResource;
    }

    public String getHandheldCertificate() {
        return handheldCertificate;
    }

    public void setHandheldCertificate(String handheldCertificate) {
        this.handheldCertificate = handheldCertificate;
    }

    public Integer getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(Integer insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    public Integer getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Integer manualFlag) {
        this.manualFlag = manualFlag;
    }

    public Integer getCanAudit() {
        return canAudit;
    }

    public void setCanAudit(Integer canAudit) {
        this.canAudit = canAudit;
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

    public String getLatestAuditBatch() {
        return latestAuditBatch;
    }

    public void setLatestAuditBatch(String latestAuditBatch) {
        this.latestAuditBatch = latestAuditBatch;
    }

    public Date getAuditSuccessTime() {
        return auditSuccessTime;
    }

    public void setAuditSuccessTime(Date auditSuccessTime) {
        this.auditSuccessTime = auditSuccessTime;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
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

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public String getOperationRemark() {
        return operationRemark;
    }

    public void setOperationRemark(String operationRemark) {
        this.operationRemark = operationRemark;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Double getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(Double serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
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

    public Date getSubmmitStartTime() {
        return submmitStartTime;
    }

    public void setSubmmitStartTime(Date submmitStartTime) {
        this.submmitStartTime = submmitStartTime;
    }

    public Date getSubmmitEndTime() {
        return submmitEndTime;
    }

    public void setSubmmitEndTime(Date submmitEndTime) {
        this.submmitEndTime = submmitEndTime;
    }

    public Date getSubmmitTime() {
        return submmitTime;
    }

    public void setSubmmitTime(Date submmitTime) {
        this.submmitTime = submmitTime;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String[] getPaymentVoucher() {
        return paymentVoucher;
    }

    public void setPaymentVoucher(String[] paymentVoucher) {
        this.paymentVoucher = paymentVoucher;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Double getOtherFeeRate() {
        return otherFeeRate;
    }

    public void setOtherFeeRate(Double otherFeeRate) {
        this.otherFeeRate = otherFeeRate;
    }

    public Double getPrepaymentPenaltyRate() {
        return prepaymentPenaltyRate;
    }

    public void setPrepaymentPenaltyRate(Double prepaymentPenaltyRate) {
        this.prepaymentPenaltyRate = prepaymentPenaltyRate;
    }

    public Double getOverdueFineRate() {
        return overdueFineRate;
    }

    public void setOverdueFineRate(Double overdueFineRate) {
        this.overdueFineRate = overdueFineRate;
    }

    public Integer getPrepaymentDays() {
        return prepaymentDays;
    }

    public void setPrepaymentDays(Integer prepaymentDays) {
        this.prepaymentDays = prepaymentDays;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public Integer getMaxOverdueDays() {
        return maxOverdueDays;
    }

    public void setMaxOverdueDays(Integer maxOverdueDays) {
        this.maxOverdueDays = maxOverdueDays;
    }

    public String getPaymentOrderNumber() {
        return paymentOrderNumber;
    }

    public void setPaymentOrderNumber(String paymentOrderNumber) {
        this.paymentOrderNumber = paymentOrderNumber;
    }

    public Double getTotalCommercialAmount() {
        return totalCommercialAmount;
    }

    public void setTotalCommercialAmount(Double totalCommercialAmount) {
        this.totalCommercialAmount = totalCommercialAmount;
    }

    public Double getTotalCompulsoryAmount() {
        return totalCompulsoryAmount;
    }

    public void setTotalCompulsoryAmount(Double totalCompulsoryAmount) {
        this.totalCompulsoryAmount = totalCompulsoryAmount;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getTotalApplyAmount() {
        return totalApplyAmount;
    }

    public void setTotalApplyAmount(Double totalApplyAmount) {
        this.totalApplyAmount = totalApplyAmount;
    }

    public String getLoanAccountType() {
        return loanAccountType;
    }

    public void setLoanAccountType(String loanAccountType) {
        this.loanAccountType = loanAccountType;
    }

    public String getLoanAccountBank() {
        return loanAccountBank;
    }

    public void setLoanAccountBank(String loanAccountBank) {
        this.loanAccountBank = loanAccountBank;
    }

    public String getLoanAccountBankBranch() {
        return loanAccountBankBranch;
    }

    public void setLoanAccountBankBranch(String loanAccountBankBranch) {
        this.loanAccountBankBranch = loanAccountBankBranch;
    }

    public String getLoanAccountName() {
        return loanAccountName;
    }

    public void setLoanAccountName(String loanAccountName) {
        this.loanAccountName = loanAccountName;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Boolean getChannelApplication() {
        return isChannelApplication;
    }

    public void setChannelApplication(Boolean channelApplication) {
        isChannelApplication = channelApplication;
    }

    public Integer getCustomerAccountInfoId() {
        return customerAccountInfoId;
    }

    public void setCustomerAccountInfoId(Integer customerAccountInfoId) {
        this.customerAccountInfoId = customerAccountInfoId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Double getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(Double totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getInsuranceBranchName() {
        return insuranceBranchName;
    }

    public void setInsuranceBranchName(String insuranceBranchName) {
        this.insuranceBranchName = insuranceBranchName;
    }

    @Override
    public String toString() {
        return "RequisitionVO{" +
                "id=" + id +
                ", requisitionNumber='" + requisitionNumber + '\'' +
                ", channelId=" + channelId +
                ", channelCode='" + channelCode + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelUserId=" + channelUserId +
                ", channelUserName='" + channelUserName + '\'' +
                ", channelUserMobile='" + channelUserMobile + '\'' +
                ", contractNumber='" + contractNumber + '\'' +
                ", productType='" + productType + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", customerId=" + customerId +
                ", customerAccountInfoId=" + customerAccountInfoId +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", borrowStartTime=" + borrowStartTime +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", borrowEndTime=" + borrowEndTime +
                ", submmitStartTime=" + submmitStartTime +
                ", submmitEndTime=" + submmitEndTime +
                ", requisitionStatus='" + requisitionStatus + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                ", operationRemark='" + operationRemark + '\'' +
                ", borrowAmount=" + borrowAmount +
                ", serviceFeeRate=" + serviceFeeRate +
                ", otherFeeRate=" + otherFeeRate +
                ", prepaymentPenaltyRate=" + prepaymentPenaltyRate +
                ", overdueFineRate=" + overdueFineRate +
                ", prepaymentDays=" + prepaymentDays +
                ", businessDuration=" + businessDuration +
                ", maxOverdueDays=" + maxOverdueDays +
                ", paymentOrderNumber='" + paymentOrderNumber + '\'' +
                ", serviceFee=" + serviceFee +
                ", submmitTime=" + submmitTime +
                ", percentage=" + percentage +
                ", loanAccountNumber='" + loanAccountNumber + '\'' +
                ", paymentVoucher=" + Arrays.toString(paymentVoucher) +
                ", totalCommercialAmount=" + totalCommercialAmount +
                ", totalCompulsoryAmount=" + totalCompulsoryAmount +
                ", totalTaxAmount=" + totalTaxAmount +
                ", totalApplyAmount=" + totalApplyAmount +
                ", auditSuccessTime=" + auditSuccessTime +
                ", latestAuditBatch='" + latestAuditBatch + '\'' +
                ", repayDayType='" + repayDayType + '\'' +
                ", loanAccountType='" + loanAccountType + '\'' +
                ", loanAccountBank='" + loanAccountBank + '\'' +
                ", loanAccountBankBranch='" + loanAccountBankBranch + '\'' +
                ", loanAccountName='" + loanAccountName + '\'' +
                ", insuranceCompanyName='" + insuranceCompanyName + '\'' +
                ", insuranceBranchName='" + insuranceBranchName + '\'' +
                ", loanTime=" + loanTime +
                ", isChannelApplication=" + isChannelApplication +
                ", repayType='" + repayType + '\'' +
                ", canAudit=" + canAudit +
                ", manualFlag=" + manualFlag +
                ", carNum=" + carNum +
                ", insuranceNum=" + insuranceNum +
                ", handheldCertificate='" + handheldCertificate + '\'' +
                ", otherResource=" + Arrays.toString(otherResource) +
                ", totalPayAmount=" + totalPayAmount +
                '}';
    }
}
