package com.fintech.insurance.micro.dto.customer;


import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 客户VO
 * @Date: 2017/11/9 16:51
 */
public class CustomerVO implements Serializable {
    // 客户info id
    private Integer accountInfoId;

    // 客户账户id
    private Integer accountId;

    // 客户名称
    private String name;
    // 客户身份证号
    private String idNum;
    // 客户电话
    private String phone;
    // 所属渠道
    private String channelOf;
    // 所属公司 -- 企业名称
    private String companyOf;
    // 客户状态
    private String status;
    private String bankCard;//银行卡号
    private String bankName;//银行名称
    private String bankBranch;//支行名称

    @ImageUrl
    private String bankCardPicture;//银行卡正面照
    private String bankCardPictureUrl;//银行卡正面照
    private String bankCardPictureNarrowUrl;//银行卡正面照

    @ImageUrl
    private String idFront;//身份证正面照
    private String idFrontUrl;//身份证正面照
    private String idFrontNarrowUrl;//身份证正面照

    @ImageUrl
    private String idBack;//身份证反面照
    private String idBackUrl;//身份证反面照
    private String idBackNarrowUrl;//身份证反面照

    // 企业名称
    private String enterpriseName;

    //企业营业执照号
    private String businessLicence;

    //企业营业执照图片
    @ImageUrl
    private String licencePicture;
    private String licencePictureUrl;
    private String licencePictureNarrowUrl;

    //是否冻结标志位
    private Integer isLocked;

    //手机号
    private String mobile;

    //上上签编号
    private String certificationId;

    //渠道用户
    private Integer channelUserId;

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }
    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannelOf() {
        return channelOf;
    }

    public void setChannelOf(String channelOf) {
        this.channelOf = channelOf;
    }

    public String getCompanyOf() {
        return companyOf;
    }

    public void setCompanyOf(String companyOf) {
        this.companyOf = companyOf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankCardPicture() {
        return bankCardPicture;
    }

    public void setBankCardPicture(String bankCardPicture) {
        this.bankCardPicture = bankCardPicture;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getAccountInfoId() {
        return accountInfoId;
    }

    public void setAccountInfoId(Integer accountInfoId) {
        this.accountInfoId = accountInfoId;
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
}
