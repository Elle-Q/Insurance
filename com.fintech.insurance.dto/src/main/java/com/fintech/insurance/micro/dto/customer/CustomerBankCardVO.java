package com.fintech.insurance.micro.dto.customer;

import java.io.Serializable;

/**
 * 客户银行卡VO
 */
public class CustomerBankCardVO implements Serializable {

    private String accountBank;

    private String accountNumber;

    private String accountMobile;

    private Integer customerId;

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getAccountBank() {

        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }
}
