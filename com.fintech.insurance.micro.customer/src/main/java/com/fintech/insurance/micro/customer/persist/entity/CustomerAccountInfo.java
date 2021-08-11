package com.fintech.insurance.micro.customer.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/11 14:06
 */
@Entity
@Table(name = "cust_account_info")
public class CustomerAccountInfo extends BaseEntity {
    @Column(name = "channel_code", columnDefinition = "varchar(32) not null comment '渠道编码，该客户的来源渠道'")
    private String channelCode;

    @Column(name = "customer_name", columnDefinition = "varchar(64) not null comment '客户姓名'")
    private String customerName;

    @Column(name = "customer_mobile", columnDefinition = "varchar(15) not null comment '客户手机号码'")
    private String customerMobile;

    @Column(name = "account_number", columnDefinition = "varchar(32) not null comment '银行账户号'")
    private String accountNumber;

    @Column(name = "account_bank_name", columnDefinition = "varchar(128) comment '银行帐户开户行名称'")
    private String accountBankName;

    @Column(name = "account_bank_branch", columnDefinition = "varchar(128) comment '银行帐户开户行支行'")
    private String accountBankBranch;

    @Column(name = "bank_card_picture", columnDefinition = "varchar(32) not null comment '银行卡正面照'")
    private String bankCardPicture;

    @Column(name = "enterprise_name", columnDefinition = "varchar(128) comment '企业名称'")
    private String enterpriseName;

    @Column(name = "business_licence", columnDefinition = "varchar(64) comment '营业执照号码'")
    private String businessLicence;

    @Column(name = "business_licence_picture", columnDefinition = "varchar(32) comment '营业执照照片'")
    private String businessLicencePicture;

    @Column(name = "is_enterprise_default", columnDefinition = "boolean not null default 0 comment '是否为企业默认信息'")
    private Boolean enterpriseDefault;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private CustomerAccount customerAccount;

    @Column(name = "channel_user_id", columnDefinition = "int(10) comment '渠道用户id system_user 表'")
    private Integer channelUserId;

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getAccountBankBranch() {
        return accountBankBranch;
    }

    public void setAccountBankBranch(String accountBankBranch) {
        this.accountBankBranch = accountBankBranch;
    }

    public String getBankCardPicture() {
        return bankCardPicture;
    }

    public void setBankCardPicture(String bankCardPicture) {
        this.bankCardPicture = bankCardPicture;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getBusinessLicencePicture() {
        return businessLicencePicture;
    }

    public void setBusinessLicencePicture(String businessLicencePicture) {
        this.businessLicencePicture = businessLicencePicture;
    }

    public Boolean getEnterpriseDefault() {
        return enterpriseDefault;
    }

    public void setEnterpriseDefault(Boolean enterpriseDefault) {
        this.enterpriseDefault = enterpriseDefault;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }
}
