package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 保险公司信息表
 */
@Entity
@Table(name = "data_insurance_company")
public class InsuranceCompany extends BaseEntity implements Serializable {

    @Column(name = "company_name", columnDefinition = "varchar(256) comment '保险公司名称'")
    private String companyName;

    @Column(name = "contact_name", columnDefinition = "varchar(64) comment '保险公司对接人姓名'")
    private String contactName;

    @Column(name = "contact_phone", columnDefinition = "varchar(32) comment '保险公司对接人电话号码'")
    private String contactPhone;

    @Column(name = "account_bank", columnDefinition = "varchar(16) comment '保险公司保费账户开户行编码'")
    private String accountBank;

    @Column(name = "account_bank_branch", columnDefinition = "varchar(128) comment '保险公司保费账户开户行支行'")
    private String accountBankBranch;

    @Column(name = "account_number", columnDefinition = "varchar(32) comment '保险公司保费账户'")
    private String accountNumber;

    @Column(name = "account_name", columnDefinition = "varchar(256) comment '保险公司保费账户户名'")
    private String accountName;

    @Column(name = "is_account_company", columnDefinition = "boolean comment '是否公司级保费账户，为是时该表的保费账户相关字段都不能为空'")
    private Boolean accountCompanyFlag = false;

    @Column(name = "is_disabled", columnDefinition = "boolean comment '是否被禁用，被禁用后则不能开展该保险公司的业务'")
    private Boolean disabledFlag = false;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    @OneToMany(mappedBy = "insuranceCompany", fetch = FetchType.EAGER)
    private Set<InsuranceBranch> insuranceBranches = new HashSet<>();

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Boolean getAccountCompanyFlag() {
        return accountCompanyFlag;
    }

    public void setAccountCompanyFlag(Boolean accountCompanyFlag) {
        this.accountCompanyFlag = accountCompanyFlag;
    }

    public Boolean getDisabledFlag() {
        return disabledFlag;
    }

    public void setDisabledFlag(Boolean disabledFlag) {
        this.disabledFlag = disabledFlag;
    }

    public Set<InsuranceBranch> getInsuranceBranches() {
        Set<InsuranceBranch> set = new HashSet<>();

        for (InsuranceBranch branch : this.insuranceBranches) {
            if (!branch.getDisabledFlag()) {
                set.add(branch);
            }
        }
        return set;
    }

    public void setInsuranceBranches(Set<InsuranceBranch> insuranceBranches) {
        if (insuranceBranches != null && insuranceBranches.size() == 0) {
            this.insuranceBranches = insuranceBranches;
        }
    }
}
