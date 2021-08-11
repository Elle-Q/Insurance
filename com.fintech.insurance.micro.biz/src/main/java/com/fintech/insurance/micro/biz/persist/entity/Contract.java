package com.fintech.insurance.micro.biz.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-11-13 17:04:30
 */
@Entity
@Table(name = "busi_contract")
public class Contract extends BaseEntity {

    private static final long serialVersionUID = -8820461361835512412L;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_id", columnDefinition = "int(11) unsigned not null comment '业务申请单'")
    private Requisition requisition;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", columnDefinition = "int(11) unsigned not null comment '关联渠道'")
    private Channel channel;

    @Column(name = "customer_id", columnDefinition = "int(10) unsigned not null comment '申请业务的客户id，关联customer_account表的主键'")
    private Integer customerId;

    @Column(name = "customer_contract_number", columnDefinition = "varchar(64) not null comment '正式的合同号, 在用户支付服务单成功之后生成'")
    private String customerContractNumber;

    @Column(name = "contract_number", columnDefinition = "varchar(64) comment '用于内部关联的合同号'")
    private String contractNumber;

    @Column(name = "content_file", columnDefinition = "varchar(32) comment '借款合同内容文件，转存七牛之后的文件id'")
    private String contentShowFile;

    @Column(name = "content_signed_doc", columnDefinition = "varchar(32) comment '借款合同内容原始文件PDF，转存七牛之后的文件id'")
    private String contentOriginFile;

    @Column(name = "bestsign_file", columnDefinition = "varchar(64) comment '上上签文件id'")
    private String bestsignFile;

    @Column(name = "service_content_file", columnDefinition = "varchar(32) comment '服务合同内容文件，转存七牛之后的文件id'")
    private String serviceContentShowFile;

    @Column(name = "service_signed_doc", columnDefinition = "varchar(32) comment '服务合同内容原始文件PDF，转存七牛之后的文件id'")
    private String serviceContentOriginFile;

    @Column(name = "service_bestsign_file", columnDefinition = "varchar(64) comment '转存七牛之后的文件id'")
    private String serviceBestSignFile;

    @Column(name = "contract_amount", columnDefinition = "bigint unsigned not null")
    private BigDecimal contractAmount;

    //计息类型，按月或者按日计息")
    @Column(name = "interest_type", length = 16, columnDefinition = "varchar(16) comment '计息类型，按月或者按日计息'")
    private String interestType;

    @Column(name = "interest_rate", columnDefinition = "double comment '利率点（万倍）'")
    private Double interestRate;

    @Column(name = "loan_ratio", columnDefinition = "double comment '可借比例（万倍）'")
    private Double loanRatio;

    @Column(name = "business_duration", columnDefinition = "int unsigned not null comment '合同的借款期数'")
    private Integer businessDuration;

    @Column(name = "contract_status", columnDefinition = "varchar(16) not null comment '合同状态'")
    private String contractStatus;

    @Column(name = "start_date", columnDefinition = "date not null comment '借款日开始日期'")
    private Date startDate;

    @Column(name = "end_date", columnDefinition = "date not null comment '借款日终止日期'")
    private Date endDate;

    public String getServiceBestSignFile() {
        return serviceBestSignFile;
    }

    public void setServiceBestSignFile(String serviceBestSignFile) {
        this.serviceBestSignFile = serviceBestSignFile;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public void setRequisition(Requisition requisition) {
        this.requisition = requisition;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerContractNumber() {
        return customerContractNumber;
    }

    public void setCustomerContractNumber(String serialNumber) {
        this.customerContractNumber = serialNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContentShowFile() {
        return contentShowFile;
    }

    public void setContentShowFile(String contentFile) {
        this.contentShowFile = contentFile;
    }

    public String getBestsignFile() {
        return bestsignFile;
    }

    public void setBestsignFile(String bestsignFile) {
        this.bestsignFile = bestsignFile;
    }

    public String getServiceContentShowFile() {
        return serviceContentShowFile;
    }

    public void setServiceContentShowFile(String serviceContentShowFile) {
        this.serviceContentShowFile = serviceContentShowFile;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Double loanRatio) {
        this.loanRatio = loanRatio;
    }

    public String getContentOriginFile() {
        return contentOriginFile;
    }

    public void setContentOriginFile(String contentOriginFile) {
        this.contentOriginFile = contentOriginFile;
    }

    public String getServiceContentOriginFile() {
        return serviceContentOriginFile;
    }

    public void setServiceContentOriginFile(String serviceContentOriginFile) {
        this.serviceContentOriginFile = serviceContentOriginFile;
    }
}
