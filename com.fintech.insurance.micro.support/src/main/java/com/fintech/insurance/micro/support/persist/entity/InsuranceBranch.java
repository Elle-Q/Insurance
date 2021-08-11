package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 保险公司支部信息表
 */
@Entity
@Table(name = "data_insurance_branch")
public class InsuranceBranch extends BaseEntity implements Serializable {

    @Column(name = "branch_name", columnDefinition = "varchar(256) comment '分支机构名称'")
    private String branchName;

    @Column(name = "branch_address", columnDefinition = "varchar(256) comment '分支机构地址'")
    private String branchAddress;

    @Column(name = "contact_name", columnDefinition = "varchar(64) comment '对接人姓名'")
    private String contactName;

    @Column(name = "contact_phone", columnDefinition = "varchar(32) comment '对接人电话号码'")
    private String contactPhone;

    @Column(name = "account_bank", columnDefinition = "varchar(16) comment '保费账户开户行编码'")
    private String accountBank;

    @Column(name = "account_bank_branch", columnDefinition = "varchar(128) comment '保费账户开户行支行名称'")
    private String accountBankBranch;

    @Column(name = "account_number", columnDefinition = "varchar(32) comment '保费账户'")
    private String accountNumber;

    @Column(name = "account_name", columnDefinition = "varchar(256) comment '保费账户户名'")
    private String accountName;

    @Column(name = "is_disabled", columnDefinition = "boolean comment '是否被禁用，禁用后不能在该支部开展业务'")
    private Boolean disabledFlag = false;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", columnDefinition = "int(11) comment '外键标识，关联insurance_company表的id'")
    private InsuranceCompany insuranceCompany;

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

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountBankBranch() {
        return accountBankBranch;
    }

    public void setAccountBankBranch(String accountBankBranch) {
        this.accountBankBranch = accountBankBranch;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean getDisabledFlag() {
        return disabledFlag;
    }

    public void setDisabledFlag(Boolean disabledFlag) {
        this.disabledFlag = disabledFlag;
    }
}
