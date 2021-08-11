package com.fintech.insurance.micro.customer.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;

/**
 * @Author: Clayburn
 * @Description: 客户银行卡信息表
 * @Date: 2017/11/13 14:53
 */
@Entity
@Table(name = "cust_bank_card")
public class CustomerBankCard extends BaseEntity {
    @Column(name = "account_bank", columnDefinition = "varchar(16) not null")
    private String accountBank;

    @Column(name = "account_number", columnDefinition = "varchar(32) not null")
    private String accountNumber;

    @Column(name = "account_mobile", columnDefinition = "varchar(16) not null")
    private String accountMobile;

    @JoinColumn(name = "account_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private CustomerAccount customerAccount;

    @Column(name = "disable_flag", columnDefinition = "boolean not null default 0")
    private Boolean disableFlag = false;

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountMobile() {
        return accountMobile;
    }

    public void setAccountMobile(String accountMobile) {
        this.accountMobile = accountMobile;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public Boolean getDisableFlag() {
        return disableFlag;
    }

    public void setDisableFlag(Boolean disableFlag) {
        this.disableFlag = disableFlag;
    }
}
