package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 11:43
 */
@Entity
@Table(name = "finance_enterprise_bank")
public class EnterpriseBank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "int(11) not null auto_increment comment '主键'")
    private Integer id;
    @Column(name = "enterprise_code", columnDefinition = "varchar(64) not null comment '银行服务企业编码'")
    private String enterpriseCode;
    @Column(name = "application_code", columnDefinition = "varchar(64) not null comment '应用类型编码: 如代扣、 代付等'")
    private String applicationCode;
    // 内部的银行code，可能不同的支付平台对同一银行返回的bankcode不同，这里的code是为了统一不同平台对同一银行返回的不同code
    @Column(name = "app_bank_code", columnDefinition = "varchar(64) not null comment '平台内部银行代码'")
    private String appBankCode;
    // 支付平台返回bankcode
    @Column(name = "enterprise_bank_code", columnDefinition = "varchar(64) not null comment '企业银行银行代码'")
    private String enterpriseBankCode;
    @Column(name = "bank_name", columnDefinition = "varchar(256)")
    private String bankName;
    @Column(name = "remark", columnDefinition = "varchar(256)")
    private String remark;
    @Column(name = "single_limit", columnDefinition = "bigint(20) unsigned")
    private BigDecimal singleLimit;
    @Column(name = "daily_limit", columnDefinition = "bigint(20) unsigned")
    private BigDecimal dailyLimit;
    @Column(name = "disable_flag", columnDefinition = "boolean not null default 0")
    private Boolean disableFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Boolean getDisableFlag() {
        return disableFlag;
    }

    public void setDisableFlag(Boolean disableFlag) {
        this.disableFlag = disableFlag;
    }
}
