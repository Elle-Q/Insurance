package com.fintech.insurance.micro.vo.wechat;


import com.fintech.insurance.commons.annotations.ImageUrl;

import javax.validation.constraints.NotNull;

/**
 * 客户VO
 * @author qxy
 */
public class WeChatCustomerVO extends VerificationCodeVO {

    //客户info id 唯一
    private Integer customerAccountInfoId;

    //客户账户id
    private Integer customerId;

    //所属渠道
    private String channelCode;

    // 企业名称
    @NotNull(message = "106301")
    private String companyOf;

    //客户名称
    @NotNull(message = "106301")
    private String name;

    //营业执照
    @NotNull(message = "106301")
    private String businessLicence;

    //营业执照图片
    @NotNull(message = "106301")
    @ImageUrl
    private String licencePicture;

    //营业执照url
    public String licencePictureUrl;

    //营业执照缩略图url
    public String licencePictureNarrowUrl;

    //身份证号
    @NotNull(message = "106301")
    private String idNum;

    //银行卡号
    @NotNull(message = "106301")
    private String bankCard;

    //手机号码
    @NotNull(message = "106301")
    private String phone;

    //身份证正面照
    @NotNull(message = "106301")
    @ImageUrl
    private String idFront;

    //身份证正面照url
    public String idFrontUrl;

    //身份证正面照缩略图url
    public String idFrontNarrowUrl;

    //身份证反面照
    @ImageUrl
    @NotNull(message = "106301")
    private String idBack;

    //身份证反面照url
    public String idBackUrl;

    //身份证反面照缩略图url
    public String idBackNarrowUrl;

    //银行卡正面照
    @NotNull(message = "106301")
    @ImageUrl
    private String bankCardPicture;

    //银行卡正面照url
    public String bankCardPictureUrl;

    //银行卡正面照缩略图url
    public String bankCardPictureNarrowUrl;

    //是否冻结标志位
    private Integer isLocked;

    //渠道用户
    private Integer channelUserId;

    //token
    private String token;

    private long tokenExpireSeconds = 0;

    public String getLicencePictureUrl() {
        return licencePictureUrl;
    }

    public void setLicencePictureUrl(String licencePictureUrl) {
        this.licencePictureUrl = licencePictureUrl;
    }

    public String getLicencePictureNarrowUrl() {
        return licencePictureNarrowUrl;
    }

    public void setLicencePictureNarrowUrl(String licencePictureNarrowUrl) {
        this.licencePictureNarrowUrl = licencePictureNarrowUrl;
    }

    public String getIdFrontUrl() {
        return idFrontUrl;
    }

    public void setIdFrontUrl(String idFrontUrl) {
        this.idFrontUrl = idFrontUrl;
    }

    public String getIdFrontNarrowUrl() {
        return idFrontNarrowUrl;
    }

    public void setIdFrontNarrowUrl(String idFrontNarrowUrl) {
        this.idFrontNarrowUrl = idFrontNarrowUrl;
    }

    public String getIdBackUrl() {
        return idBackUrl;
    }

    public void setIdBackUrl(String idBackUrl) {
        this.idBackUrl = idBackUrl;
    }

    public String getIdBackNarrowUrl() {
        return idBackNarrowUrl;
    }

    public void setIdBackNarrowUrl(String idBackNarrowUrl) {
        this.idBackNarrowUrl = idBackNarrowUrl;
    }

    public String getBankCardPictureUrl() {
        return bankCardPictureUrl;
    }

    public void setBankCardPictureUrl(String bankCardPictureUrl) {
        this.bankCardPictureUrl = bankCardPictureUrl;
    }

    public String getBankCardPictureNarrowUrl() {
        return bankCardPictureNarrowUrl;
    }

    public void setBankCardPictureNarrowUrl(String bankCardPictureNarrowUrl) {
        this.bankCardPictureNarrowUrl = bankCardPictureNarrowUrl;
    }

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpireSeconds() {
        return tokenExpireSeconds;
    }

    public void setTokenExpireSeconds(long tokenExpireSeconds) {
        this.tokenExpireSeconds = tokenExpireSeconds;
    }

    public String getCompanyOf() {
        return companyOf;
    }

    public void setCompanyOf(String companyOf) {
        this.companyOf = companyOf;
    }

    public Integer getCustomerAccountInfoId() {
        return customerAccountInfoId;
    }

    public void setCustomerAccountInfoId(Integer customerId) {
        this.customerAccountInfoId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getLicencePicture() {
        return licencePicture;
    }

    public void setLicencePicture(String licencePicture) {
        this.licencePicture = licencePicture;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
