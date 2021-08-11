package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 13:41
 */
public class EnterpriseBankVO implements Serializable {
    // 企业编号
    private String enterpriseCode;
    // 应用类型（DEBT扣款、VERIFY验证）
    private String applicationCode;
    // 内部的银行code，可能不同的支付平台对同一银行返回的bankcode不同，这里的code是为了统一不同平台对同一银行返回的不同code
    private String appBankCode;
    // 支付平台返回的bankcode
    private String enterpriseBankCode;
    // 银行编号
    private String bankName;
    // 备注
    private String remark;
    // 单笔扣款限额
    @FinanceDataPoint
    private Double singleLimit;
    // 每日扣款限额
    @FinanceDataPoint
    private Double dailyLimit;

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getAppBankCode() {
        return appBankCode;
    }

    public void setAppBankCode(String appBankCode) {
        this.appBankCode = appBankCode;
    }

    public String getEnterpriseBankCode() {
        return enterpriseBankCode;
    }

    public void setEnterpriseBankCode(String enterpriseBankCode) {
        this.enterpriseBankCode = enterpriseBankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(Double singleLimit) {
        this.singleLimit = singleLimit;
    }

    public Double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}
