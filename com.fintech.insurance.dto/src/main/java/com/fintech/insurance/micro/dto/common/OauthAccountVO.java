package com.fintech.insurance.micro.dto.common;

import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.micro.dto.biz.BaseVO;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方授权帐户信息VO
 */
public class OauthAccountVO extends BaseVO {

    private Integer userId;

    private Boolean isUserLocked;

    private OauthType oauthType;

    private String oauthAppId;

    private String oauthAccount;

    private String nickName;

    private String gender;

    private String headerImage;

    private String wxUnionId;

    private String oauthContent;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public OauthType getOauthType() {
        return oauthType;
    }

    public void setOauthType(OauthType oauthType) {
        this.oauthType = oauthType;
    }

    public String getOauthAppId() {
        return oauthAppId;
    }

    public void setOauthAppId(String oauthAppId) {
        this.oauthAppId = oauthAppId;
    }

    public String getOauthAccount() {
        return oauthAccount;
    }

    public void setOauthAccount(String oauthAccount) {
        this.oauthAccount = oauthAccount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getOauthContent() {
        return oauthContent;
    }

    public void setOauthContent(String oauthContent) {
        this.oauthContent = oauthContent;
    }

    public Boolean getUserLocked() {
        return isUserLocked;
    }

    public void setUserLocked(Boolean userLocked) {
        isUserLocked = userLocked;
    }
}
