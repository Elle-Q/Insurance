package com.fintech.insurance.micro.biz.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 业务申请单
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-11-13 17:04:30
 */
@Entity
@Table(name = "busi_requisition")
public class Requisition extends BaseEntity implements Cloneable {

    private static final long serialVersionUID = -8820461361835512414L;
    @Column(name = "customer_id", columnDefinition = "int(10) unsigned not null comment '申请业务的客户id，关联customer_account表的主键'")
    private Integer customerId;

    @Column(name = "customer_account_info_id", columnDefinition = "int(10) comment '申请业务的客户账户id，关联customer_account_info表的主键'")
    private Integer customerAccountInfoId = 0;

    @ManyToOne
    @JoinColumn(name = "channel_id", columnDefinition = "int(10) unsigned not null comment '渠道id，关联sys_channel表的主键'")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "product_id", columnDefinition = "int(10) unsigned not null comment '产品id，关联busi_product表的主键'")
    private Product product;

    @Column(name = "product_type", columnDefinition = "varchar(16) not null comment '产品类型'")
    private String productType;

    @Column(name = "is_channel_application", columnDefinition = "boolean not null default 1 comment '是否为渠道人员录单'")
    private Boolean isChannelApplication;

    @Column(name = "channel_user_id", columnDefinition = "int(10) unsigned comment '渠道用户id，关联sys_user表中的主键'")
    private Integer channelUserId;

    @Column(name = "requisition_status", columnDefinition = "varchar(16) not null")
    private String requisitionStatus;

    @Column(name = "requisition_number", columnDefinition = "varchar(64) not null comment '订单号'")
    private String requisitionNumber;

    //还款方式  principal_interest:等额本息")
    @Column(name = "repay_type", length = 16, columnDefinition = "varchar(16) comment '还款类型'")
    private String repayType;

    //还款日类型 initial_payment：期初还款，final_payment：期末还款")
    @Column(name = "repay_day_type", length = 16, columnDefinition = "varchar(16) comment '还款日类型'")
    private String repayDayType;

    //服务费率（万倍）
    @Column(name = "service_fee_rate", columnDefinition = "double comment '服务费率（万倍）'")
    private Double serviceFeeRate;

    //其他费用的费率（万倍）
    @Column(name = "other_fee_rate", columnDefinition = "double comment '其他费用的费率（万倍）'")
    private Double otherFeeRate;

    //提前还款罚息率（万倍）
    @Column(name = "prepayment_penalty_rate", columnDefinition = "double comment '提前还款罚息率（万倍）'")
    private Double prepaymentPenaltyRate;

    //提前还款天数
    @Column(name = "prepayment_days", columnDefinition = "int comment '提前还款天数'")
    private Integer prepaymentDays;

    //逾期罚息率（万倍）
    @Column(name = "overdue_fine_rate", columnDefinition = "double comment '逾期罚息率（万倍）'")
    private Double overdueFineRate;

    //最大逾期天数
    @Column(name = "max_overdue_days", columnDefinition = "int comment '最大逾期天数'")
    private Integer maxOverdueDays;

    @Column(name = "payment_order_number", columnDefinition = "varchar(64) comment '支付订单号'")
    private String paymentOrderNumber;

    @Column(name = "total_commercial_amount", columnDefinition = "bigint not null comment '商业保险总价值，单位为分'")
    private Integer totalCommercialAmount = 0;

    @Column(name = "total_compulsory_amount", columnDefinition = "bigint not null comment '交强险总价值'")
    private Integer totalCompulsoryAmount = 0;

    @Column(name = "total_tax_amount", columnDefinition = "bigint not null comment '车船税总价值'")
    private Integer totalTaxAmount = 0;

    @Column(name = "total_apply_amount", columnDefinition = "bigint not null comment '总订单金额，单位为分'")
    private Integer totalApplyAmount = 0;

    @Column(name = "business_duration", columnDefinition = "int unsigned not null default 0 comment '客户指定的分期数，0则不指定，自动匹配'")
    private Integer businessDuration;

    @Column(name = "submission_date", columnDefinition = "datetime comment '提交日期'")
    private Date submissionDate;

    @Column(name = "latest_audit_batch", columnDefinition = "varchar(32) comment '最近提交审核的批次号'")
    private String latestAuditBatch;

    @Column(name = "loan_account_type", columnDefinition = "varchar(16) not null comment '放款帐户类型'")
    private String loanAccountType;

    @Column(name = "loan_account_number", columnDefinition = "varchar(32) comment '放款帐户号'")
    private String loanAccountNumber;

    @Column(name = "loan_account_bank", columnDefinition = "varchar(16) comment '放款帐户银行编码'")
    private String loanAccountBank;

    @Column(name = "loan_account_bank_branch", columnDefinition = "varchar(128) comment '放款银行名称'")
    private String loanAccountBankBranch;

    @Column(name = "loan_account_name", columnDefinition = "varchar(256) comment '放款帐户名称型'")
    private String loanAccountName;

    @Column(name = "insurance_company_name", columnDefinition = "varchar(256) comment '保险公司名称'")
    private String insuranceCompanyName;

    @Column(name = "insurance_branch_name", columnDefinition = "varchar(256) comment '保险公司支部名称'")
    private String insuranceBranchName;

    @Column(name = "loan_time", columnDefinition = "datetime comment '放款时间'")
    private Date loanTime;

    @Column(name = "audit_success_time", columnDefinition = "datetime comment '审核通过时间'")
    private Date auditSuccessTime;

    @Column(name = "other_resource", columnDefinition = "text comment '其他材料资源'")
    private String otherResource;


    @OneToMany(mappedBy = "requisition", fetch = FetchType.EAGER)
    private Set<RequisitionDetail> details;

    @Column(name = "created_by", columnDefinition = "int comment '创建者'")
    private Integer createBy;

    //更新者
    @Column(name = "updated_by", columnDefinition = "int comment '更新者'")
    private Integer updateBy;

    @Column(name = "id_card_evidence", columnDefinition = "String comment '身份证手持照片'")
    private String idCardEvidence;


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerAccountInfoId() {
        return customerAccountInfoId;
    }

    public void setCustomerAccountInfoId(Integer customerAccountInfoId) {
        this.customerAccountInfoId = customerAccountInfoId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Boolean getChannelApplication() {
        return isChannelApplication;
    }

    public void setChannelApplication(Boolean channelApplication) {
        isChannelApplication = channelApplication;
    }

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
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

    public Double getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(Double serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
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

    public Integer getPrepaymentDays() {
        return prepaymentDays;
    }

    public void setPrepaymentDays(Integer prepaymentDays) {
        this.prepaymentDays = prepaymentDays;
    }

    public Double getOverdueFineRate() {
        return overdueFineRate;
    }

    public void setOverdueFineRate(Double overdueFineRate) {
        this.overdueFineRate = overdueFineRate;
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

    public Integer getTotalCommercialAmount() {
        return totalCommercialAmount;
    }

    public void setTotalCommercialAmount(Integer totalCommercialAmount) {
        this.totalCommercialAmount = totalCommercialAmount;
    }

    public Integer getTotalCompulsoryAmount() {
        return totalCompulsoryAmount;
    }

    public void setTotalCompulsoryAmount(Integer totalCompulsoryAmount) {
        this.totalCompulsoryAmount = totalCompulsoryAmount;
    }

    public Integer getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Integer totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Integer getTotalApplyAmount() {
        return totalApplyAmount;
    }

    public void setTotalApplyAmount(Integer totalApplyAmount) {
        this.totalApplyAmount = totalApplyAmount;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getLatestAuditBatch() {
        return latestAuditBatch;
    }

    public void setLatestAuditBatch(String latestAuditBatch) {
        this.latestAuditBatch = latestAuditBatch;
    }

    public String getLoanAccountType() {
        return loanAccountType;
    }

    public void setLoanAccountType(String loanAccountType) {
        this.loanAccountType = loanAccountType;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
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

    public Date getAuditSuccessTime() {
        return auditSuccessTime;
    }

    public void setAuditSuccessTime(Date auditSuccessTime) {
        this.auditSuccessTime = auditSuccessTime;
    }

    public String getOtherResource() {
        return otherResource;
    }

    public void setOtherResource(String otherResource) {
        this.otherResource = otherResource;
    }

    public Set<RequisitionDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<RequisitionDetail> details) {
        this.details = details;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getIdCardEvidence() {
        return idCardEvidence;
    }

    public void setIdCardEvidence(String idCardEvidence) {
        this.idCardEvidence = idCardEvidence;
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
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
