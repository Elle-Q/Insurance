package com.fintech.insurance.micro.dto.finance;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/12 19:41
 */
public class VerifyUserVO {

    private String customerName;

    private String certNo;

    private String bankCardNo;

    private String mobileNo;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
