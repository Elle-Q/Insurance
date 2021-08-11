package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.ImageUrl;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/27 15:24
 */
public class CustVO {
    // 客户id
    private Integer customerId;
    // 客户名称
    private String customerName;
    // 身份证号
    private String customerIdNumber;
    // 银行卡号
    private String customerBankCardNumber;
    // 银行卡正面照
    @ImageUrl
    private String customerBankCardFront;
    // 银行卡正面照url
    private String customerBankCardFrontUrl;
    // 银行卡正面照缩略图url
    private String customerBankCardFrontNarrowUrl;
    // 手机号
    private String customerMobile;
    // 身份证正面照
    @ImageUrl
    private String customerIdFront;
    // 身份证正面照url
    private String customerIdFrontUrl;
    // 身份证正面照缩略图url
    private String customerIdFrontNarrowUrl;
    // 身份证反面照
    @ImageUrl
    private String customerIdBack;
    // 身份证反面照url
    private String customerIdBackUrl;
    // 身份证反面照缩略图url
    private String customerIdBackNarrowUrl;
    // 手持身份证照片
    @ImageUrl
    private String idCardEvidence;
    // 手持身份证照片url
    private String idCardEvidenceUrl;
    // 手持身份证照片缩略图url
    private String idCardEvidenceNarrowUrl;

    public String getCustomerBankCardFrontUrl() {
        return customerBankCardFrontUrl;
    }

    public void setCustomerBankCardFrontUrl(String customerBankCardFrontUrl) {
        this.customerBankCardFrontUrl = customerBankCardFrontUrl;
    }

    public String getCustomerBankCardFrontNarrowUrl() {
        return customerBankCardFrontNarrowUrl;
    }

    public void setCustomerBankCardFrontNarrowUrl(String customerBankCardFrontNarrowUrl) {
        this.customerBankCardFrontNarrowUrl = customerBankCardFrontNarrowUrl;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    public String getCustomerBankCardNumber() {
        return customerBankCardNumber;
    }

    public void setCustomerBankCardNumber(String customerBankCardNumber) {
        this.customerBankCardNumber = customerBankCardNumber;
    }

    public String getCustomerBankCardFront() {
        return customerBankCardFront;
    }

    public void setCustomerBankCardFront(String customerBankCardFront) {
        this.customerBankCardFront = customerBankCardFront;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerIdFront() {
        return customerIdFront;
    }

    public void setCustomerIdFront(String customerIdFront) {
        this.customerIdFront = customerIdFront;
    }

    public String getCustomerIdBack() {
        return customerIdBack;
    }

    public void setCustomerIdBack(String customerIdBack) {
        this.customerIdBack = customerIdBack;
    }

    public String getIdCardEvidence() {
        return idCardEvidence;
    }

    public void setIdCardEvidence(String idCardEvidence) {
        this.idCardEvidence = idCardEvidence;
    }

    public String getCustomerIdFrontUrl() {
        return customerIdFrontUrl;
    }

    public void setCustomerIdFrontUrl(String customerIdFrontUrl) {
        this.customerIdFrontUrl = customerIdFrontUrl;
    }

    public String getCustomerIdFrontNarrowUrl() {
        return customerIdFrontNarrowUrl;
    }

    public void setCustomerIdFrontNarrowUrl(String customerIdFrontNarrowUrl) {
        this.customerIdFrontNarrowUrl = customerIdFrontNarrowUrl;
    }

    public String getCustomerIdBackUrl() {
        return customerIdBackUrl;
    }

    public void setCustomerIdBackUrl(String customerIdBackUrl) {
        this.customerIdBackUrl = customerIdBackUrl;
    }

    public String getCustomerIdBackNarrowUrl() {
        return customerIdBackNarrowUrl;
    }

    public void setCustomerIdBackNarrowUrl(String customerIdBackNarrowUrl) {
        this.customerIdBackNarrowUrl = customerIdBackNarrowUrl;
    }

    public String getIdCardEvidenceUrl() {
        return idCardEvidenceUrl;
    }

    public void setIdCardEvidenceUrl(String idCardEvidenceUrl) {
        this.idCardEvidenceUrl = idCardEvidenceUrl;
    }

    public String getIdCardEvidenceNarrowUrl() {
        return idCardEvidenceNarrowUrl;
    }

    public void setIdCardEvidenceNarrowUrl(String idCardEvidenceNarrowUrl) {
        this.idCardEvidenceNarrowUrl = idCardEvidenceNarrowUrl;
    }
}
