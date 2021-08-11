package com.fintech.insurance.micro.dto.customer;


import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Man liu
 * @Description: 客户账户VO
 * @Date: 2017/11/9 16:51
 */
public class CustomerAccountInfoVO implements Serializable {

    // 客户账户id
    private Integer id;

    // 客户id
    private Integer accountId;

    // 客户银行卡id
    private Integer customerBankCardId;

    //企业名称
    private String enterpriseName;

    //营业执照号码
    private String businessLicence;

    //营业执照照片
    private String businessLicencePicture;

    //渠道编码，该客户的来源渠道
    private String channelCode;

    //客户姓名
    private String customerName;

    //身份证号码，全局唯一
    private String idNumber;

    //银行账户号
    private String accountNumber;

    //客户手机号码
    private String customerMobile;

    //银行帐户开户行名称
    private String accountBankName;

    //银行帐户开户行支行
    private String accountBankBranch;

    //身份证正面照片
    private String idFront;

    //身份证反面照片
    private String idBack;

    //银行卡正面照
    private String bankCardPicture;

    //是否为企业默认信息
    private Boolean enterpriseDefault;

    //是否冻结标志位
    private Boolean lockedTag;

    //冻结时间
    private Date lockedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCustomerBankCardId() {
        return customerBankCardId;
    }

    public void setCustomerBankCardId(Integer customerBankCardId) {
        this.customerBankCardId = customerBankCardId;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
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

    public String getIdFront() {
        return idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public String getBankCardPicture() {
        return bankCardPicture;
    }

    public void setBankCardPicture(String bankCardPicture) {
        this.bankCardPicture = bankCardPicture;
    }

    public Boolean getEnterpriseDefault() {
        return enterpriseDefault;
    }

    public void setEnterpriseDefault(Boolean enterpriseDefault) {
        this.enterpriseDefault = enterpriseDefault;
    }

    public Boolean getLockedTag() {
        return lockedTag;
    }

    public void setLockedTag(Boolean lockedTag) {
        this.lockedTag = lockedTag;
    }

    public Date getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Date lockedAt) {
        this.lockedAt = lockedAt;
    }
}
